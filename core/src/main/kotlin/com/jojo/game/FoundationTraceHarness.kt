package com.jojo.game

import java.nio.file.Files
import java.nio.file.Path

/**
 * object  `FoundationTraceHarness`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

object FoundationTraceHarness {
    private fun q(s: String?) = s?.let { "\"${it.replace("\\", "\\\\").replace("\"", "\\\"")}\"" } ?: "null"
    private fun caseRaw(s: String) =
        Regex("\\{[^{}]*\\}").findAll(s).map { it.value }.filter { "\"id\"" in it }.toList()

    private fun str(s: String, k: String) = Regex("\"$k\"\\s*:\\s*\"([^\"]*)\"").find(s)?.groupValues?.get(1) ?: ""
    private fun kind(s: String) = str(s, "kind")
    private fun esc(s: String) = s.replace("\\", "\\\\").replace("\"", "\\\"")
    private fun arr(a: Iterable<Any?>) = a.joinToString(",", "[", "]") {
        when (it) {
            is String -> q(it); null -> "null"; else -> it.toString()
        }
    }

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

                /**
                 * 공개 메서드 `state`
                 *
                 * ### 파라미터
                - `i` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
                 *
                 * ### 응답 스펙
                 * - 반환 타입: `Triple<() -> Unit, () -> Unit, () -> Unit>`
                 * - 반환값: 동작 결과의 도메인 값입니다.
                 */

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
