package com.jojo.game.presentation.battle.render

import com.jojo.game.MagicUiList
import com.jojo.game.presentation.battle.assets.BattleUiAssets

/** Literal Cocos SpriteFrame geometry for the battle MagicLayer fixture. */
object BattleDialogRenderContract {
    /**
     * data class  `Sprite`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Sprite(val path: String, val x: Float, val y: Float, val width: Float, val height: Float)

    /**
     * 공개 메서드 `magicListIcon`
     *
     * ### 파라미터
    - `magic` (`MagicUiList.Magic`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `x` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `y` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun magicListIcon(magic: MagicUiList.Magic, x: Float, y: Float) =
        Sprite(BattleUiAssets.magicIcon(magic.icon), x + 5.073f, y + 57.383f, 76.8f, 76.8f)

    /**
     * 공개 메서드 `magicDetailSprites`
     *
     * ### 파라미터
    - `magic` (`MagicUiList.Magic`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun magicDetailSprites(magic: MagicUiList.Magic) = listOf(
        Sprite(BattleUiAssets.magicIcon(magic.icon), 478.186f, 562f, 80f, 80f),
        Sprite(BattleUiAssets.hitArea(magic.hit), 834.213f, 450.755f, 160f, 160f),
        Sprite(BattleUiAssets.effectArea(magic.eff), 834.213f, 219.367f, 160f, 160f),
    )
}
