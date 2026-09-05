package com.jojo.game.verification

/** Isolated EditLayer3 source oracle, reachable only by the verification flag. */
internal class EditGlobalSourceOracle(
    ambition: Int,
    money: Int,
    stage: Int,
    private val names: List<String>
) {
    enum class Field { SCENE, AMBITION, MONEY }

    sealed interface Effect {
        data class SetAmbition(val value: Int) : Effect
        data class SetMoney(val value: Int) : Effect
        data class SetStage(val value: Int) : Effect
        data object ReplaceHall : Effect
        data object Close : Effect
        data object AskClearInventory : Effect
        data object ClearInventory : Effect
        data class Toast(val text: String) : Effect
    }

    private val original = mutableMapOf(Field.AMBITION to ambition, Field.MONEY to money)
    private val pending = linkedMapOf<Field, Int>()
    var sceneLabel = names[stage.coerceIn(names.indices)] + if (stage % 2 == 0) "R" else "S"
        private set

    fun endEdit(field: Field, value: Int) {
        if (original[field] == value) pending.remove(field) else pending[field] = value
    }

    fun selectScene(index: Int) {
        pending[Field.SCENE] = index
        sceneLabel = names[index]
    }

    fun button(tag: Int): List<Effect> = when (tag) {
        0 -> buildList {
            pending.forEach { (field, value) ->
                when (field) {
                    Field.SCENE -> addAll(listOf(Effect.SetStage(value * 2), Effect.ReplaceHall))
                    Field.AMBITION -> add(Effect.SetAmbition(value.coerceIn(1, 100)))
                    Field.MONEY -> add(Effect.SetMoney(value.coerceIn(0, 9_999_999)))
                }
            }
            add(Effect.Close)
        }
        2 -> listOf(Effect.AskClearInventory)
        else -> emptyList()
    }

    fun clearInventoryAnswer(answer: Int) =
        if (answer == 0) listOf(Effect.ClearInventory, Effect.Toast("모든 장비와 아이템을 버렸습니다!")) else emptyList()
}
