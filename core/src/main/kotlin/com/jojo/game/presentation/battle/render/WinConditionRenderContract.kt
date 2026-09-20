// Battle
package com.jojo.game.presentation.battle.render

import com.badlogic.gdx.graphics.Color

/**
 * `WinConditionRenderContract`: 승리 조건 창의 글자색을 한 곳에 둔다.
 *
 * 그리기 쪽 `BattleScreen.drawWinConditionBox`와 증거 쪽 `BattleWinConditionRenderEvents`가
 * 같은 값을 읽는다. 앞서는 그리기만 색을 들고 있었고 증거는 색을 적지 않아, 비교기가 그 행을
 * 통째로 건너뛰었다. 그래서 포트가 본문을 `#003fff`, 버튼을 `#00d100`으로 그리고 있어도
 * 어떤 게이트도 떨어지지 않았다.
 */
object WinConditionRenderContract {
    /**
     * `BODY`: 조건 본문 글자색이다.
     *
     * 원본 `WinConBoxLayer`의 `bg0/scrollview/view/content/item` 노드 색은 (14,1,222)이다.
     * 포트는 `Color(0f, .25f, 1f, 1f)` = (0,64,255)를 쓰고 있었다.
     */
    const val BODY_HEX = "#0e01de"

    /**
     * `BUTTON`: 「짐이 알겠다.」 단추 글자색이다.
     *
     * 원본 같은 프리팹 `bg0/button/Background/Label` 노드 색은 (2,91,0)이다.
     * 포트는 `Color(0f, .82f, 0f, 1f)` = (0,209,0)을 쓰고 있었다.
     */
    const val BUTTON_HEX = "#025b00"

    /** 조건 본문 색: 그리기 쪽이 글꼴에 넣는 값이다. */
    val body: Color = Color.valueOf("${BODY_HEX}ff")

    /** 단추 문구 색: 그리기 쪽이 글꼴에 넣는 값이다. */
    val button: Color = Color.valueOf("${BUTTON_HEX}ff")
}
