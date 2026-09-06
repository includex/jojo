package com.jojo.game

/**
 * Production route adapter for UnitInfoLayer's magic tab child.
 *
 * The recovered route is button4 TOUCH_END -> a panel4 magic row TOUCH_END ->
 * Global108. Keeping the adapter event-driven prevents fixture code from
 * bypassing the same source input contract used by the live layer.
 */

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
