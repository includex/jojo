package com.jojo.game

import java.nio.file.Files
import java.nio.file.Path

/** Direct lifecycle implementation of recovered ui/Welcome.js. */
private class WelcomeFlow {
    val routes = mutableListOf<List<Any>>()

    /**
     * 공개 메서드 `onCreate`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun onCreate() = replaceScene("LOGIN", 1)

    /**
     * 공개 메서드 `onEvent`
     *
     * ### 파라미터
    - `event` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun onEvent(event: Int) {
        if (event == 3 || event == 5) replaceScene("LOGIN", 1)
    }

    private fun replaceScene(name: String, flag: Int) {
        routes += listOf(name, flag)
    }
}

/**
 * object  `WelcomeTraceHarness`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

object WelcomeTraceHarness {
    private fun routes(v: List<List<Any>>) = v.joinToString(",", "[", "]") { "[\"${it[0]}\",${it[1]}]" }
    @JvmStatic
    fun main(args: Array<String>) {
        val text = Files.readString(Path.of(args[0]))
        val cases = Regex("\\{\\\"name\\\":\\\"([^\\\"]+)\\\",\\\"events\\\":\\[([^]]*)]}").findAll(
            text.replace(
                Regex("\\s+"),
                ""
            )
        )
        val json = cases.joinToString(",", "{", "}") { m ->
            val p = WelcomeFlow(); p.onCreate()
            val trace = mutableListOf("{\"step\":\"create\",\"routes\":${routes(p.routes)}}")
            Regex("\\d+").findAll(m.groupValues[2]).forEach { e ->
                p.onEvent(e.value.toInt()); trace += "{\"step\":\"event:${e.value}\",\"routes\":${
                routes(p.routes)
            }}"
            }
            "\"${m.groupValues[1]}\":[${trace.joinToString(",")}]"
        }
        Files.createDirectories(Path.of(args[1]).parent); Files.writeString(Path.of(args[1]), json)
    }
}
