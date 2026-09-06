// Shared
package com.jojo.game.presentation.shared.overlay


/** UnitInfoMagicRoute: UnitInfoLayer의 마법 탭 하위 화면 진입 어댑터이다. 버튼과 마법 행의 TOUCH_END 입력을 순서대로 처리해 Global108 경로를 생성하며, 실제 입력 계약을 우회하지 않는다. */

object UnitInfoMagicRoute {
    fun open(
        unitInfo: UnitInfoLayer,
        magics: List<MagicUiList.Magic>,
        row: Int = 0,
        event: Int = UnitInfoLayer.TOUCH_END,
    ): MagicInfoLayer? {
        if (!unitInfo.onButton(4, event)) return null
        if (!unitInfo.onMagic(row, event)) return null
        val request = unitInfo.takeRoutes().lastOrNull {
            it.route == UnitInfoLayer.Route.MAGIC && it.index == row
        } ?: return null
        val magic = magics.getOrNull(request.index)?.takeIf { it.name == request.value } ?: return null
        return MagicInfoLayer(magic)
    }
}
