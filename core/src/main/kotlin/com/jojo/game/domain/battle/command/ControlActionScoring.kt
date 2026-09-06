// Battle
package com.jojo.game.domain.battle.command

import com.jojo.game.domain.battle.command.ControlScoring.Arm
import com.jojo.game.domain.battle.command.ControlScoring.Category
import com.jojo.game.domain.battle.command.ControlScoring.Lift
import com.jojo.game.domain.battle.command.ControlScoring.Magic
import com.jojo.game.domain.battle.command.ControlScoring.Skills
import com.jojo.game.domain.battle.command.ControlScoring.Status
import com.jojo.game.domain.battle.command.ControlScoring.Type
import com.jojo.game.domain.battle.command.ControlScoring.Unit
import com.jojo.game.domain.battle.command.ControlScoring.Values
object ControlActionScoring {
fun attackValue(
        attacker: Unit,
        target: Unit,
        counter: Boolean,
        values: Values = Values(),
        skills: Skills = Skills()
    ): Int {
        var score = 0
        val wenGuan = attacker.armType == Arm.WEN_GUAN
        for (harm in attacker.attackHarms(target)) {
            var item = floorRate(harm.harm, values.hpMpRate, harm.target.hp)
            if (harm.rate != 100) item = floorRate(item, harm.rate, values.accuracyBase)
            score += item
            if (counter && attacker.skill(skills.noCounter) == 255 && attacker.status(Status.HL) == Lift.NORMAL) {
                if (attacker.skill(skills.counterSkills) != 255) score += values.counterNoSkill
                var retaliation = attackValue(harm.target, attacker, false, values, skills)
                retaliation -= when (attacker.armType) {
                    Arm.WU_JIANG -> if (attacker.isRemote) 0 else retaliation / 2
                    Arm.QUAN_NENG -> retaliation / 3
                    else -> 0
                }
                score -= retaliation
                if (!wenGuan) score = maxOf(1, score)
            }
            if (!wenGuan) {
                if (harm.flag and 4 != 0 && !attacker.isCanXue()) score += values.kill
                if (harm.flag and 2 != 0) score += values.famous
            }
        }
        if (!wenGuan && target.skill(skills.zszd) != 255) score += values.zszd
        return score
    }



