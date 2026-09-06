// Campaign
package com.jojo.game.application.campaign
import com.jojo.game.presentation.shared.overlay.*

/** AchievementFixtureState: 저장 기능과 연결된 진행 화면의 프레임워크 독립 상태를 관리한다. */
class AchievementFixtureState(private val rewards: Map<Int, List<Any>>) {
    /**
     * `removed` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var removed = 0


    /**
     * `rows`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun rows() = rewards.entries.map { (id, v) ->
        listOf(
            "${v[0]} B$id",
            "Lv:${v[1]} Gold:${v[2]}",
            (0..2).joinToString("  ") { if ((v[3] as Int and (1 shl it)) != 0) "★" else "☆" })
    }.flatten()


    /**
     * `touch`: 입력을 규칙에 따라 계산·변환한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun touch(button: Int, event: Int) {
        if (button == 0 && event == 2) removed++
    }
}


/**
 * `DailySignInFlow` 클래스: campaign 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

class DailySignInFlow(var count: Int, val signins: MutableList<Int>, private val now: Int) {
    /**
     * `removed` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var removed = 0
    /**
     * `writes` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val writes = linkedMapOf<String, Any>()
    /**
     * `layers` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val layers = mutableListOf<String>()


    /**
     * `claim`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
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
 * `RaffleFlow` 클래스: campaign 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

class RaffleFlow(var count: Int, var coins: Int) {
    /**
     * `writes` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val writes = linkedMapOf<String, Any>()
    /**
     * `calls` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val calls = mutableListOf<List<Any>>()
    /**
     * `toasts` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val toasts = mutableListOf<String>()
    /**
     * `layers` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val layers = mutableListOf<String>()


    /**
     * `inc`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun inc() {
        count++; writes["REWARD_VEDIO_COUNT"] = count
    }

    /** 보상 동영상의 허용된 결과 코드만 광고 횟수를 차감한다. */
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
     * `generatedPool`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun generatedPool(): Pair<List<Pair<Int, Int>>, List<Int>> {
        val pool = listOf(0 to 1, 4 to 100, 4 to 50, 4 to 25, 4 to 12, 4 to 6, 4 to 3, 4 to 1)
        val rate = listOf(1, 2, 3) + List(5) { 4 } + List(8) { 5 } + List(10) { 6 } + List(12) { 7 } + List(14) { 8 }
        return pool to rate
    }


    /**
     * `rewardGold`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun rewardGold(id: Int) {
        calls += listOf("gold", id)
    }
}


/**
 * `RegistrationFlow` 클래스: campaign 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

class RegistrationFlow {
    /**
     * `removed` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var removed = 0
    /**
     * `changed` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var changed = false
    /**
     * `display` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var display = ""
    /**
     * `toasts` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val toasts = mutableListOf<String>()
    /**
     * `files` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val files = mutableListOf<List<String>>()
    /**
     * `textChanged`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun textChanged() {
        changed = true
    }

    /**
     * `editingEnded`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun editingEnded(s: String) {
        if (changed) display = s
    }

    /**
     * `touch`: 입력을 규칙에 따라 계산·변환한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun touch(button: Int, event: Int) {
        if (event == 2) {
            toasts += "클릭했습니다$button"; if (button == 1) removed++
        }
    }

    /** 등록 화면의 검증된 저장·암호화·공유 요청 흐름을 실행한다. */
    fun writeRegister(path: String, fileName: String, payload: String) {
        files += listOf("mkdir", path.substringBeforeLast('/')); files += listOf(
            "write",
            path,
            payload
        ); files += listOf("helper", "shareFile", path, fileName)
    }
}
