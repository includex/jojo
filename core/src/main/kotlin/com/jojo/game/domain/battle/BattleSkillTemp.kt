// Battle
package com.jojo.game.domain.battle

/** BattleSkillTemp: 라운드별 초기화 규칙을 따르는 임시 스킬 값을 보관하며, 지속 효과의 남은 상태를 관리한다. */
class BattleSkillTemp(
    /**
     * `resetTypeForSkill` ((Int) -> ResetType): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val resetTypeForSkill: (Int) -> ResetType = { ResetType.RESET },
) {
    /** 임시 스킬 값의 유지 기간을 나타낸다. */
    enum class ResetType { RESET, NONE, NEXT_ROUND }

    /**
     * `Value` 클래스: battle 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    private data class Value(val amount: Int, val writtenRound: Int)

    /**
     * `values` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

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

    /**
     * `snapshot`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    internal fun snapshot(): Map<String, Map<Int, Pair<Int, Int>>> = values.mapValues { (_, skills) ->
        skills.mapValues { (_, value) -> value.amount to value.writtenRound }
    }

    /**
     * `restore`: 입력을 규칙에 따라 계산·변환한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

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
