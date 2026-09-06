// 시나리오 거점 상호작용 실행 어댑터
package com.jojo.game.presentation.scenario.input

import com.jojo.game.presentation.scenario.hall.HallInteractionIntent

/** ScenarioHallInteractionPort: 거점 입력 의도를 실제 Screen 동작으로 연결하기 위한 최소 명령 경계다. */
interface ScenarioHallInteractionPort {
    /** startBattle: 거점의 전투 시작 선택을 다음 전투 화면 전환으로 위임한다. */
    fun startBattle()
    /** openManagement: 지정한 관리 메뉴를 열도록 Screen에 요청한다. */
    fun openManagement(kindName: String)
    /** selectHallMenu: 일반 거점 메뉴 행의 선택을 Screen 상태에 반영한다. */
    fun selectHallMenu(index: Int)
}

/** ScenarioHallInteractionExecutor: 순수 입력 의도를 Port의 부수효과 호출 하나로 변환한다. */
internal object ScenarioHallInteractionExecutor {
    /** execute: 닫힘·열기처럼 Screen 변경이 없는 의도는 소비하고, 나머지는 대응 명령을 실행한다. */
    fun execute(intent: HallInteractionIntent, port: ScenarioHallInteractionPort) = when (intent) {
        HallInteractionIntent.None, HallInteractionIntent.MenuClosed, HallInteractionIntent.OpenMenu -> Unit
        HallInteractionIntent.StartBattle -> port.startBattle()
        is HallInteractionIntent.OpenManagement -> port.openManagement(intent.kind.name)
        is HallInteractionIntent.MenuSelection -> port.selectHallMenu(intent.index)
    }
}
