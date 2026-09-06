// Verification
package com.jojo.game.verification

import com.jojo.game.*

import java.nio.file.Files
import java.nio.file.Path

/** WelcomeFlow: 복원된 ui/Welcome.js의 화면 전환 수명 주기를 재현한다. */
private class WelcomeFlow {
    val routes = mutableListOf<List<Any>>()

    /** onCreate: 시작 시 로그인 화면으로 이동한다. */
    fun onCreate() = replaceScene("LOGIN", 1)

    /** onEvent: 입력 이벤트에 따라 로그인 화면을 다시 연다. */
    fun onEvent(event: Int) {
        if (event == 3 || event == 5) replaceScene("LOGIN", 1)
    }

    /** replaceScene: 검증 입력을 처리하고 관련 상태를 갱신한다. */
    private fun replaceScene(name: String, flag: Int) {
        routes += listOf(name, flag)
    }
}


/** WelcomeTraceHarness: Welcome 흐름을 입력 픽스처와 비교하는 실행기이다. */
object WelcomeTraceHarness {
    /** routes: 검증 입력을 처리하고 관련 상태를 갱신한다. */
    private fun routes(v: List<List<Any>>) = v.joinToString(",", "[", "]") { "[\"${it[0]}\",${it[1]}]" }
    /** main: 검증 실행 흐름을 시작하고 종료 상태를 반환한다. */
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
