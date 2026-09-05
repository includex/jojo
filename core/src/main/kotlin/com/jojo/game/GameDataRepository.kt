package com.jojo.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.JsonReader
import com.badlogic.gdx.utils.JsonValue

/** Byte-oriented source for the game's packaged data tables. */
internal fun interface GameDataResourceSource {
    fun read(fileName: String): ByteArray
}

/**
 * Resolves JVM resources first so headless tools work without a LibGDX
 * application, then falls back to LibGDX's internal asset source.
 */
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

/** Parsed, validated tables handed to the read-only catalog as one value. */
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

/** Loads, decodes, parses, validates, and assembles all catalog tables. */
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
