package com.jojo.game.application.battle.bootstrap

/**
 * Lifecycle of the authored opening sequence before tactical player input is
 * available. Kept independent of BattleScreen so both the presentation layer
 * and read-only campaign projections share the same domain state.
 */
internal enum class BattleBootstrapPhase {
    SCENE0,
    INITIAL_SCENE1,
    COMPLETE,
}
