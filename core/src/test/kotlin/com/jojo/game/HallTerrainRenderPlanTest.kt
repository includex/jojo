package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals

class HallTerrainRenderPlanTest {
    @Test fun `terrain snapshot copies source cells and preserves source display names`() {
        val skills = mutableListOf(true, false, true, false)
        val values = mutableListOf(TerrainLayer.Value(1, "ignored", "★"))
        val view = HallTerrainView.from(
            TerrainLayer.Tab.RISE,
            listOf(TerrainLayer.Cell(99, "ignored", 7, skills, values)),
        )
        skills[0] = false
        values[0] = TerrainLayer.Value(1, "ignored", "--")

        assertEquals(true, view.riseTab)
        assertEquals("평원", view.rows.single().name)
        assertEquals(7, view.rows.single().iconIndex)
        assertEquals(listOf(true, false, true, false), view.rows.single().enabledSkills)
        assertEquals(listOf("★"), view.rows.single().values)
    }

    @Test fun `terrain draw plan golden preserves panel draw ordering and source assets`() {
        val view = HallTerrainView(
            riseTab = false,
            rows = listOf(HallTerrainRowView("평원", 7, listOf(true, false, true, false), listOf("★", "◎", "○"))),
        )
        val commands = HallTerrainRenderPlan.commands(view)

        val expected = buildList {
            addAll(listOf(
                "TILED|maps/ui/start-battle/logo9.png|",
                "PATCH|maps/ui/start-battle/box1.png|",
                "PATCH|maps/ui/start-battle/title.png|",
                "TEXT||지형 정보 일람",
                "PATCH|maps/ui/start-battle/box2.png|",
                "TEXT||이름",
            ))
            listOf("마왕", "보병", "기병", "궁기", "포차", "무술", "군주", "보병", "기병", "궁기", "포차", "무술", "무술")
                .forEach { header ->
                    add("PATCH|maps/ui/start-battle/box2.png|")
                    add("TEXT||$header")
                }
            addAll(listOf(
                "PATCH|maps/ui/terrain-layer/row-even.png|",
                "SPRITE|maps/terrain-icons/7.png|",
                "TEXT||평원",
                "SPRITE|maps/ui/terrain-layer/skill1.png|",
                "SPRITE|maps/ui/terrain-layer/skill2-disabled.png|",
                "SPRITE|maps/ui/terrain-layer/skill3.png|",
                "SPRITE|maps/ui/terrain-layer/skill4-disabled.png|",
                "TEXT||★",
                "TEXT||◎",
                "TEXT||○",
                "PATCH|maps/ui/start-battle/button.png|",
                "TEXT||지형 효과",
                "PATCH|maps/ui/start-battle/button.png|",
                "TEXT||기동력 소모",
                "PATCH|maps/ui/start-battle/button.png|",
                "TEXT||확인",
            ))
        }
        assertEquals(expected, commands.map { "${it.kind}|${it.asset}|${it.text}" })
        assertEquals(235.84f, commands.first().x)
        assertEquals(594f, commands[3].y)
        assertEquals(HallTerrainTextColor.ORANGE, commands.first { it.text == "★" }.color)
        assertEquals(HallTerrainTextColor.GREEN, commands.first { it.text == "○" }.color)
    }
}
