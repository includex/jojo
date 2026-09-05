package com.jojo.game

import java.nio.file.Files
import java.nio.file.Path

/** Canonical current-game trace driven by tools/platform_trace_cases.json. */
object PlatformTraceHarness {
    private fun esc(s: String) = s.replace("\\", "\\\\").replace("\"", "\\\"")
    private fun obj(vararg x: Pair<String, String>) = "{" + x.joinToString(",") { "\"${it.first}\":${it.second}" } + "}"
    private fun q(s: String?) = s?.let { "\"${esc(it)}\"" } ?: "null"
    private fun rawCases(s: String) =
        Regex("\\{[^{}]*\\}").findAll(s).map { it.value }.filter { "\"id\"" in it }.toList()

    private fun str(x: String, k: String, d: String = "") =
        (Regex("\"$k\"\\s*:\\s*\"([^\"]*)\"").find(x)?.groupValues?.get(1) ?: d).replace("\\n", "\n")
            .replace("\\\"", "\"").replace("\\\\", "\\")

    private fun num(x: String, k: String, d: Int = 0) =
        Regex("\"$k\"\\s*:\\s*(-?\\d+)").find(x)?.groupValues?.get(1)?.toInt() ?: d

    private fun base(
        kind: String,
        attached: Boolean,
        cmds: String = "[]",
        events: String = "[]",
        native: String = "[]"
    ) = arrayOf(
        "kind" to q(kind),
        "attached" to attached.toString(),
        "layers" to "[]",
        "cmds" to cmds,
        "events" to events,
        "native" to native
    ).toMutableList()

    private fun one(c: String): Pair<String, String> {
        val id = str(c, "id")
        val kind = str(c, "kind")
        val event = str(c, "event")
        val b = base(kind, true)
        when (kind) {
            "privacy" -> {
                var calls = 0
                val native = mutableListOf<String>()
                var cmds = mutableListOf<String>()
                val p = PrivacyConsentFlow(object : NativeBoundary {
                    override fun call(name: String) {
                        native += name
                    }
                }, { calls++ }, { cmds += "END_GAME" })
                val z = event.split(':'); p.touch(z[0].toInt(), z[1].toInt()); b[1] =
                    "attached" to p.attached.toString(); b[3] =
                    "cmds" to "[" + cmds.joinToString(",") { q(it) } + "]"; b[5] =
                    "native" to "[" + native.joinToString(",") { q(it) } + "]"; b += "called" to calls.toString(); b += "privacy" to "null"
            }

            "statement" -> {
                val ev = mutableListOf<String>()
                val cmds = mutableListOf<String>()
                val p = LegalStatementFlow({ ev += it }, { cmds += "END_GAME" }); p.onCreate(num(c, "playerTimer"))
                val z = event.split(':'); p.touch(z[0].toInt(), z[1].toInt()); b[1] =
                    "attached" to p.attached.toString(); b[3] =
                    "cmds" to "[" + cmds.joinToString(",") { q(it) } + "]"; b[4] =
                    "events" to "[" + ev.joinToString(",") { q(it) } + "]"; b += "time" to p.time.toString(); b += "statement" to p.statement.toString(); b += "scheduleSpecs" to "[[\"scheduleOnce\",${p.unlockDelay}],[\"schedule\",${p.countdownInterval},${p.countdownRepeat},${p.countdownDelay}]]"
            }

            "version" -> {
                val p = VersionInfoFlow(str(c, "version")); p.touch(event.substringAfter(':').toInt()); b[1] =
                    "attached" to p.attached.toString(); b += "lines" to p.lines.size.toString(); b += "first" to q(p.lines.first())
            }

            "install" -> {
                val p = InstallationFlow(true, str(c, "launch"), num(c, "mineFloor"))
                val z = event.split(':'); p.touch(z[0].toInt(), z[1].toInt()); b[1] =
                    "attached" to p.attached.toString(); b += "buttons" to p.buttons.joinToString(",", "[", "]")
            }

            "hot" -> {
                val ev = mutableListOf<String>()
                val p = UpdateFlow { ev += it }; if (event == "over") p.over() else {
                    val buttons = p.setButtonFlag(5, true); b += "ini" to p.parseIni(
                        str(
                            c,
                            "ini"
                        )
                    ).entries.joinToString(
                        ",",
                        "{",
                        "}"
                    ) { q(it.key) + ":" + q(it.value) }; b += "diff" to p.olderThanDay(
                        num(c, "left").toLong(),
                        num(c, "right").toLong()
                    ).toString(); b += "buttons" to buttons.joinToString(",", "[", "]")
                }; b[1] = "attached" to p.attached.toString(); b[4] =
                    "events" to "[" + ev.joinToString(",") { q(it) } + "]"
            }

            "login" -> {
                val p = LoginEligibility(
                    num(c, "appId"),
                    num(c, "money"),
                    num(c, "mineFloor")
                ); b += "floor" to p.floor.toString(); b += "check" to p.checkFloor()
                    .toString(); b += "toast" to q(p.toast)
            }

            "sdk" -> {
                val saved = str(c, "saved").ifEmpty { null }
                val macs = Regex("\"macs\"\\s*:\\s*\\[([^]]*)]").find(c)?.groupValues?.get(1)
                    ?.let { Regex("\"([^\"]*)\"").findAll(it).map { m -> m.groupValues[1] }.toList() } ?: emptyList()
                val device = DeviceIdentityService(
                    saved,
                    macs,
                    num(c, "macError")
                ).desktopDeviceId(); if (device != null) b += "device" to q(device); b += "saved" to q(device ?: saved)
            }

            "taptap" -> {
                b += "haveLogin" to TapTapSession().haveLogin().toString()
            }

            "video" -> {
                var called = 0
                val p =
                    VideoRewardFlow(Regex("\"loadError\"\\s*:\\s*true").containsMatchIn(c)) { called++ }; p.onCreate(); p.onEvent(
                    num(c, "event")
                ); b[1] =
                    "attached" to p.attached.toString(); b += "called" to called.toString(); b += "clip" to q(p.clip); b += "plays" to p.plays.toString(); b += "toast" to q(
                    p.toast
                )
            }
        }
        return id to "[" + obj(*b.toTypedArray()) + "]"
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val result = rawCases(Files.readString(Path.of(args[0]))).joinToString(
            ",",
            "{",
            "}"
        ) { val (a, b) = one(it); q(a) + ":" + b }; Files.createDirectories(Path.of(args[1]).parent); Files.writeString(
            Path.of(args[1]),
            result
        ); println(result)
    }
}
