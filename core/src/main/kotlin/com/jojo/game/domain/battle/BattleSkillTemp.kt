package com.jojo.game.domain.battle

/** 라운드별 초기화 규칙을 따르는 전투 임시 스킬 값을 보관한다. */
class BattleSkillTemp(
    private val resetTypeForSkill: (Int) -> ResetType = { ResetType.RESET },
) {
    /** 임시 스킬 값의 유지 기간을 나타낸다. */
    enum class ResetType { RESET, NONE, NEXT_ROUND }

    private data class Value(val amount: Int, val writtenRound: Int)

    private val values = linkedMapOf<String, MutableMap<Int, Value>>()

    /** 유닛 스킬의 임시 값을 1 증가시키고 결과를 반환한다. */
    fun increment(unitId: String, skillId: Int, currentRound: Int): Int =
        (value(unitId, skillId) + 1).also { set(unitId, skillId, it, currentRound) }

    /** 유닛 스킬의 임시 값과 기록 라운드를 저장한다. */
    fun set(unitId: String, skillId: Int, amount: Int, currentRound: Int) {
        values.getOrPut(unitId) { linkedMapOf() }[skillId] = Value(amount, currentRound)
    }

    /** 저장된 임시 값을 조회하고 없으면 기본값을 반환한다. */
    fun value(unitId: String, skillId: Int, default: Int = 0): Int = values[unitId]?.get(skillId)?.amount ?: default

    internal fun snapshot(): Map<String, Map<Int, Pair<Int, Int>>> = values.mapValues { (_, skills) ->
        skills.mapValues { (_, value) -> value.amount to value.writtenRound }
    }

    internal fun restore(snapshot: Map<String, Map<Int, Pair<Int, Int>>>) {
        values.clear()
        snapshot.forEach { (unitId, skills) ->
            val restored = linkedMapOf<Int, Value>()
            skills.forEach { (skillId, value) -> restored[skillId] = Value(value.first, value.second) }
            values[unitId] = restored
        }
    }

    /** 초기화 규칙에 따라 이전 라운드의 임시 값을 정리한다. */
    fun reset(previousRound: Int) {
        val retained = values.flatMap { (unitId, skills) ->
            skills.mapNotNull { (skillId, stored) ->
                val keep = when (resetTypeForSkill(skillId)) {
                    ResetType.NONE -> true
                    ResetType.NEXT_ROUND -> stored.writtenRound == previousRound
                    ResetType.RESET -> false
                }
                if (keep) Triple(unitId, skillId, stored) else null
            }
        }
        values.clear()
        retained.forEach { (unitId, skillId, stored) ->
            values.getOrPut(unitId) { linkedMapOf() }[skillId] = stored
        }
    }
}
