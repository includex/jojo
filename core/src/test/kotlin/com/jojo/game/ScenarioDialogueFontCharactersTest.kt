package com.jojo.game

import com.badlogic.gdx.utils.JsonReader
import com.jojo.game.infrastructure.data.ScenarioDialogueTextCatalog
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.assertEquals

class ScenarioDialogueFontCharactersTest {
    @Test
    fun `font repertoire covers every catalog dialogue and alias`() {
        val resource = checkNotNull(javaClass.classLoader.getResourceAsStream("scenarios/dialogue-text.json"))
        val root = resource.bufferedReader(Charsets.UTF_8).use { JsonReader().parse(it.readText()) }
        val glyphs = ScenarioDialogueTextCatalog.fontCharacters.toSet()
        for (section in listOf("entries", "aliases")) {
            var entry = root.get(section).child
            while (entry != null) {
                assertTrue(entry.asString().all { it in glyphs }, "Missing glyphs for ${entry.name}")
                entry = entry.next
            }
        }
        assertEquals(glyphs.size, ScenarioDialogueTextCatalog.fontCharacters.length)
    }

    @Test
    fun `Xu Shao dialogue retains syllables absent from script references`() {
        val text = ScenarioDialogueTextCatalog.text("R_00.scene1.dialogue0011")
        assertTrue(text.contains("그러셨군요. 실은 이 늙은이가 그저 허튼소리를 한 것입니다."))
        assertTrue(text.contains("부디 허물치 말아 주십시오."))
        assertTrue(text.all { it in ScenarioDialogueTextCatalog.fontCharacters })
    }
}
