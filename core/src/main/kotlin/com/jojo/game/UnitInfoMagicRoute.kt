package com.jojo.game

/**
 * Production route adapter for UnitInfoLayer's magic tab child.
 *
 * The recovered route is button4 TOUCH_END -> a panel4 magic row TOUCH_END ->
 * Global108. Keeping the adapter event-driven prevents fixture code from
 * bypassing the same source input contract used by the live layer.
 */
/**
 * object  `UnitInfoMagicRoute`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
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
