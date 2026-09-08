// Test
package com.jojo.game

import com.jojo.game.infrastructure.data.GameDataCatalog
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * SkillIntroContractTest: 특기 설명문이 원본 `Model.skillIntro` 형식을 따르는지 검증한다.
 *
 * 이식본은 오랫동안 특기 이름을 쉼표로 이어 붙여 보여 주었다. 원본은 이름 뒤에
 * `defineSkill`의 `format` 열이 정한 값 표기를 붙이고, 무장 정보 화면에서는 특기마다
 * `【…】` 문단과 설명문 한 줄을 만든다.
 */
class SkillIntroContractTest {
    @Test
    fun `a unit without skills reads the source empty text`() {
        val catalog = GameDataCatalog.load()
        // 관직 표에 특기가 붙지 않는 자리를 고르면 원본과 같이 "없음"이다.
        assertEquals("없음", catalog.skillIntro(characterId = 1023, postsId = 59, campaign = null))
    }

    @Test
    fun `a unit with skills gets formatted names, not a comma joined list`() {
        val catalog = GameDataCatalog.load()
        // 특기가 붙는 첫 무장을 자료에서 찾는다. 특정 무장에 매어 두면 표가 바뀔 때 함께 깨진다.
        val (characterId, postsId) = (0..1023).firstNotNullOf { id ->
            val posts = catalog.unitProfile(id)?.posts ?: return@firstNotNullOf null
            (id to posts).takeIf { catalog.skillsForUnit(id, posts, null).isNotEmpty() }
        }

        val inline = catalog.skillIntro(characterId, postsId, null)
        val block = catalog.skillIntro(characterId, postsId, null, mode = 3)

        assertTrue(inline.isNotBlank() && inline != "없음", "unit=$characterId posts=$postsId inline=$inline")
        // 한 줄 형식은 `/`로 잇고, 화면 형식은 특기마다 문단을 만든다.
        assertTrue(!inline.contains(", "), inline)
        assertTrue(block.startsWith("【"), block)
        assertEquals(inline.split("/").size, block.split("【").size - 1, "$inline\n---\n$block")
    }
}
