package com.jojo.port.desktop

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.headless.HeadlessApplication
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration
import com.jojo.port.BattleSpriteTimeline
import com.jojo.port.OriginalGameData

/** One-process counterpart to the source Cocos AnimationState frame dump. */
object SpriteMatrixDump {
    @JvmStatic
    fun main(args: Array<String>) {
        val profileIds = args.firstOrNull { it.startsWith("--profiles=") }
            ?.substringAfter('=')
            ?.split(',')
            ?.filter(String::isNotBlank)
            ?.map(String::toInt)
            .orEmpty()
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
                    val data = OriginalGameData.load()
                    profileIds.forEach { id ->
                        val profile = requireNotNull(data.unitProfile(id)) { "missing original profile $id" }
                        println("PORT_UNIT_PROFILE id=$id name=${profile.name} face=${profile.face} head=${profile.face + 8}")
                    }
                    Gdx.app.exit()
                    return
                }
                val timeline = BattleSpriteTimeline.load()
                cases.forEach { (action, direction, ticks) ->
                    ticks.forEachIndexed { index, tick ->
                        val frame = requireNotNull(timeline.frame(action, direction, tick / 24f)) {
                            "missing port frame action=$action direction=$direction tick=$tick"
                        }
                        println("PORT_SPRITE_FRAME action=$action direction=$direction f$index tick=$tick source=${frame.source} x=0 y=${frame.sourceY} width=${frame.sourceWidth} height=${frame.sourceHeight} flipX=${frame.flipX}")
                    }
                }
                Gdx.app.exit()
            }
        }, HeadlessApplicationConfiguration().apply { updatesPerSecond = 60 })
    }
}
