package com.jojo.game.verification

import com.jojo.game.*

import java.nio.file.Files
import java.nio.file.Path

/** JSON/event adapter for the production progression state machines. */
object ProgressionLayerTraceHarness {
    private fun q(s: String) = "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n") + "\""
    private fun balanced(s: String, start: Int): String {
        var d = 0
        var quote = false
        var esc = false; for (i in start until s.length) {
            val c = s[i]; if (quote) {
                if (esc) esc = false else if (c == '\\') esc = true else if (c == '\"') quote = false
            } else if (c == '\"') quote = true else if (c == '{' || c == '[') d++ else if (c == '}' || c == ']') {
                d--; if (d == 0) return s.substring(start, i + 1)
            }
        }; error("unbalanced fixture")
    }

    private fun cases(raw: String): List<String> {
        val b = raw.indexOf('[', raw.indexOf("\"cases\""))
        val out = mutableListOf<String>()
        var i = b + 1; while (i < raw.length) {
            if (raw[i] == '{') {
                val x = balanced(raw, i); out += x; i += x.length
            } else i++
        }; return out
    }

    private fun field(o: String, n: String): String? =
        Regex("\\\"$n\\\"\\s*:\\s*(\\{(?:[^{}]|\\{[^{}]*})*}|\\[[^]]*]|\\\"(?:\\\\.|[^\"])*\\\"|-?\\d+|true|false)").find(
            o
        )?.groupValues?.get(1)

    private fun str(o: String, n: String, d: String = "") = field(o, n)?.removeSurrounding("\"") ?: d
    private fun int(o: String, n: String, d: Int = 0) = field(o, n)?.toIntOrNull() ?: d
    private fun ints(o: String, n: String) =
        field(o, n)?.let { Regex("-?\\d+").findAll(it).map { m -> m.value.toInt() }.toList() } ?: emptyList()

    private fun events(o: String) =
        field(o, "events")?.let { Regex("\"([^\"]*)\"").findAll(it).map { m -> m.groupValues[1] }.toList() }
            ?: emptyList()

    private fun ss(v: List<String>) = v.joinToString(",", "[", "]", transform = ::q)
    private fun vv(v: List<List<Any>>) = v.joinToString(",", "[", "]") { row ->
        row.joinToString(
            ",",
            "[",
            "]"
        ) { if (it is String) q(it) else it.toString() }
    }

    private fun writes(v: Map<String, Any>) = v.entries.joinToString(
        ",",
        "{",
        "}"
    ) { q(it.key) + ":" + (if (it.value is String) q(it.value as String) else it.value) }

    private fun snap(
        step: String,
        kind: String,
        removed: Int = 0,
        labels: List<String> = emptyList(),
        count: Int? = null,
        coins: Int? = null,
        write: Map<String, Any> = emptyMap(),
        calls: List<List<Any>> = emptyList(),
        toasts: List<String> = emptyList(),
        layers: List<String> = emptyList(),
        done: Int = 0,
        progress: Int? = null,
        changed: Boolean? = null,
        files: List<List<String>> = emptyList(),
        pool: String? = null,
        buttons: List<Boolean> = emptyList()
    ) =
        "{\"step\":${q(step)},\"kind\":${q(kind)},\"removed\":$removed,\"labels\":${ss(labels)},\"count\":${count ?: "null"},\"coins\":${coins ?: "null"},\"writes\":${
            writes(
                write
            )
        },\"calls\":${vv(calls)},\"toasts\":${ss(toasts)},\"layers\":${ss(layers)},\"done\":$done,\"progress\":${progress ?: "null"},\"editChanged\":${changed ?: "null"},\"fileOps\":${
            vv(
                files
            )
        },\"pool\":${pool ?: "null"},\"buttons\":[${buttons.joinToString(",")}] }".replace("] }", "]}")

    private fun achievements(c: String): String {
        val rewards =
            Regex("\"(\\d+)\"\\s*:\\s*\\[\"([^\"]+)\",(\\d+),(\\d+),(\\d+)]").findAll(field(c, "rewards") ?: "{}")
                .associate { m ->
                    m.groupValues[1].toInt() to listOf(
                        m.groupValues[2],
                        m.groupValues[3].toInt(),
                        m.groupValues[4].toInt(),
                        m.groupValues[5].toInt()
                    )
                }
        val layer = AchievementFixtureState(rewards)
        val out = mutableListOf(snap("create", "achievements", labels = layer.rows()))
        events(c).forEach { e ->
            val p = e.split(':'); layer.touch(p[1].toInt(), p[2].toInt()); out += snap(
            e,
            "achievements",
            layer.removed,
            layer.rows()
        )
        }
        return out.joinToString(",", "[", "]")
    }

    private fun signin(c: String): String {
        val layer = DailySignInFlow(int(c, "count"), ints(c, "signins").toMutableList(), int(c, "time"))
        val out = mutableListOf(snap("create", "signin", labels = listOf("행운 코인: ${layer.count}"), count = layer.count))
        events(c).forEach { e ->
            if (e == "claim") layer.claim(); out += snap(
            e,
            "signin",
            layer.removed,
            listOf("행운 코인: ${layer.count}"),
            layer.count,
            write = layer.writes,
            layers = layer.layers
        )
        }
        return out.joinToString(",", "[", "]")
    }

    private fun poolJson(pool: Pair<List<Pair<Int, Int>>, List<Int>>, objects: Boolean = false): String {
        val rows = pool.first.joinToString(
            ",",
            "[",
            "]"
        ) { if (objects) "{\"id\":${it.second},\"type\":${it.first}}" else "[${it.first},${it.second}]" }; return "{\"pool\":$rows,\"rate\":[${
            pool.second.joinToString(
                ","
            )
        }] }".replace("] }", "]}")
    }

    private fun raffle(c: String): String {
        val layer = RaffleFlow(int(c, "count"), int(c, "coins"))
        var pool = field(c, "reward")?.let { "{\"pool\":[[4,${int(it, "id")}]],\"rate\":[1]}" }
            ?: "{\"pool\":[[4,0]],\"rate\":[1]}"


        fun labels(extra: Boolean = false) =
            buildList { add("행운 코인: ${layer.coins}"); if (extra) add("오늘 남은 뽑기 횟수:${layer.count}/30") }

        val out = mutableListOf(
            snap(
                "create",
                "raffle",
                labels = labels(),
                count = layer.count,
                coins = layer.coins,
                pool = pool,
                buttons = listOf(true)
            )
        )
        events(c).forEach { e ->
            when (e.substringBefore(':')) {
                "inc" -> layer.inc(); "reward" -> layer.rewardGold(
                int(
                    field(c, "reward")!!,
                    "id"
                )
            ); "video" -> layer.rewardVideo(str(c, "video"), int(c, "confirm")); "pool" -> {
                val generated = layer.generatedPool(); pool = poolJson(generated); layer.writes["REWARD_POOL"] =
                    poolJson(generated, true); layer.toasts += "상점이 새로고침되었습니다"
            }
            }
            val extra = e in setOf("inc", "reset", "reward") || layer.writes.containsKey("REWARD_VEDIO_COUNT")
            val shown = if (e == "pool") listOf(
                "행운 코인: ${layer.coins}",
                "판자 업그레이드",
                "100돈",
                "50돈",
                "25돈",
                "12돈",
                "6돈",
                "3돈",
                "1돈"
            ) else labels(extra); out += snap(
            e,
            "raffle",
            labels = shown,
            count = layer.count,
            coins = layer.coins,
            write = layer.writes,
            calls = layer.calls,
            toasts = layer.toasts,
            layers = layer.layers,
            pool = pool,
            buttons = listOf(true)
        )
        }
        return out.joinToString(",", "[", "]")
    }

    private fun reset(c: String): String {
        val layer = ResetLayerSourceOracle(); layer.onCreate()
        val out = mutableListOf(
            snap(
                "create",
                "reset",
                layer.removed,
                done = layer.done,
                progress = layer.progress.toInt()
            )
        ); events(c).forEach {
            out += snap(
                it,
                "reset",
                layer.removed,
                done = layer.done,
                progress = layer.progress.toInt()
            )
        }; return out.joinToString(",", "[", "]")
    }

    private fun register(c: String): String {
        val layer = RegistrationFlow()
        val labels = mutableListOf("")
        val out = mutableListOf(
            snap(
                "create",
                "register",
                labels = labels,
                changed = layer.changed
            )
        ); events(c).forEach { e ->
            when (e.substringBefore(':')) {
                "text" -> layer.textChanged(); "end" -> {
                layer.editingEnded(e.substringAfter(':')); labels += layer.display
            }; "button" -> {
                val p = e.split(':'); layer.touch(p[1].toInt(), p[2].toInt())
            }; "build" -> layer.writeRegister(
                "/tmp/register/dev-lbzSha08GeZ1Fu-app.json",
                "dev-lbzSha08GeZ1Fu-app.json",
                "eyJyRmxhZyI6MywibWQ1IjoiZGV2LWxielNoYTA4R2VaMUZ1LWFwcC0zIn0="
            )
            }
            val shown = if (e == "build") listOf("활성화 목록, 가격:4\n" + (0 until 14).joinToString("") {
                "활성화됨 ->" + listOf(
                    "원클릭으로 모든 보물 획득",
                    "벤치, 장비 업그레이드 활성화",
                    "장비 등급 제한을 제거합니다.",
                    "난이도가 한 단계 낮아질 때마다, 최대 턴 수가 3턴 증가합니다.",
                    "적군 체력이 남아도 도망가지 않습니다.",
                    "중독은 사망에 이르지 않습니다.",
                    "게임 내 편집 및 보기 활성화",
                    "게임 속도는 최대 10배까지 높일 수 있습니다.",
                    "스토리 건너뛰기 활성화",
                    "과일은 능력을 높일 수 있습니다",
                    "전투 상태 패널 사용 불가",
                    "프로필 사진 등 미화 기능 활성화",
                    "만렙 시작",
                    "원클릭으로 아이템 만땅"
                )[it] + "/"
            }) else labels; out += snap(
            e,
            "register",
            layer.removed,
            shown,
            toasts = layer.toasts,
            layers = if (e == "build") listOf("MsgBox") else emptyList(),
            changed = layer.changed,
            files = layer.files
        )
        }; return out.joinToString(",", "[", "]")
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val json = cases(Files.readString(Path.of(args[0]))).joinToString(",", "{", "}") { c ->
            q(
                str(
                    c,
                    "name"
                )
            ) + ":" + when (str(c, "kind")) {
                "achievements" -> achievements(c); "signin" -> signin(c); "raffle" -> raffle(c); "reset" -> reset(c); "register" -> register(
                    c
                ); else -> error("unknown progression kind")
            }
        }
        val out = Path.of(args[1]); Files.createDirectories(out.parent); Files.writeString(out, json)
    }
}

/** Isolated source-inventory oracle: ResetLayer has no recovered addLayer caller. */
private class ResetLayerSourceOracle {
    var removed = 0
    var done = 0
    var progress = 0.0


    fun onCreate() {
        progress = 1.0; done++; removed++
    }
}