    private fun Unit.skill(ids: IntArray) = ids.firstOrNull { skill(it) != 255 }?.let(::skill) ?: 255
    fun magicValue(
        magic: Magic, caster: Unit, target: Unit, cache: MutableMap<String, Int>,
        hitRate: (Unit, Unit, Magic) -> Int = { _, _, _ -> 100 }, values: Values = Values(), skills: Skills = Skills()
    ): Int {
        val key = "magic_${caster.index}_${target.index}_${magic.id}"
        cache[key]?.let { return it }
        var score = 0
        var hpSteal = false
        var lift = 0
        var abnormal = 0
        var famousMask = if (target.famous) 3 else 1
        when (magic.type) {
            Type.XISHOU_MP -> if (caster.isCanLan() && target.mpCur >= 1) {
                val harm = caster.magicHarm(magic, target).coerceIn(1, target.mpCur)
                if (harm >= 1) score += floorRate(harm, values.hpMpRate, target.mp)
            }

            Type.XISHOU_HP -> if (caster.isCanXue() && target.hpCur >= 1) hpSteal = true
            Type.HUIFU_MP -> if (target.isCanLan() && target.mine == caster.mine) {
                if (magic.category != Category.MX || caster.hpCur > 40) {
                    val gain = if (magic.category == Category.MX) {
                        -floorRate(40, values.hpMpRate, target.hp) + floorRate(target.mpCur, values.hpMpRate, target.mp)
                    } else {
                        val harm = caster.magicHarm(magic, target)
                        -floorRate(harm, values.hpMpRate, caster.mp) + floorRate(harm, values.hpMpRate, target.mp)
                    }
                    score += maxOf(1, gain)
                }
            }

            Type.HUIFU_HP -> if (target.isCanXue() && target.mine == caster.mine) {
                val harm = caster.magicHarm(magic, target).coerceIn(0, target.hp - target.hpCur)
                if (harm >= 1) score += floorRate(
                    harm,
                    values.hpMpRate,
                    target.hp
                ) + if (famousMask and 2 != 0) values.healFamous else values.healNormal
            }

            else -> {
                run category@{
                    when (magic.category) {
                        Category.HFZT -> for (s in Status.MB..Status.ZD) if (target.status(s) != Lift.NORMAL) abnormal =
                            abnormal or (1 shl s)

                        Category.JDNL -> {
                            val k = armMask(target.armType); if (k and 1 != 0) {
                                if (target.status(Status.ATT) == Lift.DOWN) return@category; lift =
                                    lift or (1 shl Status.ATT)
                            }; if (k and 2 != 0) {
                                if (target.status(Status.SPR) == Lift.DOWN) return@category; lift =
                                    lift or (1 shl Status.SPR)
                            }
                        }

                        Category.JDFY -> {
                            if (target.status(Status.DEF) == Lift.DOWN) return@category; lift =
                                lift or (1 shl Status.DEF)
                        }

                        Category.JDMJ -> {
                            if (target.status(Status.CRI) == Lift.DOWN) return@category; lift =
                                lift or (1 shl Status.CRI)
                        }

                        Category.JDSQ -> {
                            if (target.status(Status.MOR) == Lift.DOWN) return@category; lift =
                                lift or (1 shl Status.MOR)
                        }

                        Category.SQ -> for (s in Status.ATT..Status.MOV) if (target.status(s) != Lift.DOWN) lift =
                            lift or (1 shl s)

                        Category.MB -> {
                            if (target.status(Status.MB) != Lift.NORMAL) return@category; lift =
                                lift or (1 shl Status.MB)
                        }

                        Category.FZ -> {
                            if (target.status(Status.JZ) != Lift.NORMAL) return@category; lift =
                                lift or (1 shl Status.JZ)
                        }

                        Category.HL -> {
                            if (target.status(Status.HL) != Lift.NORMAL) return@category; lift =
                                lift or (1 shl Status.HL)
                        }

                        Category.ZD -> {
                            if (target.status(Status.ZD) != Lift.NORMAL) return@category; lift =
                                lift or (1 shl Status.ZD)
                        }
                        Category.ZCXD -> {
                            if (target.ai < 3 || target.status(Status.XD) == Lift.NORMAL) return@category; abnormal =
                                abnormal or (1 shl Status.MOV)
                        }

                        Category.ZJYDL -> {
                            if (target.status(Status.MOV) != Lift.DOWN) return@category; abnormal =
                                abnormal or (1 shl Status.MOV)
                        }

                        Category.ZJMJ -> {
                            if (target.status(Status.CRI) != Lift.DOWN) return@category; abnormal =
                                abnormal or (1 shl Status.CRI)
                        }

                        Category.ZJSQ -> {
                            if (target.status(Status.MOR) != Lift.DOWN) return@category; abnormal =
                                abnormal or (1 shl Status.MOR)
                        }

                        Category.ZJNL -> {
                            val k = armMask(target.armType); if (k and 1 != 0) {
                                if (target.status(Status.ATT) != Lift.DOWN) return@category; abnormal =
                                    abnormal or (1 shl Status.ATT)
                            }; if (k and 2 != 0) {
                                if (target.status(Status.SPR) != Lift.DOWN) return@category; abnormal =
                                    abnormal or (1 shl Status.SPR)
                            }
                        }

                        Category.ZJFY -> {
                            if (target.status(Status.DEF) != Lift.DOWN) return@category; abnormal =
                                abnormal or (1 shl Status.DEF)
                        }
                    }
                }
                score += statusValue(abnormal, target, values) + statusValue(lift, target, values)
            }
        }
        if (magic.harmType != 4) {
            var harm = caster.magicHarm(magic, target).coerceIn(1, target.hpCur)
            if (harm >= target.hpCur) famousMask = famousMask or 4
            score += if (caster.mine == target.mine) -floorRate(harm, values.hpMpRate, target.hp) else floorRate(
                harm,
                values.hpMpRate,
                target.hp
            )
            if (hpSteal) {
                harm = minOf(caster.hp - caster.hpCur, harm); score += floorRate(harm, values.hpMpRate, caster.hp)
            }
        }
        if (score < 1) return 0
        val rate = hitRate(caster, target, magic)
        if (rate != 100) score = floorRate(score, rate, 100)
        score += maxOf(1, score)
        if (famousMask and 2 != 0) score += values.famous
        if (famousMask and 4 != 0) score += values.kill
        if (target.skill(skills.zszd) != 255) score += values.zszd
        return score.also { cache[key] = it }
    }

    private fun armMask(arm: Int) = when (arm) {
        Arm.QUAN_NENG -> 3; Arm.WEN_GUAN -> 2; else -> 1
    }

    private fun floorRate(value: Int, rate: Int, divisor: Int) = if (divisor == 0) 0 else value * rate / divisor
    private fun statusValue(mask: Int, unit: Unit, v: Values): Int {
        var n = 0


        fun has(s: Int) = mask and (1 shl s) != 0
        if (has(Status.ATT)) n += if (unit.armType == Arm.QUAN_NENG) v.attackQn else if (unit.armType == Arm.WU_JIANG) v.attackWj else 0
        if (has(Status.DEF)) n += when (unit.armType) {
            Arm.QUAN_NENG -> v.attackQn; Arm.WEN_GUAN -> v.defWg; else -> v.defWj
        }
        if (has(Status.SPR)) n += if (unit.armType == Arm.QUAN_NENG) v.sprQn else if (unit.armType == Arm.WEN_GUAN) v.sprWg else 0
        if (has(Status.CRI)) n += if (unit.armType == Arm.QUAN_NENG) v.criQn else if (unit.armType == Arm.WU_JIANG) v.criWj else 0
        if (has(Status.MOR)) n += if (unit.armType == Arm.QUAN_NENG) v.morQn else if (unit.armType == Arm.WU_JIANG) v.morWj else 0
        if (has(Status.MOV)) n += v.mov
        if (has(Status.MB)) n += v.mabi
        if (has(Status.JZ)) n += when (unit.armType) {
            Arm.QUAN_NENG -> v.jzQn; Arm.WEN_GUAN -> v.jzWg; else -> v.jzWj
        }
        if (has(Status.HL)) n += when (unit.armType) {
            Arm.QUAN_NENG -> v.hlQn; Arm.WEN_GUAN -> v.hlWg; else -> v.hlWj
        }
        if (has(Status.ZD)) n += when (unit.armType) {
            Arm.QUAN_NENG -> v.zdQn; Arm.WEN_GUAN -> v.zdWg; else -> v.zdWj
        }
        if (has(Status.XD)) n += unit.aiValue
        return n
    }
}
