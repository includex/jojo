// Battle
package com.jojo.game.presentation.battle.preparation
/**
 * `BattlePreparationController`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

internal class BattlePreparationController(
    availableIds: List<Int>,
    requiredIds: List<Int>,
    val minimum: Int,
    val maximum: Int,
) {
    /**
     * `availableIds` (List<Int>): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val availableIds: List<Int> = availableIds.toList()
    /**
     * `requiredIds` (List<Int>): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val requiredIds: List<Int> = requiredIds.filter { it in this.availableIds }.distinct()
    /**
     * `selectedIds` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val selectedIds = this.requiredIds.toMutableList()
    /**
     * `cursor` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var cursor: Int = this.availableIds.indexOf(selectedIds.firstOrNull()).coerceAtLeast(0)
        private set

    /**
     * `selection` (List<Int> get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val selection: List<Int> get() = selectedIds.toList()
    /**
     * `cursorId` (Int? get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val cursorId: Int? get() = availableIds.getOrNull(cursor) ?: selectedIds.firstOrNull()
    /**
     * `canStart` (Boolean get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val canStart: Boolean get() = selectedIds.size in minimum..maximum


    /**
     * `moveCursor`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun moveCursor(offset: Int) {
        if (availableIds.isNotEmpty()) cursor = Math.floorMod(cursor + offset, availableIds.size)
    }


    /**
     * `toggle`: 입력을 규칙에 따라 계산·변환한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun toggle(id: Int?) {
        id ?: return
        if (id in requiredIds) return
        if (id in selectedIds) selectedIds.remove(id)
        else if (selectedIds.size < maximum && id in availableIds) selectedIds += id
    }


    /**
     * `commit`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun commit(): List<Int>? = selection.takeIf { canStart }

    /** touch: 입력 또는 이벤트를 반영해 전투 상태를 전환한다. */
    fun touch(x: Float, y: Float, sortOpen: Boolean): BattlePreparationAction {
        if (sortOpen) return if (x !in 658f..831f || y !in 32f..276f) {
            BattlePreparationAction.CancelSort
        } else {
            BattlePreparationAction.SelectSort(((276f - y) / 46.4f).toInt().coerceIn(0, 4))
        }
        availableIds.forEachIndexed { index, id ->
            val cx = (233.686f + index % 6 * 133f) * SCALE
            val cy = (667.5f - index / 6 * 144f) * SCALE
            if (x in cx - 57.2f..cx + 57.2f && y in cy - 61.9f..cy + 61.9f) {
                cursor = index
                toggle(id)
                return BattlePreparationAction.SelectionChanged
            }
        }
        selectedIds.toList().forEachIndexed { index, id ->
            val cx = (217.336f + index * 100f) * SCALE
            if (x in cx - 43f..cx + 43f && y in 183f..278f) {
                cursor = availableIds.indexOf(id).coerceAtLeast(0)
                toggle(id)
                return BattlePreparationAction.SelectionChanged
            }
        }
        if (y in 49f..93f && x in 954f..1040f) return BattlePreparationAction.Start
        if (y in 49f..93f && x in 1048f..1135f) return BattlePreparationAction.Cancel
        if (x in 658f..831f && y in 276f..320f) return BattlePreparationAction.OpenSort
        return BattlePreparationAction.None
    }

    private companion object {
        /**
         * `SCALE` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val SCALE = .86f
    }
}
/**
 * `BattlePreparationAction`: 관련 상태와 동작을 묶는 interface다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

internal sealed interface BattlePreparationAction {
    /**
     * `None`: 관련 상태와 동작을 묶는 object다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data object None : BattlePreparationAction
    /**
     * `SelectionChanged`: 관련 상태와 동작을 묶는 object다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data object SelectionChanged : BattlePreparationAction
    /**
     * `Start`: 관련 상태와 동작을 묶는 object다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data object Start : BattlePreparationAction
    /**
     * `Cancel`: 관련 상태와 동작을 묶는 object다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data object Cancel : BattlePreparationAction
    /**
     * `OpenSort`: 관련 상태와 동작을 묶는 object다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data object OpenSort : BattlePreparationAction
    /**
     * `CancelSort`: 관련 상태와 동작을 묶는 object다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data object CancelSort : BattlePreparationAction
    /**
     * `SelectSort`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class SelectSort(val index: Int) : BattlePreparationAction
}
