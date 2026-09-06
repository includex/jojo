// Verification
package com.jojo.game.verification

import com.jojo.game.*

import java.nio.file.Files
import java.nio.file.Path

/** GiftService: 검증 시나리오의 상태와 동작을 제공하는 타입이다. */
private class GiftService {
    /**
     * `removes` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var removes = 0
    /**
     * `routes` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val routes = mutableListOf<List<Any>>()
    /** onCreate: 런타임 이벤트를 받아 검증 산출물을 갱신한다. */
    fun onCreate() {
        removes++
    }

    /** event: 검증 입력을 처리하고 관련 상태를 갱신한다. */
    fun event(button: String, event: Int) {
        when (button) {
            "b0" -> if (event != 2) removes++; "b1" -> if (event == 2) routes += listOf(
            "SKM",
            1
        ); "b2" -> if (event == 2) routes += listOf("SKM", 2)
        }
    }
}


/** SendGiftsTraceHarness: 선물 전송 서비스의 생성·입력·라우팅 결과를 추적한다. */
object SendGiftsTraceHarness {
    /** state: 검증 입력을 처리하고 관련 상태를 갱신한다. */
    private fun state(step: String, p: GiftService): String {
        val r = p.routes.joinToString(
            ",",
            "[",
            "]"
        ) { "[\"${it[0]}\",${it[1]}]" }; return "{\"step\":\"$step\",\"removes\":${p.removes},\"routes\":$r}"
    }

    /** main: 검증 실행 흐름을 시작하고 종료 상태를 반환한다. */
    @JvmStatic
    /**
     * `main`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

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
