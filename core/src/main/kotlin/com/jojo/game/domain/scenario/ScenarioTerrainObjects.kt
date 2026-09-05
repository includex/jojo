package com.jojo.game.domain.scenario

/** A scripted battlefield object and the terrain tile it overlays. */
data class ScenarioMapObject(
    val x: Int,
    val y: Int,
    val objectId: Int,
    val terrainId: Int,
    val enabled: Boolean,
)

/** A scripted fire overlay on a battlefield tile. */
data class ScenarioFire(
    val x: Int,
    val y: Int,
    val enabled: Boolean,
)
