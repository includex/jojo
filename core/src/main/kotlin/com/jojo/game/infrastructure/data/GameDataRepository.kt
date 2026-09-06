// Game
package com.jojo.game.infrastructure.data

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.JsonReader
import com.badlogic.gdx.utils.JsonValue

/** GameDataResourceSource: 패키지에 포함된 게임 데이터 테이블을 파일 이름으로 읽는 바이트 원본 계약이다. */
internal fun interface GameDataResourceSource {

    fun read(fileName: String): ByteArray
}

/** ClasspathThenGdxGameDataResourceSource: 헤드리스 도구에서는 JVM 리소스를 우선 읽고, 없으면 LibGDX 내부 자원을 읽는다. */
internal class ClasspathThenGdxGameDataResourceSource(
    private val classLoader: ClassLoader = GameDataCatalog::class.java.classLoader,
) : GameDataResourceSource {
    override fun read(fileName: String): ByteArray = classLoader
        .getResourceAsStream("$DATA_DIRECTORY/$fileName")
        ?.use { it.readBytes() }
        ?: Gdx.files.internal("$DATA_DIRECTORY/$fileName").readBytes()

    private companion object {
        const val DATA_DIRECTORY = "maps/data"
    }
}

/** 해석과 검증을 마친 테이블 묶음이다. */
internal data class GameDataTableBundle(
    val units: List<JsonValue>,
    val arms: List<JsonValue>,
    val posts: List<JsonValue>,
    val hitAreas: List<JsonValue>,
    val effectAreas: List<JsonValue>,
    val magics: List<JsonValue>,
    val items: List<JsonValue>,
    val itemSkills: JsonValue,
    val unitPostSkills: List<JsonValue>,
    val defineSkills: List<JsonValue>,
    val shops: List<JsonValue>,
    val config: JsonValue,
    val gameConfig: JsonValue,
)

/** 모든 카탈로그 테이블을 읽고 복호화·해석·검증한다. */
internal class GameDataRepository(
    private val source: GameDataResourceSource,
    private val jsonReader: JsonReader = JsonReader(),
) {

    fun load(): GameDataTableBundle = GameDataTableBundle(
        units = arrayTable("unit.bin", "unit"),
        arms = arrayTable("arms.bin", "arms"),
        posts = arrayTable("posts.bin", "posts"),
        hitAreas = arrayTable("hitarea.bin", "hitarea"),
        effectAreas = arrayTable("effarea.bin", "effarea"),
        magics = arrayTable("magic.bin", "magic"),
        items = arrayTable("item.bin", "item"),
        itemSkills = objectTable("itemSkills.bin", "itemSkills"),
        unitPostSkills = arrayTable("unitPostsSkill.bin", "unitPostsSkill"),
        defineSkills = arrayTable("defineSkill.bin", "defineSkill"),
        shops = arrayTable("shop.bin", "shop"),
        config = objectTable("config.bin", "config"),
        gameConfig = objectTable("gameConfig.bin", "gameConfig"),
    )

    private fun arrayTable(fileName: String, tableName: String): List<JsonValue> {
        val root = decodedRoot(fileName, tableName)
        require(root.isArray) { "$tableName 테이블 형식이 배열이 아닙니다." }
        return generateSequence(root.child) { it.next }.toList()
    }

    private fun objectTable(fileName: String, tableName: String): JsonValue {
        val root = decodedRoot(fileName, tableName)
        require(root.isObject) { "$tableName 테이블 형식이 객체가 아닙니다." }
        return root
    }

    private fun decodedRoot(fileName: String, tableName: String): JsonValue {
        val decoded = requireNotNull(EncryptedGameDataCodec.decode(source.read(fileName))) {
            "$tableName 테이블 검증 실패"
        }
        return runCatching { jsonReader.parse(decoded) }.getOrElse { cause ->
            throw IllegalArgumentException("$tableName 테이블 JSON 형식이 잘못되었습니다.", cause)
        }
    }
}
