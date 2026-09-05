package com.jojo.game.presentation.battle.assets

/**
 * Authored Cocos SpriteFrame paths used by the battle's modal UI.
 *
 * The source routes use one-based resource names (`Game/Magic/${icon + 1}-1`,
 * etc.).  Keeping that conversion here prevents renderers from substituting
 * a generic icon or from accidentally treating a magic id as an icon id.
 */
/**
 * object  `BattleUiAssets`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

object BattleUiAssets {
    const val CHOICE_PANEL = "maps/ui/choice-panel.png"
    const val CHOICE_ROW = "maps/ui/choice-row.png"
    const val MP_CURRENT_MARK = "maps/marks/1.png"
    const val MP_MAX_MARK = "maps/marks/2.png"

    /**
     * 공개 메서드 `magicIcon`
     *
     * ### 파라미터
    - `icon` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `String`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun magicIcon(icon: Int): String = "maps/magic-icons/${icon + 1}.png"

    /**
     * 공개 메서드 `hitArea`
     *
     * ### 파라미터
    - `hitArea` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `String`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun hitArea(hitArea: Int): String = "maps/magic-hitareas/${hitArea + 1}.png"

    /**
     * 공개 메서드 `effectArea`
     *
     * ### 파라미터
    - `effectArea` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `String`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun effectArea(effectArea: Int): String = "maps/magic-effareas/${effectArea + 1}.png"
}
