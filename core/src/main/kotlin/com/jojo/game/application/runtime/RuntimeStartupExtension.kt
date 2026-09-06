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
    /**
     * `route`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun route(request: RuntimeStartupRequest): Boolean
}

/** RuntimeStartupExtensions: 등록된 시작 확장을 차례로 호출해 처리 가능한 시작 경로를 찾는다. */
internal object RuntimeStartupExtensions {
    /**
     * `route`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun route(request: RuntimeStartupRequest): Boolean =
        ServiceLoader.load(RuntimeStartupExtension::class.java).any { it.route(request) }
}
