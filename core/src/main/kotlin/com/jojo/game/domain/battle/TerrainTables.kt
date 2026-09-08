// Battle
package com.jojo.game.domain.battle

/**
 * TerrainTables: 지형 창이 필요로 하는 자료 표를 계층 중립인 값으로 옮긴다.
 *
 * 자료 카탈로그가 화면 객체를 직접 만들면 자원 계층이 표현 계층을 되짚어 들어간다.
 * 카탈로그는 이 두 표만 내놓고, 창을 만드는 일은 표현 계층이 맡는다.
 */

/** 지형 한 줄: 식별자와 이름, 물리·전략 스킬 표시등에 쓰는 두 플래그다. */
data class TerrainRow(val id: Int, val name: String, val flag: Int, val magic: Int)

/** 병과 한 줄: 지형별 상승치와 이동 비용이다. 표에 없는 지형은 담기지 않는다. */
data class TerrainArmRow(
    val id: Int,
    val name: String,
    val terrainRise: Map<Int, Int>,
    val terrainExpend: Map<Int, Int>,
)
