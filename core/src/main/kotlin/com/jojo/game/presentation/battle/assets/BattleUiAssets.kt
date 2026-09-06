// Battle
package com.jojo.game.presentation.battle.assets

/** 전투 모달 UI에서 사용하는 스프라이트 경로를 생성합니다. */
object BattleUiAssets {
    /**
     * `CHOICE_PANEL` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    const val CHOICE_PANEL = "maps/ui/choice-panel.png"
    /**
     * `CHOICE_ROW` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    const val CHOICE_ROW = "maps/ui/choice-row.png"
    /**
     * `MP_CURRENT_MARK` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    const val MP_CURRENT_MARK = "maps/marks/1.png"
    /**
     * `MP_MAX_MARK` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    const val MP_MAX_MARK = "maps/marks/2.png"

    /** 마법 아이콘의 리소스 경로를 반환합니다. */
    fun magicIcon(icon: Int): String = "maps/magic-icons/${icon + 1}.png"

    /** 마법 타격 영역 아이콘의 리소스 경로를 반환합니다. */
    fun hitArea(hitArea: Int): String = "maps/magic-hitareas/${hitArea + 1}.png"

    /** 마법 효과 영역 아이콘의 리소스 경로를 반환합니다. */
    fun effectArea(effectArea: Int): String = "maps/magic-effareas/${effectArea + 1}.png"
}
