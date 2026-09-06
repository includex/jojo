// Verification
package com.jojo.game.verification

import com.jojo.game.*
import com.jojo.game.presentation.shared.ServiceFlow
import com.jojo.game.presentation.shared.ServiceMenuState

import java.nio.file.Files
import java.nio.file.Path


/** FrameworkServiceTraceHarness: 프레임워크 서비스와 스킬 모델의 호출 결과를 추적한다. */
object FrameworkServiceTraceHarness {
    /** q: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun q(s: String) = "\"$s\""
    /** cases: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun cases(s: String) = Regex("\\{[^{}]*\\}").findAll(s).map { it.value }
        .filter { it.contains("\"kind\":\"service\"") || it.contains("\"kind\":\"skm\"") }.toList()

    /** str: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun str(s: String, k: String) = Regex("\"$k\"\\s*:\\s*\"([^\"]*)\"").find(s)?.groupValues?.get(1) ?: ""
    /** num: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun num(s: String, k: String) = Regex("\"$k\"\\s*:\\s*(\\d+)").find(s)?.groupValues?.get(1)?.toInt() ?: 0
    /** main: 검증 실행 흐름을 시작하고 종료 상태를 반환한다. */
    @JvmStatic
    /**
     * `main`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun main(a: Array<String>) {
        val out = cases(Files.readString(Path.of(a[0]))).joinToString(",", "{", "}") { c ->
            val id = str(c, "id"); if (str(
                c,
                "kind"
            ) == "service"
        ) {
            var calls = 0
            val routes = mutableListOf<String>()
            val p = ServiceFlow({ routes += "skm" }, { calls++ })
            val z = str(c, "event").split(':'); p.touch(
                z[0].toInt(),
                z[1].toInt()
            ); q(id) + ":[{\"kind\":\"service\",\"attached\":${p.attached},\"layers\":${if (routes.isEmpty()) "[]" else "[[\"skm\",4]]"},\"calls\":$calls}]"
        } else {
            val p = ServiceMenuState(num(c, "flag")); p.touch(
                num(
                    c,
                    "event"
                )
            ); q(id) + ":[{\"kind\":\"skm\",\"attached\":${p.attached},\"visible\":${
                p.visible.joinToString(
                    ",",
                    "[",
                    "]"
                )
            }}]"
        }
        }; Files.createDirectories(Path.of(a[1]).parent); Files.writeString(Path.of(a[1]), out)
    }
}
