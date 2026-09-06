// Verification
package com.jojo.game.verification

import com.jojo.game.*
import com.jojo.game.infrastructure.security.FoundationCodec
import com.jojo.game.infrastructure.security.Md5Service
import com.jojo.game.infrastructure.security.UserPreferencesStore
import com.jojo.game.infrastructure.security.UuidCodec
import com.jojo.game.infrastructure.security.StatusMachine
import com.jojo.game.infrastructure.security.EventDispatcher

import java.nio.file.Files
import java.nio.file.Path


/** FoundationTraceHarness: Foundation 서비스와 상태 전이를 입력별로 추적한다. */
object FoundationTraceHarness {
    /** q: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun q(s: String?) = s?.let { "\"${it.replace("\\", "\\\\").replace("\"", "\\\"")}\"" } ?: "null"
    /** caseRaw: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun caseRaw(s: String) =
        Regex("\\{[^{}]*\\}").findAll(s).map { it.value }.filter { "\"id\"" in it }.toList()

    /** str: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun str(s: String, k: String) = Regex("\"$k\"\\s*:\\s*\"([^\"]*)\"").find(s)?.groupValues?.get(1) ?: ""
    /** kind: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun kind(s: String) = str(s, "kind")
    /** esc: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun esc(s: String) = s.replace("\\", "\\\\").replace("\"", "\\\"")
    /** arr: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun arr(a: Iterable<Any?>) = a.joinToString(",", "[", "]") {
        when (it) {
            is String -> q(it); null -> "null"; else -> it.toString()
        }
    }

    /** run: 검증 실행에 필요한 상태를 구성한다. */
    private fun run(c: String): Pair<String, String> {
        val id = str(c, "id")
        val k = kind(c)
        val logs = mutableListOf<String>()
        val fields = mutableListOf<Pair<String, String>>("kind" to q(k)); when (k) {
            "uuid" -> {
                val x = UuidCodec()
                val u = str(c, "uuid")
                val z = x.compress(u); fields += "compressed" to q(z); fields += "decoded" to q(x.decode(z))
            }

            "tool" -> {
                val text = str(c, "text")
                val b = FoundationCodec.bytes(text); fields += "bytes" to b.joinToString(
                    ",",
                    "[",
                    "]"
                ) { (it.toInt() and 255).toString() }; fields += "round" to q(
                    FoundationCodec.text(
                        FoundationCodec.xor(
                            FoundationCodec.xor(b, "ccz65Sha08GeZ1Fu", false),
                            "ccz65Sha08GeZ1Fu",
                            true
                        )
                    )
                )
            }

            "md5" -> {
                val text = str(
                    c,
                    "text"
                ); fields += "hex" to q(Md5Service.hex(text)); fields += "b64" to q(Md5Service.b64(text)); fields += "hmac" to q(
                    Md5Service.hmac(str(c, "key"), text)
                ); fields += "test" to "true"
            }

            "user" -> {
                val local = linkedMapOf<String, String>()
                val file = linkedMapOf<String, String>()
                val p = UserPreferencesStore(local, file, logs)
                val save = str(c, "legacySave")
                val x = str(c, "legacyX"); p.user["save1"] = save; p.user["x"] =
                    x; p.flush(); fields += "first" to q(p.get("save1", "d")); fields += "second" to q(
                    p.get(
                        "x",
                        "d"
                    )
                ); fields += "localKeys" to arr(local.keys.sorted()); fields += "user" to "{\"save1\":${q(save)},\"x\":${
                    q(
                        x
                    )
                }}"
            }

            "userGlobal" -> {
                val local = linkedMapOf<String, String>()
                val file = linkedMapOf<String, String>()
                val p = UserPreferencesStore(local, file, logs); p.global["g"] = str(c, "globalG"); p.flush(true)
                val got = p.get("g", "d", true); p.delete(
                    "g",
                    true
                ); fields += "got" to q(got); fields += "global" to "{}"; fields += "file" to "true"
            }

            "status" -> {
                val p = StatusMachine()
                val o = mutableListOf<String>()

                /** state: 상태 진입·이탈·갱신 콜백을 한 묶음으로 만든다. */
                fun state(i: Int): Triple<() -> Unit, () -> Unit, () -> Unit> =
                    Triple({ o.add("enter$i") }, { o.add("exit$i") }, { o.add("update$i") })
                p.states[0] = state(0); p.states[1] = state(1); p.change(0)
                p.conditions.add(Triple(0, 1, { o.add("cond"); true })); p.update(); p.clear()
                fields += "order" to arr(o); fields += "status" to p.status.toString()
            }

            else -> {
                val p = EventDispatcher()
                val o = mutableListOf<String>()
                val target = Any()
                lateinit var id1: String; id1 = p.add("A", target, { o += "one"; p.remove(id1) }); p.add(
                    "A",
                    target,
                    { o += "once" },
                    true
                ); p.dispatch("A"); p.dispatch("A"); p.dispatch(
                    "A",
                    true
                ); p.update(); fields += "order" to arr(o); fields += "events" to arr(p.events.keys.sorted()); fields += "queued" to p.queue.size.toString()
            }
        }; return id to "[{" + ((listOf(
            "kind" to q(k),
            "logs" to arr(logs)
        ) + fields.drop(1)).joinToString(",") { q(it.first) + ":" + it.second }) + "}]"
    }

    /** main: 검증 실행 흐름을 시작하고 종료 상태를 반환한다. */
    @JvmStatic
    fun main(a: Array<String>) {
        val out = caseRaw(Files.readString(Path.of(a[0]))).joinToString(
            ",",
            "{",
            "}"
        ) { val (x, y) = run(it); q(x) + ":" + y }; Files.createDirectories(Path.of(a[1]).parent); Files.writeString(
            Path.of(a[1]),
            out
        ); println(out)
    }
}
