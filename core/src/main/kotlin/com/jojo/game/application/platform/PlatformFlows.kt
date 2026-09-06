// Platform
package com.jojo.game.application.platform

/** NativeBoundary: 복구된 플랫폼 팩토리의 데스크톱 흐름이다. Android와 JSB 작업은 [NativeBoundary] 호출로 표현하며 실제 동작은 여기서 흉내 내지 않는다. */

interface NativeBoundary {
    /**
     * `call`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun call(name: String)
}


/**
 * `PrivacyConsentFlow` 클래스: platform 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

class PrivacyConsentFlow(
    /**
     * `native` (NativeBoundary,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val native: NativeBoundary,
    /**
     * `accepted` (() -> Unit,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val accepted: () -> Unit,
    /**
     * `end` (() -> Unit): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val end: () -> Unit
) {
    /**
     * `attached` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var attached = true


    /**
     * `touch`: 입력을 규칙에 따라 계산·변환한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun touch(button: Int, event: Int) {
        if (event != 2) return; when (button) {
            0 -> {
                native.call("write:/w//Privacy.txt:Hello World!"); accepted(); attached = false
            }; 1 -> end()
        }
    }
}


/**
 * `LegalStatementFlow` 클래스: platform 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

class LegalStatementFlow(private val emit: (String) -> Unit, private val end: () -> Unit) {
    /**
     * `attached` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var attached = true
    /**
     * `statement` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var statement = 0
    /**
     * `time` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var time = 8
    /**
     * `countdownInterval` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var countdownInterval = 1
    /**
     * `countdownRepeat` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var countdownRepeat = 0
    /**
     * `countdownDelay` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var countdownDelay = 0
    /**
     * `unlockDelay` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var unlockDelay = 0


    /**
     * `onCreate`: 필요한 객체나 결과를 생성한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun onCreate(playerTimer: Int) {
        // JS는 Math.floor를 두 번 사용한다. Kotlin의 /는 0 방향으로 버리므로
        // 모든 정수 입력에서 원본 동작을 보존하려면 floorDiv를 사용한다.
        val minutes = Math.floorDiv(playerTimer, 60_000)
        time = maxOf(3, time - Math.floorDiv(minutes, 30))
        // 원본의 반복 예약과 잠금 해제 예약 등록을 재현한다. 콜백 시점은 엔진
        // 스케줄러 경계에 맡기고 등록 순서만 결정적으로 유지한다.
        countdownRepeat = time
        unlockDelay = time + 1
    }


    /**
     * `touch`: 입력을 규칙에 따라 계산·변환한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun touch(button: Int, event: Int) {
        if (event != 2) return; if (button == 0) {
            statement = 1; emit("ENTER_GAME")
        } else end(); attached = false
    }
}


/**
 * `VersionInfoFlow` 클래스: platform 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

class VersionInfoFlow(private val version: String) {
    /**
     * `attached` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var attached = true
    /**
     * `lines` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val lines = (if (version.isEmpty()) "버그 수정</br>코드 최적화" else version).split("</br>")
    /**
     * `touch`: 입력을 규칙에 따라 계산·변환한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun touch(event: Int) {
        if (event == 2) attached = false
    }
}


/**
 * `InstallationFlow` 클래스: platform 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

class InstallationFlow(private val desktop: Boolean, private val launch: String, mineFloor: Int) {
    /**
     * `buttons` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val buttons = MutableList(5) { true }

    init {
        if (mineFloor < 1) buttons[4] = false; if (launch.isEmpty()) buttons[2] = false
    }

    /**
     * `attached` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var attached = true
    /**
     * `touch`: 입력을 규칙에 따라 계산·변환한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun touch(button: Int, event: Int) {
        if (event == 2 && button == 0) attached = false
    }
}


/**
 * `UpdateFlow` 클래스: platform 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

class UpdateFlow(private val emit: (String) -> Unit) {
    /**
     * `attached` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var attached = true
    /**
     * `flags` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private var flags = 0
    /**
     * `setButtonFlag`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun setButtonFlag(mask: Int, on: Boolean): List<Boolean> {
        flags = if (on) flags or mask else flags and mask.inv(); return (0..3).map { flags and (1 shl it) != 0 }
    }

    /**
     * `parseIni`: 입력을 규칙에 따라 계산·변환한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun parseIni(text: String) = text.lineSequence().mapNotNull { line ->
        line.indexOf('=').takeIf { it >= 0 }?.let { i -> line.substring(0, i).trim() to line.substring(i + 1).trim() }
    }.toMap()

    /**
     * `olderThanDay`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun olderThanDay(a: Long, b: Long) = kotlin.math.abs(a - b) > 86400000
    /**
     * `over`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun over() {
        emit("UPDATE_SUCC"); attached = false
    }
}


/**
 * `LoginEligibility` 클래스: platform 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

class LoginEligibility(private val appId: Int, private val money: Int, private val mineFloor: Int) {
    /**
     * `floors` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val floors = mapOf(
        8026 to 0,
        8031 to 0,
        8023 to 0,
        8011 to 0,
        8030 to 0,
        8021 to 1,
        8013 to 1,
        8014 to 1,
        8024 to 1,
        8001 to 1,
        8012 to 2,
        8003 to 2,
        8007 to 2,
        8027 to 2,
        8008 to 2,
        8032 to 3,
        8034 to 3,
        8033 to 3,
        8028 to 3,
        8029 to 3,
        8009 to 4,
        8016 to 4,
        8020 to 4
    )
    /**
     * `floor` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val floor = floors[appId] ?: 0
    /**
     * `toast` (String?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var toast: String? = null
    /**
     * `checkFloor`: 조건과 입력 상태를 검증한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun checkFloor(): Int = if (money < 20 && mineFloor < floor) {
        toast = "권한이 부족합니다!"; 1
    } else 0
}


/**
 * `DeviceIdentityService` 클래스: platform 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

class DeviceIdentityService(private val saved: String?, private val macs: List<String>, private val macError: Int) {

    /**
     * `desktopDeviceId`: 입력을 규칙에 따라 계산·변환한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun desktopDeviceId(): String? {
        if (saved != null) return saved; if (macError != 0) return null
        val joined = macs.filter { !it.startsWith("0-50-56-") }.distinct()
            .joinToString(""); return java.security.MessageDigest.getInstance("MD5").digest(joined.toByteArray())
            .joinToString("") { "%02x".format(it) }
    }
}


/**
 * `TapTapSession` 클래스: platform 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

class TapTapSession {
    /**
     * `haveLogin`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun haveLogin() = true
}


/**
 * `VideoRewardFlow` 클래스: platform 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

class VideoRewardFlow(private val loadError: Boolean, private val done: () -> Unit) {
    /**
     * `attached` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var attached = true
    /**
     * `clip` (String?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var clip: String? = null
    /**
     * `plays` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var plays = 0
    /**
     * `toast` (String?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var toast: String? = null
    /**
     * `onCreate`: 필요한 객체나 결과를 생성한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun onCreate() {
        if (loadError) {
            toast = "죄송합니다. 비디오 로드에 실패했습니다!"; done(); attached = false
        } else {
            clip = "logo-clip"; plays++
        }
    }

    /**
     * `onEvent`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun onEvent(event: Int) {
        if (event == 3 || event == 5) {
            done(); attached = false
        }
    }
}
