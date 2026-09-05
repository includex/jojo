package com.jojo.game

import java.nio.file.Files
import java.nio.file.Path

private class ProgressLoadingState {
    var bg = ""
    var label = ""
    var progress = 0.0

    /**
     * 공개 메서드 `onCreate`
     *
     * ### 파라미터
    - `arg` (`String?`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun onCreate(arg: String?) {
        bg = "bg4"; if (arg != null) label = arg
    }

    /**
     * 공개 메서드 `set`
     *
     * ### 파라미터
    - `value` (`Double`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun set(value: Double) {
        progress = value
    }
}

/**
 * object  `Progress2TraceHarness`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

object Progress2TraceHarness {
    private fun state(step: String, p: ProgressLoadingState) =
        "{\"step\":\"$step\",\"bg\":\"${p.bg}\",\"label\":\"${p.label}\",\"spin\":{\"t\":2,\"a\":-360},\"progress\":${p.progress}}"

    @JvmStatic
    fun main(args: Array<String>) {
        val input = Files.readString(Path.of(args[0])).trim()
        val cases =
            Regex("\\{\\\"name\\\":\\\"([^\\\"]+)\\\",\\\"arg\\\":(null|\\\"[^\\\"]*\\\"),\\\"events\\\":\\[([^]]*)]}").findAll(
                input
            )
        val output = cases.joinToString(",", "{", "}") { m ->
            val p = ProgressLoadingState()
            val arg = m.groupValues[2].let { if (it == "null") null else it.removeSurrounding("\"") }
            p.onCreate(arg)
            val trace = mutableListOf(state("create", p))
            Regex("-?\\d+(?:\\.\\d+)?").findAll(m.groupValues[3])
                .forEach { e -> p.set(e.value.toDouble()); trace += state("set:${e.value}", p) }
            "\"${m.groupValues[1]}\":[${trace.joinToString(",")}]"
        }
        Files.createDirectories(Path.of(args[1]).parent)
        Files.writeString(Path.of(args[1]), output)
    }
}
