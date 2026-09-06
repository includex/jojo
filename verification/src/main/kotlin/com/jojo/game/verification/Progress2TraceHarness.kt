// Verification
package com.jojo.game.verification

import com.jojo.game.*

import java.nio.file.Files
import java.nio.file.Path

/** ProgressLoadingState: 검증 실행의 현재 상태를 표현하는 타입이다. */
private class ProgressLoadingState {
    /**
     * `bg` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var bg = ""
    /**
     * `label` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var label = ""
    /**
     * `progress` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var progress = 0.0


    /** onCreate: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    fun onCreate(arg: String?) {
        bg = "bg4"; if (arg != null) label = arg
    }


    /** set: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    fun set(value: Double) {
        progress = value
    }
}


/** Progress2TraceHarness: 검증 실행을 시작하고 추적 결과를 수집하는 타입이다. */
object Progress2TraceHarness {
    /** state: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun state(step: String, p: ProgressLoadingState) =
        "{\"step\":\"$step\",\"bg\":\"${p.bg}\",\"label\":\"${p.label}\",\"spin\":{\"t\":2,\"a\":-360},\"progress\":${p.progress}}"

    /** main: 검증 실행 흐름을 시작하고 종료 상태를 반환한다. */
    @JvmStatic
    /**
     * `main`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

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
