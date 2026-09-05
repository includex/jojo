package com.jojo.game.verification

import com.jojo.game.*

import java.nio.file.Files
import java.nio.file.Path

private class GiftService {
    var removes = 0
    val routes = mutableListOf<List<Any>>()
    fun onCreate() {
        removes++
    }

    fun event(button: String, event: Int) {
        when (button) {
            "b0" -> if (event != 2) removes++; "b1" -> if (event == 2) routes += listOf(
            "SKM",
            1
        ); "b2" -> if (event == 2) routes += listOf("SKM", 2)
        }
    }
}

/**
 * object  `SendGiftsTraceHarness`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

object SendGiftsTraceHarness {
    private fun state(step: String, p: GiftService): String {
        val r = p.routes.joinToString(
            ",",
            "[",
            "]"
        ) { "[\"${it[0]}\",${it[1]}]" }; return "{\"step\":\"$step\",\"removes\":${p.removes},\"routes\":$r}"
    }

    @JvmStatic
    fun main(a: Array<String>) {
        val s = Files.readString(Path.of(a[0])).replace(Regex("\\s+"), "")
        val cs = Regex("\\{\\\"name\\\":\\\"([^\\\"]+)\\\",\\\"events\\\":\\[([^]]*)]}").findAll(s)
        val out = cs.joinToString(",", "{", "}") { m ->
            val p = GiftService(); p.onCreate()
            val t = mutableListOf(state("create", p)); Regex("\\\"(b[012]):(\\d)\\\"").findAll(m.groupValues[2])
            .forEach { e ->
                p.event(
                    e.groupValues[1],
                    e.groupValues[2].toInt()
                ); t += state(e.groupValues[1] + ":" + e.groupValues[2], p)
            }; "\"${m.groupValues[1]}\":[${t.joinToString(",")}]"
        }; Files.createDirectories(Path.of(a[1]).parent); Files.writeString(Path.of(a[1]), out)
    }
}
