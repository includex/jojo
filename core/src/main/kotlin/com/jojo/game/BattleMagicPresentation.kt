package com.jojo.game
import com.jojo.game.domain.battle.*
import com.jojo.game.domain.battle.*

/**
 * Pure projection of BattleScreen._magicProcess's `l` (target) and `o`
 * (caster recovery) char-info groups.  Battle has already committed the
 * result; this planner retains the old visual value until `playMeff` ends.
 */
/**
 * object  `BattleMagicPresentation`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

object BattleMagicPresentation {
    /**
     * data class  `Change`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Change(val unitId: String, val hpAdd: Int = 0, val mpAdd: Int = 0)

    fun changes(
        result: TacticalActionResult.Magic,
        casterId: String,
        magic: GameDataCatalog.MagicProfile?,
    ): List<Change> = changes(result.targets, casterId, magic)

    fun changes(
        targets: List<MagicTarget>,
        casterId: String,
        magic: GameDataCatalog.MagicProfile?,
    ): List<Change> {
        val values = linkedMapOf<String, Change>()

        /**
         * 공개 메서드 `add`
         *
         * ### 파라미터
        - `id` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `hp` (`Int = 0`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `mp` (`Int = 0`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun add(id: String, hp: Int = 0, mp: Int = 0) {
            val old = values[id] ?: Change(id)
            values[id] = old.copy(hpAdd = old.hpAdd + hp, mpAdd = old.mpAdd + mp)
        }
        targets.forEach { target ->
            add(target.targetId, hp = target.healing - target.damage)
            when {
                target.magicDrain > 0 -> {
                    add(target.targetId, mp = -target.magicDrain)
                    add(casterId, mp = target.magicRecovery)
                }
                // MX (HP -> caster MP) stores target HP loss in damage and
                // recovery in the caster's `o` char-info group.
                magic?.type == 20 && magic.category == 24 && target.magicRecovery > 0 ->
                    add(casterId, mp = target.magicRecovery)

                target.magicRecovery > 0 -> add(target.targetId, mp = target.magicRecovery)
            }
            if (target.casterHealing > 0) add(casterId, hp = target.casterHealing)
        }
        return values.values.filter { it.hpAdd != 0 || it.mpAdd != 0 }
    }
}
