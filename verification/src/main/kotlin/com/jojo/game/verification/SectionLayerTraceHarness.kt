package com.jojo.game.verification
import com.jojo.game.presentation.shared.overlay.*

import com.jojo.game.*

import java.nio.file.Files
import java.nio.file.Path

/** SectionLayer의 표시·선택 상태를 원본 입력과 비교한다. */

object SectionLayerTraceHarness {
    @JvmStatic
    fun main(a: Array<String>) {
        val s = Files.readString(Path.of(a[0])).replace(Regex("\\s+"), "")
        val out =
            Regex("""\{"id":"([^"]+)","idx":(\d+),"name":"([^"]*)","setting":(\d+),"events":\[(.*?)]}""").findAll(s)
                .joinToString(",", "{", "}") { m ->
                    var c = 0
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
