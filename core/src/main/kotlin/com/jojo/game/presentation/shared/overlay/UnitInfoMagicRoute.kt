// Shared
package com.jojo.game.presentation.shared.overlay


/** UnitInfoMagicRoute: UnitInfoLayer의 마법 탭 하위 화면 진입 어댑터이다. 버튼과 마법 행의 TOUCH_END 입력을 순서대로 처리해 Global108 경로를 생성하며, 실제 입력 계약을 우회하지 않는다. */

object UnitInfoMagicRoute {
    /**
     * `open`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun open(
        unitInfo: UnitInfoLayer,
        magics: List<MagicUiList.Magic>,
        row: Int = 0,
        event: Int = UnitInfoLayer.TOUCH_END,
    ): MagicInfoLayer? {
        if (!unitInfo.onButton(4, event)) return null
        if (!unitInfo.onMagic(row, event)) return null
        /**
         * `request` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val request = unitInfo.takeRoutes().lastOrNull {
            it.route == UnitInfoLayer.Route.MAGIC && it.index == row
        } ?: return null
        /**
         * `magic` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val magic = magics.getOrNull(request.index)?.takeIf { it.name == request.value } ?: return null
        return MagicInfoLayer(magic)
    }
}
