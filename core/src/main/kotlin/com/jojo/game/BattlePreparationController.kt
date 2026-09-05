package com.jojo.game

/** Pure selection and pointer policy for battle preparation. */
internal class BattlePreparationController(
    availableIds: List<Int>,
    requiredIds: List<Int>,
    val minimum: Int,
    val maximum: Int,
) {
    val availableIds: List<Int> = availableIds.toList()
    val requiredIds: List<Int> = requiredIds.filter { it in this.availableIds }.distinct()
    private val selectedIds = this.requiredIds.toMutableList()
    var cursor: Int = this.availableIds.indexOf(selectedIds.firstOrNull()).coerceAtLeast(0)
        private set

    val selection: List<Int> get() = selectedIds.toList()
    val cursorId: Int? get() = availableIds.getOrNull(cursor) ?: selectedIds.firstOrNull()
    val canStart: Boolean get() = selectedIds.size in minimum..maximum

    fun moveCursor(offset: Int) {
        if (availableIds.isNotEmpty()) cursor = Math.floorMod(cursor + offset, availableIds.size)
    }

    fun toggle(id: Int?) {
        id ?: return
        if (id in requiredIds) return
        if (id in selectedIds) selectedIds.remove(id)
        else if (selectedIds.size < maximum && id in availableIds) selectedIds += id
    }

    fun commit(): List<Int>? = selection.takeIf { canStart }

    /** Hit order mirrors the scene: modal sort, roster, selected slots, buttons, then sort opener. */
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

    private companion object { const val SCALE = .86f }
}

internal sealed interface BattlePreparationAction {
    data object None : BattlePreparationAction
    data object SelectionChanged : BattlePreparationAction
    data object Start : BattlePreparationAction
    data object Cancel : BattlePreparationAction
    data object OpenSort : BattlePreparationAction
    data object CancelSort : BattlePreparationAction
    data class SelectSort(val index: Int) : BattlePreparationAction
}
