package com.jojo.game.desktop

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.headless.HeadlessApplication
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration
import com.jojo.game.BattleSpriteTimeline
import com.jojo.game.GameDataCatalog

/** One-process counterpart to the source Cocos AnimationState frame dump. */
object SpriteMatrixDump {
    @JvmStatic
/**
 * 공개 메서드 `main`
 *
 * ### 파라미터
- `args` (`Array<String>`): 구현 기준으로 역할 및 허용 값 정의 필요
 *
 * ### 응답 스펙
 * - 반환 타입: `Unit`
 * - 반환값: 동작 결과의 도메인 값입니다.
 */

    fun main(args: Array<String>) {
        val profileIds = args.firstOrNull { it.startsWith("--profiles=") }
            ?.substringAfter('=')
            ?.split(',')
            ?.filter(String::isNotBlank)
            ?.map(String::toInt)
            .orEmpty()
/**
 * 공개 메서드 `value`
 *
 * ### 파라미터
- `name` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
 *
 * ### 응답 스펙
 * - 반환 타입: `String`
 * - 반환값: 동작 결과의 도메인 값입니다.
 */

        fun value(name: String): String = args.firstOrNull { it.startsWith("--$name=") }
            ?.substringAfter('=') ?: error("--$name is required")
        val cases = args.firstOrNull { it.startsWith("--cases=") }
            ?.substringAfter('=')
            ?.split(';')
            ?.filter(String::isNotBlank)
            ?.map { encoded ->
                val (action, direction, ticks) = encoded.split(':', limit = 3)
                Triple(action.toInt(), direction.toInt(), ticks.split(',').filter(String::isNotBlank).map(String::toInt))
            }
            ?: listOf(Triple(
                value("action").toInt(),
                value("direction").toInt(),
                value("ticks").split(',').filter(String::isNotBlank).map(String::toInt),
            ))
        HeadlessApplication(object : ApplicationAdapter() {
            override fun create() {
                if (profileIds.isNotEmpty()) {
                    val data = GameDataCatalog.load()
                    profileIds.forEach { id ->
                        val profile = requireNotNull(data.unitProfile(id)) { "missing game-data profile $id" }
                        println("GAME_UNIT_PROFILE id=$id name=${profile.name} face=${profile.face} head=${profile.face + 8}")
                    }
                    Gdx.app.exit()
                    return
                }
                val timeline = BattleSpriteTimeline.load()
                cases.forEach { (action, direction, ticks) ->
                    ticks.forEachIndexed { index, tick ->
                        val frame = requireNotNull(timeline.frame(action, direction, tick / 24f)) {
                            "missing game frame action=$action direction=$direction tick=$tick"
                        }
                        println("GAME_SPRITE_FRAME action=$action direction=$direction f$index tick=$tick source=${frame.source} x=0 y=${frame.sourceY} width=${frame.sourceWidth} height=${frame.sourceHeight} flipX=${frame.flipX}")
                    }
                }
                Gdx.app.exit()
            }
        }, HeadlessApplicationConfiguration().apply { updatesPerSecond = 60 })
    }
}
