package com.jojo.game.domain.scenario

/** Deterministic seed transition used by authored scenario random draws. */
object ScenarioRandomSequence {
    fun nextFromSeed(seed: Double): Pair<Double, Int> {
        val nextSeed = (9301.0 * seed + 49297.0) % 233280.0
        return nextSeed to ((nextSeed / 233280.0 * 201.0).toInt() % 101)
    }
}
