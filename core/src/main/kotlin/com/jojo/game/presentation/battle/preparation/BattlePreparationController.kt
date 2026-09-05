package com.jojo.game.presentation.battle.preparation

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

    /**
     * 공개 메서드 `moveCursor`
     *
     * ### 파라미터
    - `offset` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun moveCursor(offset: Int) {
        if (availableIds.isNotEmpty()) cursor = Math.floorMod(cursor + offset, availableIds.size)
    }

    /**
     * 공개 메서드 `toggle`
     *
     * ### 파라미터
    - `id` (`Int?`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun toggle(id: Int?) {
        id ?: return
        if (id in requiredIds) return
        if (id in selectedIds) selectedIds.remove(id)
        else if (selectedIds.size < maximum && id in availableIds) selectedIds += id
    }

    /**
     * 공개 메서드 `commit`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `List<Int>?`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

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

    private companion object {
        const val SCALE = .86f
    }
}

internal sealed interface BattlePreparationAction {
    data object None : BattlePreparationAction
    data object SelectionChanged : BattlePreparationAction
    data object Start : BattlePreparationAction
    data object Cancel : BattlePreparationAction
    data object OpenSort : BattlePreparationAction
    data object CancelSort : BattlePreparationAction

    /**
     * data class  `SelectSort`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class SelectSort(val index: Int) : BattlePreparationAction
}
