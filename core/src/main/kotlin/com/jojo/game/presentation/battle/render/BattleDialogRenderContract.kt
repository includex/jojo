// Battle
package com.jojo.game.presentation.battle.render

import com.jojo.game.presentation.shared.overlay.MagicUiList
import com.jojo.game.presentation.battle.assets.BattleUiAssets

/** 전투 대화 렌더 계약: 마법 목록과 상세 창의 원본 아이콘·범위 이미지 배치 좌표를 고정한다. */
object BattleDialogRenderContract {
    /** 대화 창 스프라이트: 원본 자원 경로와 화면 출력 사각형을 함께 보관한다. */
    data class Sprite(val path: String, val x: Float, val y: Float, val width: Float, val height: Float)

    /** 목록 아이콘: 마법 행의 기준 좌표에서 아이콘 출력 사각형을 계산한다. */
    fun magicListIcon(magic: MagicUiList.Magic, x: Float, y: Float) =
        Sprite(BattleUiAssets.magicIcon(magic.icon), x + 5.073f, y + 57.383f, 76.8f, 76.8f)

    /** 상세 스프라이트: 선택한 마법의 아이콘·대상 범위·효과 범위를 원본 레이아웃 순서로 반환한다. */
    fun magicDetailSprites(magic: MagicUiList.Magic) = listOf(
        Sprite(BattleUiAssets.magicIcon(magic.icon), 478.186f, 562f, 80f, 80f),
        Sprite(BattleUiAssets.hitArea(magic.hit), 834.213f, 450.755f, 160f, 160f),
        Sprite(BattleUiAssets.effectArea(magic.eff), 834.213f, 219.367f, 160f, 160f),
    )
}
