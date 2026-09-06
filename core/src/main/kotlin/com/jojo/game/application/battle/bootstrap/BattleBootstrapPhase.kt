// Battle
package com.jojo.game.application.battle.bootstrap
/**
 * `BattleBootstrapPhase` 클래스: bootstrap 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

internal enum class BattleBootstrapPhase {
    SCENE0,
    INITIAL_SCENE1,
    COMPLETE,
}
