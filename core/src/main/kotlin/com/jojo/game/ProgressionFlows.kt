package com.jojo.game
import com.jojo.game.presentation.shared.overlay.*

/** Renderer-free progression state machines used by the persistence-facing overlays. */
class AchievementFixtureState(private val rewards: Map<Int, List<Any>>) {
    var removed = 0

    /**
     * 공개 메서드 `rows`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun rows() = rewards.entries.map { (id, v) ->
        listOf(
            "${v[0]} B$id",
            "Lv:${v[1]} Gold:${v[2]}",
            (0..2).joinToString("  ") { if ((v[3] as Int and (1 shl it)) != 0) "★" else "☆" })
    }.flatten()

    /**
     * 공개 메서드 `touch`
     *
     * ### 파라미터
    - `button` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `event` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun touch(button: Int, event: Int) {
        if (button == 0 && event == 2) removed++
    }
}

/**
 * class  `DailySignInFlow`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class DailySignInFlow(var count: Int, val signins: MutableList<Int>, private val now: Int) {
    var removed = 0
    val writes = linkedMapOf<String, Any>()
    val layers = mutableListOf<String>()

    /**
     * 공개 메서드 `claim`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun claim() {
        val midnight = now / 86400 * 86400; if (midnight !in signins) {
            signins += midnight; while (signins.size > 7) signins.removeAt(0)
            var n = 0
            var d = midnight; for (i in signins.indices.reversed()) {
                if (signins[i] != d) break; n++; d -= 86400
            }; count += minOf(7, n); writes["SIGNIN_INFO"] = "[${signins.joinToString(",")}]"; writes["SIGNIN_N"] =
                count; layers += "MsgBox"; removed++
        }
    }
}

/**
 * class  `RaffleFlow`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class RaffleFlow(var count: Int, var coins: Int) {
    val writes = linkedMapOf<String, Any>()
    val calls = mutableListOf<List<Any>>()
    val toasts = mutableListOf<String>()
    val layers = mutableListOf<String>()

    /**
     * 공개 메서드 `inc`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun inc() {
        count++; writes["REWARD_VEDIO_COUNT"] = count
    }

    /** _rewardVideo: only the recovered accepted codes consume an ad quota. */
    fun rewardVideo(videoResult: String, confirm: Int = 0) {
        if (count >= 10) {
            layers += "MsgBox"
            if (coins <= 0) {
                toasts += "행운 코인이 부족하여 교환에 실패했습니다~"; return
            }
            if (confirm != 0) {
                toasts += "행운의 코인으로 광고를 보고 교환하는 것을 취소합니다!"; return
            }
            coins--; writes["SIGNIN_N"] = coins
        } else layers += "LoadLayer"
        if (count < 10) layers += "remove:LoadLayer"
        val normalized = videoResult.replace("1", "")
        if (normalized in setOf("0352", "03572", "0572", "0532")) inc()
    }

    /**
     * 공개 메서드 `generatedPool`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Pair<List<Pair<Int,Int>>,List<Int>>`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun generatedPool(): Pair<List<Pair<Int, Int>>, List<Int>> {
        val pool = listOf(0 to 1, 4 to 100, 4 to 50, 4 to 25, 4 to 12, 4 to 6, 4 to 3, 4 to 1)
        val rate = listOf(1, 2, 3) + List(5) { 4 } + List(8) { 5 } + List(10) { 6 } + List(12) { 7 } + List(14) { 8 }
        return pool to rate
    }

    /**
     * 공개 메서드 `rewardGold`
     *
     * ### 파라미터
    - `id` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun rewardGold(id: Int) {
        calls += listOf("gold", id)
    }
}

/**
 * class  `RegistrationFlow`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class RegistrationFlow {
    var removed = 0
    var changed = false
    var display = ""
    val toasts = mutableListOf<String>()
    val files = mutableListOf<List<String>>()
    fun textChanged() {
        changed = true
    }

    fun editingEnded(s: String) {
        if (changed) display = s
    }

    fun touch(button: Int, event: Int) {
        if (event == 2) {
            toasts += "클릭했습니다$button"; if (button == 1) removed++
        }
    }

    /** Validated RegisterLayer write branch: directory, encrypted payload, then native share request. */
    fun writeRegister(path: String, fileName: String, payload: String) {
        files += listOf("mkdir", path.substringBeforeLast('/')); files += listOf(
            "write",
            path,
            payload
        ); files += listOf("helper", "shareFile", path, fileName)
    }
}
