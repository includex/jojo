package com.jojo.game.verification

import com.jojo.game.*

import java.nio.file.Files
import java.nio.file.Path

/** 프레임워크 서비스와 스킬 모델의 호출 결과를 추적한다. */

object FrameworkServiceTraceHarness {
    private fun q(s: String) = "\"$s\""
    private fun cases(s: String) = Regex("\\{[^{}]*\\}").findAll(s).map { it.value }
        .filter { it.contains("\"kind\":\"service\"") || it.contains("\"kind\":\"skm\"") }.toList()

    private fun str(s: String, k: String) = Regex("\"$k\"\\s*:\\s*\"([^\"]*)\"").find(s)?.groupValues?.get(1) ?: ""
    private fun num(s: String, k: String) = Regex("\"$k\"\\s*:\\s*(\\d+)").find(s)?.groupValues?.get(1)?.toInt() ?: 0
    @JvmStatic
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
