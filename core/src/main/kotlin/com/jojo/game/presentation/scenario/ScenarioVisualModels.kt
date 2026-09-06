// Scenario
package com.jojo.game.presentation.scenario

/** TacticalUnit: 기존 화면 패키지 참조를 유지하는 시나리오 유닛 상태 별칭이다. */
@Deprecated("도메인 상태 타입을 직접 사용하세요.", ReplaceWith("TacticalUnit", "com.jojo.game.domain.scenario.TacticalUnit"))
typealias TacticalUnit = com.jojo.game.domain.scenario.TacticalUnit

/** ScenarioHead: 기존 화면 패키지 참조를 유지하는 초상화 상태 별칭이다. */
@Deprecated("도메인 상태 타입을 직접 사용하세요.", ReplaceWith("ScenarioHead", "com.jojo.game.domain.scenario.ScenarioHead"))
typealias ScenarioHead = com.jojo.game.domain.scenario.ScenarioHead
