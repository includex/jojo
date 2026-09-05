package com.jojo.port

/**
 * Direct state port of BattleLayer._skillTempValues.
 *
 * The source stores a value together with the round in which it was written.
 * resetSkillTemp(previousRound) removes RESET values, retains NONE values, and
 * retains NEXT_ROUND values only when they were written during previousRound.
 */
class BattleSkillTemp(
    private val resetTypeForSkill: (Int) -> ResetType = { ResetType.RESET },
) {
    enum class ResetType { RESET, NONE, NEXT_ROUND }

    private data class Value(val amount: Int, val writtenRound: Int)
    private val values = linkedMapOf<String, MutableMap<Int, Value>>()

    fun increment(unitId: String, skillId: Int, currentRound: Int): Int =
        (value(unitId, skillId) + 1).also { set(unitId, skillId, it, currentRound) }

    fun set(unitId: String, skillId: Int, amount: Int, currentRound: Int) {
        values.getOrPut(unitId) { linkedMapOf() }[skillId] = Value(amount, currentRound)
    }

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
