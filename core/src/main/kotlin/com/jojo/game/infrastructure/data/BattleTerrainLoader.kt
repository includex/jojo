// Infrastructure
package com.jojo.game.infrastructure.data
import com.jojo.game.domain.battle.BattleTerrainGrid

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.JsonReader

/** 원본 육각형 맵 JSON을 전투 지형 모델로 변환한다. */
object BattleTerrainLoader {
    /** 맵 인덱스에 해당하는 지형을 읽고 크기 일관성을 검증한다. */
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
