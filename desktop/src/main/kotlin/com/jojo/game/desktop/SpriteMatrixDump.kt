package com.jojo.game.desktop

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.headless.HeadlessApplication
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration
import com.jojo.game.GameDataCatalog
import com.jojo.game.presentation.battle.unit.BattleSpriteTimeline

/** 전투 스프라이트의 프레임 정보를 출력하는 데스크톱 도구입니다. */
object SpriteMatrixDump {
    @JvmStatic
    /** 프로필 또는 애니메이션 프레임을 읽어 표준 출력으로 내보냅니다. */
    fun main(args: Array<String>) {
        val profileIds = args.firstOrNull { it.startsWith("--profiles=") }
            ?.substringAfter('=')
            ?.split(',')
            ?.filter(String::isNotBlank)
            ?.map(String::toInt)
            .orEmpty()
        /** 지정한 이름의 필수 실행 인자 값을 반환합니다. */
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
