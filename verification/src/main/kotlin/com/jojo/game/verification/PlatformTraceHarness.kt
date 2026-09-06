// Verification
package com.jojo.game.verification

import com.jojo.game.*
import com.jojo.game.application.platform.DeviceIdentityService
import com.jojo.game.application.platform.InstallationFlow
import com.jojo.game.application.platform.LegalStatementFlow
import com.jojo.game.application.platform.LoginEligibility
import com.jojo.game.application.platform.NativeBoundary
import com.jojo.game.application.platform.PrivacyConsentFlow
import com.jojo.game.application.platform.TapTapSession
import com.jojo.game.application.platform.UpdateFlow
import com.jojo.game.application.platform.VersionInfoFlow
import com.jojo.game.application.platform.VideoRewardFlow

import java.nio.file.Files
import java.nio.file.Path

/** PlatformTraceHarness: tools/platform_trace_cases.json을 기준으로 현재 게임 추적을 실행한다. */
object PlatformTraceHarness {
    /** esc: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun esc(s: String) = s.replace("\\", "\\\\").replace("\"", "\\\"")
    /** obj: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun obj(vararg x: Pair<String, String>) = "{" + x.joinToString(",") { "\"${it.first}\":${it.second}" } + "}"
    /** q: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun q(s: String?) = s?.let { "\"${esc(it)}\"" } ?: "null"
    /** rawCases: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun rawCases(s: String) =
        Regex("\\{[^{}]*\\}").findAll(s).map { it.value }.filter { "\"id\"" in it }.toList()

    /** str: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun str(x: String, k: String, d: String = "") =
        (Regex("\"$k\"\\s*:\\s*\"([^\"]*)\"").find(x)?.groupValues?.get(1) ?: d).replace("\\n", "\n")
            .replace("\\\"", "\"").replace("\\\\", "\\")

    /** num: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun num(x: String, k: String, d: Int = 0) =
        Regex("\"$k\"\\s*:\\s*(-?\\d+)").find(x)?.groupValues?.get(1)?.toInt() ?: d

    /** base: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
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

    /** one: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
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
                    /** call: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
                    override fun call(name: String) {
                        native += name
                    }
                }, { calls++ }, { cmds += "END_GAME" })
                /**
                 * `z` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val z = event.split(':'); p.touch(z[0].toInt(), z[1].toInt()); b[1] =
                    "attached" to p.attached.toString(); b[3] =
                    "cmds" to "[" + cmds.joinToString(",") { q(it) } + "]"; b[5] =
                    "native" to "[" + native.joinToString(",") { q(it) } + "]"; b += "called" to calls.toString(); b += "privacy" to "null"
            }

            "statement" -> {
                /**
                 * `ev` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val ev = mutableListOf<String>()
                /**
                 * `cmds` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val cmds = mutableListOf<String>()
                /**
                 * `p` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val p = LegalStatementFlow({ ev += it }, { cmds += "END_GAME" }); p.onCreate(num(c, "playerTimer"))
                /**
                 * `z` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val z = event.split(':'); p.touch(z[0].toInt(), z[1].toInt()); b[1] =
                    "attached" to p.attached.toString(); b[3] =
                    "cmds" to "[" + cmds.joinToString(",") { q(it) } + "]"; b[4] =
                    "events" to "[" + ev.joinToString(",") { q(it) } + "]"; b += "time" to p.time.toString(); b += "statement" to p.statement.toString(); b += "scheduleSpecs" to "[[\"scheduleOnce\",${p.unlockDelay}],[\"schedule\",${p.countdownInterval},${p.countdownRepeat},${p.countdownDelay}]]"
            }

            "version" -> {
                /**
                 * `p` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val p = VersionInfoFlow(str(c, "version")); p.touch(event.substringAfter(':').toInt()); b[1] =
                    "attached" to p.attached.toString(); b += "lines" to p.lines.size.toString(); b += "first" to q(p.lines.first())
            }

            "install" -> {
                /**
                 * `p` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val p = InstallationFlow(true, str(c, "launch"), num(c, "mineFloor"))
                /**
                 * `z` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val z = event.split(':'); p.touch(z[0].toInt(), z[1].toInt()); b[1] =
                    "attached" to p.attached.toString(); b += "buttons" to p.buttons.joinToString(",", "[", "]")
            }

            "hot" -> {
                /**
                 * `ev` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val ev = mutableListOf<String>()
                /**
                 * `p` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val p = UpdateFlow { ev += it }; if (event == "over") p.over() else {
                    /**
                     * `buttons` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                     */

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
                /**
                 * `p` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val p = LoginEligibility(
                    num(c, "appId"),
                    num(c, "money"),
                    num(c, "mineFloor")
                ); b += "floor" to p.floor.toString(); b += "check" to p.checkFloor()
                    .toString(); b += "toast" to q(p.toast)
            }

            "sdk" -> {
                /**
                 * `saved` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val saved = str(c, "saved").ifEmpty { null }
                /**
                 * `macs` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val macs = Regex("\"macs\"\\s*:\\s*\\[([^]]*)]").find(c)?.groupValues?.get(1)
                    ?.let { Regex("\"([^\"]*)\"").findAll(it).map { m -> m.groupValues[1] }.toList() } ?: emptyList()
                /**
                 * `device` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

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
                /**
                 * `called` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                var called = 0
                /**
                 * `p` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

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

    /** main: 검증 실행 흐름을 시작하고 종료 상태를 반환한다. */
    @JvmStatic
    /**
     * `main`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

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
