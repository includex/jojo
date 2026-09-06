// Verification
package com.jojo.game.verification

/** EditGlobalSourceOracle: 검증 플래그로만 접근하는 EditLayer3 원본 기준 구현이다. */
internal class EditGlobalSourceOracle(
    ambition: Int,
    money: Int,
    stage: Int,
    /** names: 검증 흐름에서 사용하는 값을 담는다. */
    private val names: List<String>
) {
    /** Field: field 관련 검증 상태와 동작을 제공하는 타입이다. */
    enum class Field { SCENE, AMBITION, MONEY }

    /** Effect: effect 관련 검증 상태와 동작을 제공하는 타입이다. */
    sealed interface Effect {
        /** SetAmbition: set ambition 관련 검증 상태와 동작을 제공하는 타입이다. */
        data class SetAmbition(val value: Int) : Effect
        /** SetMoney: set money 관련 검증 상태와 동작을 제공하는 타입이다. */
        data class SetMoney(val value: Int) : Effect
        /** SetStage: set stage 관련 검증 상태와 동작을 제공하는 타입이다. */
        data class SetStage(val value: Int) : Effect
        /** ReplaceHall: replace hall 관련 검증 상태와 동작을 제공하는 타입이다. */
        data object ReplaceHall : Effect
        /** Close: close 관련 검증 상태와 동작을 제공하는 타입이다. */
        data object Close : Effect
        /** AskClearInventory: ask clear inventory 관련 검증 상태와 동작을 제공하는 타입이다. */
        data object AskClearInventory : Effect
        /** ClearInventory: clear inventory 관련 검증 상태와 동작을 제공하는 타입이다. */
        data object ClearInventory : Effect
        /** Toast: toast 관련 검증 상태와 동작을 제공하는 타입이다. */
        data class Toast(val text: String) : Effect
    }

    /** original: 검증 흐름에서 사용하는 값을 담는다. */
    private val original = mutableMapOf(Field.AMBITION to ambition, Field.MONEY to money)
    /** pending: 검증 흐름에서 사용하는 값을 담는다. */
    private val pending = linkedMapOf<Field, Int>()
    /** sceneLabel: scene label 값을 보관해 검증 흐름에서 사용한다. */
    var sceneLabel = names[stage.coerceIn(names.indices)] + if (stage % 2 == 0) "R" else "S"
        private set

    /** endEdit: end edit에 필요한 검증 동작을 실행하고 결과를 반환한다. */
    fun endEdit(field: Field, value: Int) {
        if (original[field] == value) pending.remove(field) else pending[field] = value
    }

    /** selectScene: select scene에 필요한 검증 동작을 실행하고 결과를 반환한다. */
    fun selectScene(index: Int) {
        pending[Field.SCENE] = index
        sceneLabel = names[index]
    }

    /** button: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
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

    /** clearInventoryAnswer: 검증 상태를 초기값으로 되돌린다. */
    fun clearInventoryAnswer(answer: Int) =
        if (answer == 0) listOf(Effect.ClearInventory, Effect.Toast("모든 장비와 아이템을 버렸습니다!")) else emptyList()
}
