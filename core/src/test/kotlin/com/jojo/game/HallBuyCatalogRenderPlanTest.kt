package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals

class HallBuyCatalogRenderPlanTest {
    @Test fun `equipment catalog golden preserves source row labels and draw order`() {
        val commands = HallBuyCatalogRenderPlan.commands(
            HallBuyCatalogView(
                propertyTab = false,
                rows = listOf(HallBuyCatalogRowView("검", 7, "무기", 2, 3, "100")),
            ),
        )

        assertEquals(
            listOf(
                "PATCH|maps/ui/start-battle/box1.png|",
                "TEXT||상품 목록",
                "PATCH|maps/ui/start-battle/button.png|",
                "TEXT||무기점",
                "PATCH|maps/ui/start-battle/button.png|",
                "TEXT||상점",
                "PATCH|maps/ui/start-battle/box1.png|",
                "PATCH|maps/ui/start-battle/box1.png|",
                "SPRITE|maps/item-icons/7.png|",
                "TEXT||검",
                "TEXT||레벨:",
                "TEXT||1",
                "TEXT||속성:",
                "TEXT||무기",
                "TEXT||인벤토리:",
                "TEXT||2",
                "TEXT||총합:",
                "TEXT||3",
                "TEXT||가격:",
                "TEXT||100",
            ),
            commands.map { "${it.kind}|${it.asset}|${it.text}" },
        )
        assertEquals(370.8f, commands[6].y)
        assertEquals(9, commands[2].inset)
    }

    @Test fun `property catalog uses compact card labels without equipment fields`() {
        val commands = HallBuyCatalogRenderPlan.commands(
            HallBuyCatalogView(
                propertyTab = true,
                rows = listOf(HallBuyCatalogRowView("약초", 9, "아이템", 4, 4, "20")),
            ),
        )

        assertEquals(
            listOf("약초", "인벤토리: 4", "가격: 20"),
            commands.filter { it.kind == HallBuyCatalogDrawKind.TEXT }.takeLast(3).map { it.text },
        )
        assertEquals(456f, commands.first { it.text == "약초" }.y - 73f)
    }
}
