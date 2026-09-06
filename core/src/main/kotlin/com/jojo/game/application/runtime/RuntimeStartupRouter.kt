// Runtime
package com.jojo.game.application.runtime

import com.jojo.game.*

import com.badlogic.gdx.Screen
import com.jojo.game.domain.campaign.CampaignState
import com.jojo.game.domain.scenario.ScenarioJoinBattleLimit

/** RuntimeStartupRouter: 게임 시작 인자를 RuntimeStartupRequest로 묶어 확장 시작 경로에 위임한다. */
internal class RuntimeStartupRouter(
    /**
     * `game` (JojoGame,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val game: JojoGame,
    /**
     * `state` (String?,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val state: String?,
    /**
     * `campaign` (CampaignState,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val campaign: CampaignState,
    /**
     * `showScreen` ((Screen) -> Unit,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val showScreen: (Screen) -> Unit,
    /**
     * `showBattlePreparation` ((String, String, ScenarioJoinBattleLimit, Int) -> Unit,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val showBattlePreparation: (String, String, ScenarioJoinBattleLimit, Int) -> Unit,
) {
    /**
     * `route`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun route(): Boolean = RuntimeStartupExtensions.route(
        RuntimeStartupRequest(game, state, campaign, showScreen, showBattlePreparation),
    )
}
