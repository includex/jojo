// Verification
package com.jojo.game.verification
import com.jojo.game.presentation.shared.overlay.*

import com.jojo.game.*

import java.nio.file.Files
import java.nio.file.Path


/** SectionLayerTraceHarness: SectionLayer의 표시·선택 상태를 원본 입력과 비교한다. */
object SectionLayerTraceHarness {
    /** main: 검증 실행 흐름을 시작하고 종료 상태를 반환한다. */
    @JvmStatic
    /**
     * `main`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun main(a: Array<String>) {
        val s = Files.readString(Path.of(a[0])).replace(Regex("\\s+"), "")
        val out =
            Regex("""\{"id":"([^"]+)","idx":(\d+),"name":"([^"]*)","setting":(\d+),"events":\[(.*?)]}""").findAll(s)
                .joinToString(",", "{", "}") { m ->
                    var c = 0
                    /** z: 검증 입력을 처리하고 관련 상태를 갱신한다. */
                    val l = SectionLayer(m.groupValues[4].toInt()); fun z(k: String): String {
                    val v =
                        l.view(); return "{\"step\":\"$k\",\"label\":\"${v.label}\",\"count\":${v.count},\"scheduled\":[${
                        v.scheduled.joinToString(
                            ","
                        )
                    }],\"callbacks\":${v.callbacks},\"dead\":${!v.attached}}"
                }; l.onCreate(m.groupValues[2].toInt(), m.groupValues[3]) { c++ }
                    val t = mutableListOf(z("create")); Regex("\"([^\"]+)\"").findAll(m.groupValues[5]).forEach { x ->
                    val e = x.groupValues[1]; if (e.startsWith("touch:")) l.next(
                    e.substringAfter(':').toInt()
                ) else if (e == "auto") l.auto() else l.skip(); t += z(e)
                }; "\"${m.groupValues[1]}\":[${t.joinToString(",")}]"
                }; Files.createDirectories(Path.of(a[1]).parent); Files.writeString(Path.of(a[1]), out)
    }
}
