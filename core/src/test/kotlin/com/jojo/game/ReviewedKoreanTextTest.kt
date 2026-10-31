package com.jojo.game

import com.badlogic.gdx.utils.JsonReader
import com.jojo.game.infrastructure.data.ReviewedKoreanText
import com.jojo.game.infrastructure.data.ScenarioDialogueTextCatalog
import kotlin.test.Test
import kotlin.test.assertEquals

class ReviewedKoreanTextTest {
    @Test
    fun `reviewed text reaches nested tables without changing ids or numeric mechanics`() {
        val root = JsonReader().parse("""{"id":37,"effects":[{"name":"策略穿透","value":125}],"unknown":"fixture"}""")
        ReviewedKoreanText.apply(root)
        assertEquals("책략 관통", root.get("effects").get(0).getString("name"))
        assertEquals(125, root.get("effects").get(0).getInt("value"))
        assertEquals(37, root.getInt("id"))
        assertEquals("fixture", root.getString("unknown"))
    }

    @Test
    fun `opening preserves speaker switches and uses reviewed register`() {
        assertEquals("&181\n주군, 서두르셔야 합니다!\n&0\n알고 있다!", ScenarioDialogueTextCatalog.text("R_00:scene1:247"))
        assertEquals("주군, 서두르셔야 합니다!", ScenarioDialogueTextCatalog.text("opening_scene1_page1"))
    }

    @Test
    fun `original table locations distinguish Zhang Ni from Zhang Yi`() {
        val root = JsonReader().parse((0..70).joinToString(prefix = "[", postfix = "]") { "[\"장익\"]" })
        ReviewedKoreanText.apply(root, "unit.bin")
        assertEquals("장억", root.get(68).getString(0))
        assertEquals("장익", root.get(70).getString(0))
    }
}
