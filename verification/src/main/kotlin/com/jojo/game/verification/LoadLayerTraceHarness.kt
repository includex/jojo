// Verification
package com.jojo.game.verification
import com.jojo.game.presentation.shared.overlay.*

import com.jojo.game.*


/** LoadLayerTraceHarness: 검증 실행을 시작하고 추적 결과를 수집하는 타입이다. */
object LoadLayerTraceHarness {
    /** main: 검증 실행 흐름을 시작하고 종료 상태를 반환한다. */
    @JvmStatic
    /**
     * `main`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun main(a: Array<String>) {
        val s = java.nio.file.Files.readString(java.nio.file.Path.of(a[0]))
        Regex("\\{\\\"id\\\":\\\"([^\\\"]+)\\\",\\\"text\\\":(null|\\\"([^\\\"]*)\\\")} ").findAll(s).toList()
        val rows = Regex("\\{\\\"id\\\":\\\"([^\\\"]+)\\\",\\\"text\\\":(null|\\\"([^\\\"]*)\\\")}").findAll(s)
            .map { m ->
                val v =
                    LoadLayer().onCreate(if (m.groupValues[2] == "null") null else m.groupValues[3]); "{\"id\":\"${m.groupValues[1]}\",\"view\":{\"labelActive\":${v.labelActive},\"label\":\"${v.label}\",\"anime\":\"${v.anime}\"}}"
            }.joinToString(
            ",",
            "[",
            "]"
        ); java.nio.file.Files.createDirectories(java.nio.file.Path.of(a[1]).parent); java.nio.file.Files.writeString(
            java.nio.file.Path.of(a[1]),
            rows
        )
    }
}
