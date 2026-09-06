// Runtime
package com.jojo.game.application.runtime

import com.jojo.game.*

import com.badlogic.gdx.Screen
import com.jojo.game.domain.campaign.CampaignState
import com.jojo.game.domain.scenario.ScenarioJoinBattleLimit
import java.util.ServiceLoader

/** RuntimeStartupRequest: 확장 시작 경로가 화면 교체와 캠페인 준비에 사용할 실행 문맥이다. */
data class RuntimeStartupRequest(
    val game: JojoGame,
    val state: String?,
    val campaignState: CampaignState,
    val showScreen: (Screen) -> Unit,
    val showBattlePreparation: (String, String, ScenarioJoinBattleLimit, Int) -> Unit,
)

/** RuntimeStartupExtension: 특정 캡처·검증 상태를 전용 시작 화면으로 연결하는 확장 지점이다. */
interface RuntimeStartupExtension {
    fun route(request: RuntimeStartupRequest): Boolean
}

/** RuntimeStartupExtensions: 등록된 시작 확장을 차례로 호출해 처리 가능한 시작 경로를 찾는다. */
internal object RuntimeStartupExtensions {
    fun route(request: RuntimeStartupRequest): Boolean =
        ServiceLoader.load(RuntimeStartupExtension::class.java).any { it.route(request) }
}
