// Test
package com.jojo.game
import com.jojo.game.infrastructure.data.GameDataRepository
import com.jojo.game.infrastructure.data.EncryptedGameDataCodec
import com.jojo.game.infrastructure.data.GameDataCatalog
import com.jojo.game.infrastructure.data.ClasspathThenGdxGameDataResourceSource
import com.jojo.game.infrastructure.data.GameDataResourceSource

import java.io.ByteArrayInputStream
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/** GameDataRepositoryTest: GameDataRepository의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class GameDataRepositoryTest {
    @Test
    fun `packaged source reads classpath before requiring LibGDX files`() {
        val payload = byteArrayOf(1, 2, 3)
        val requested = mutableListOf<String>()
        val classLoader = object : ClassLoader(null) {
            override fun getResourceAsStream(name: String): ByteArrayInputStream? {
                requested += name
                return ByteArrayInputStream(payload)
            }
        }

        val result = ClasspathThenGdxGameDataResourceSource(classLoader).read("unit.bin")

        assertContentEquals(payload, result)
        assertEquals(listOf("maps/data/unit.bin"), requested)
    }

    @Test
    fun `encoded tables are assembled into a queryable catalog`() {
        val source = FakeSource(validTables())

        val catalog = GameDataCatalog.load(source)

        assertEquals("테스트 유닛", catalog.unitProfile(0)?.name)
        assertEquals(7, catalog.unitProfile(0)?.face)
        assertEquals("첫 전투", catalog.battleName(0))
        assertEquals(validTables().keys, source.readNames.toSet())
    }

    @Test
    fun `damaged encrypted table is rejected with its table name`() {
        val tables = validTables().toMutableMap()
        tables["unit.bin"] = tables.getValue("unit.bin").copyOf().also { bytes ->
            bytes[bytes.lastIndex] = (bytes.last() + 1).toByte()
        }

        val failure = assertFailsWith<IllegalArgumentException> {
            GameDataRepository(FakeSource(tables)).load()
        }

        assertContains(failure.message.orEmpty(), "unit 테이블 검증 실패")
    }

    @Test
    fun `array table with object root is rejected as a format error`() {
        val tables = validTables().toMutableMap()
        tables["unit.bin"] = EncryptedGameDataCodec.encode("{}")

        val failure = assertFailsWith<IllegalArgumentException> {
            GameDataRepository(FakeSource(tables)).load()
        }

        assertContains(failure.message.orEmpty(), "unit 테이블 형식이 배열이 아닙니다")
    }

    @Test
    fun `malformed decoded JSON is reported as a JSON format error`() {
        val tables = validTables().toMutableMap()
        tables["unit.bin"] = EncryptedGameDataCodec.encode("[")

        val failure = assertFailsWith<IllegalArgumentException> {
            GameDataRepository(FakeSource(tables)).load()
        }

        assertContains(failure.message.orEmpty(), "unit 테이블 JSON 형식이 잘못되었습니다")
    }

    private class FakeSource(private val tables: Map<String, ByteArray>) : GameDataResourceSource {
        val readNames = mutableListOf<String>()

        override fun read(fileName: String): ByteArray {
            readNames += fileName
            return requireNotNull(tables[fileName]) { "테스트 테이블이 없습니다: $fileName" }
        }
    }

    private fun validTables(): Map<String, ByteArray> = mapOf(
        "unit.bin" to encoded("""[{"0":"테스트 유닛","1":7}]"""),
        "arms.bin" to encoded("[{}]"),
        "posts.bin" to encoded("[{}]"),
        "hitarea.bin" to encoded("""[{"ps":[[0,1]]}]"""),
        "effarea.bin" to encoded("""[{"ps":[]}]"""),
        "magic.bin" to encoded("[]"),
        "item.bin" to encoded("[]"),
        "itemSkills.bin" to encoded("{}"),
        "unitPostsSkill.bin" to encoded("[]"),
        "defineSkill.bin" to encoded("[]"),
        "shop.bin" to encoded("""[{"0":" 첫 전투 "}]"""),
        "config.bin" to encoded("{}"),
        "gameConfig.bin" to encoded("{}"),
    )

    private fun encoded(json: String): ByteArray = EncryptedGameDataCodec.encode(json)
}
