package com.jojo.game.infrastructure.data
import com.jojo.game.domain.battle.BattleTerrainGrid

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.JsonReader

object BattleTerrainLoader {
    fun load(mapIndex: Int): BattleTerrainGrid {
        val file = Gdx.files.internal("maps/hexmaps/$mapIndex.json")
        require(file.exists()) { "원본 Hexzmap이 없습니다: $mapIndex" }
        val root = JsonReader().parse(file)
        val rows = generateSequence(root.get("data")?.child) { it.next }.map { it.asIntArray() }.toList()
        val width = root.getInt("width")
        val height = root.getInt("height")
        require(rows.size == height && rows.all { it.size == width }) { "원본 Hexzmap 크기가 잘못되었습니다: $mapIndex" }
        return BattleTerrainGrid(width, height, rows)
    }
}
