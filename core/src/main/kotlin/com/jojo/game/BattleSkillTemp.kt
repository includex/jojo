package com.jojo.game

/**
 * Direct state implementation of BattleScreen._skillTempValues.
 *
 * The source stores a value together with the round in which it was written.
 * resetSkillTemp(previousRound) removes RESET values, retains NONE values, and
 * retains NEXT_ROUND values only when they were written during previousRound.
 */
/**
 * class  `BattleSkillTemp`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class BattleSkillTemp(
    private val resetTypeForSkill: (Int) -> ResetType = { ResetType.RESET },
) {
    /**
     * enum class  `ResetType`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    enum class ResetType { RESET, NONE, NEXT_ROUND }

    private data class Value(val amount: Int, val writtenRound: Int)

    private val values = linkedMapOf<String, MutableMap<Int, Value>>()

    /**
     * 공개 메서드 `increment`
     *
     * ### 파라미터
    - `unitId` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `skillId` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `currentRound` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Int`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun increment(unitId: String, skillId: Int, currentRound: Int): Int =
        (value(unitId, skillId) + 1).also { set(unitId, skillId, it, currentRound) }

    /**
     * 공개 메서드 `set`
     *
     * ### 파라미터
    - `unitId` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `skillId` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `amount` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `currentRound` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun set(unitId: String, skillId: Int, amount: Int, currentRound: Int) {
        values.getOrPut(unitId) { linkedMapOf() }[skillId] = Value(amount, currentRound)
    }

    /**
     * 공개 메서드 `value`
     *
     * ### 파라미터
    - `unitId` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `skillId` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `default` (`Int = 0`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Int`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

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

    /**
     * 공개 메서드 `reset`
     *
     * ### 파라미터
    - `previousRound` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

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
