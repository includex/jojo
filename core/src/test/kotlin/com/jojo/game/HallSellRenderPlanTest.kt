package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals

class HallSellRenderPlanTest {
    @Test fun `sell draw plan golden preserves card ordering tab labels and notice layering`() {
        val commands = HallSellRenderPlan.commands(
            HallSellView(
                rows = listOf(
                    HallSellRowView("검", 7, "Lv: 2", "Exp: 0", "150"),
                    HallSellRowView("약초", 9, "인벤토리: 4", null, "20"),
                ),
                money = 987,
                notice = "검 판매",
            ),
        )

        assertEquals(
            listOf(
                "TILED|maps/ui/start-battle/logo9.png|",
                "PATCH|maps/ui/start-battle/box1.png|",
                "PATCH|maps/ui/start-battle/title.png|",
                "TEXT||판매하기",
                "TEXT||창고 목록",
                "PATCH|maps/ui/start-battle/box1.png|",
                "PATCH|maps/ui/start-battle/box1.png|",
                "PATCH|maps/ui/start-battle/box1.png|",
                "SPRITE|maps/item-icons/7.png|",
                "TEXT||검",
                "TEXT||Lv: 2",
                "TEXT||Exp: 0",
                "TEXT||판매가: 150",
                "PATCH|maps/ui/start-battle/box1.png|",
                "PATCH|maps/ui/start-battle/box1.png|",
                "SPRITE|maps/item-icons/9.png|",
                "TEXT||약초",
                "TEXT||인벤토리: 4",
                "TEXT||판매가: 20",
                "TEXT||현금",
                "TEXT||987",
                "PATCH|maps/ui/start-battle/button.png|",
                "TEXT||무기점",
                "PATCH|maps/ui/start-battle/button.png|",
                "TEXT||상점",
                "PATCH|maps/ui/start-battle/button.png|",
                "TEXT||종료",
                "TEXT||검 판매",
            ),
            commands.map { "${it.kind}|${it.asset}|${it.text}" },
        )
        assertEquals(276.84f, commands[6].x)
        assertEquals(267.84f + 9f + 360f, commands[13].x)
        assertEquals(HallSellTextColor.NOTICE, commands.last().color)
        assertEquals(9, commands[21].inset)
    }
}
