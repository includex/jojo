package com.jojo.game.infrastructure.data

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.JsonReader
import com.jojo.engine.pathfinding.PathGrid

/** Loads the game's 100×100 hall terrain resource into an engine grid. */
object HallPathGridLoader {
    fun loadOrNull(sceneIndex: Int): PathGrid? = runCatching {
        val files = Gdx.files ?: return null
        val handle = files.internal("maps/pmaps/$sceneIndex.json")
        if (!handle.exists()) return null
        val root = JsonReader().parse(handle)
        val rows = generateSequence(root.child) { it.next }.map { it.asIntArray() }.toList()
        require(rows.size == 100 && rows.all { it.size == 100 })
        PathGrid(rows)
    }.getOrNull()
}
