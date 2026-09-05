package com.jojo.game

import com.jojo.game.presentation.scenario.overlay.*

/**
 * State implementation of recovered ui/CmdLayer.js (the internal feature activation
 * panel, not CommandLayer).  It intentionally retains the source's split
 * between selected (unregistered) features and immediately toggled registered
 * features, including ItemStore and registration side effects.
 */
/**
 * class  `CmdLayer`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class CmdLayer(
    val rFlag: Int,
    initialEFlag: Int,
    private val deviceId: String,
    private val unitCount: Int,
    private val inventory: List<Item>,
) {
    /**
     * data class  `Item`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Item(val id: Int, val treasure: Boolean, val property: Boolean)

    /**
     * data class  `Layer`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Layer(val layer: String, val flag: Int?, val txt: String?)

    val names = listOf(
        "원클릭으로 모든 보물 획득", "벤치, 장비 업그레이드 활성화", "업그레이드/전직 시 재계산 활성화", "턴 제한 증가",
        "적군 체력이 남아도 도망가지 않습니다.", "중독되면 죽음; 확장 저장", "편집 기능 활성화", "속도를 10배까지 높일 수 있습니다.",
        "스토리 건너뛰기 활성화", "과일로 오방위 능력치 상승", "전투 상태 패널 사용 불가", "조조 전 원본 아바타와 이미지 사용",
        "만렙 시작", "원클릭으로 모든 아이템"
    )
    private val gold = listOf(10.0, 5.0, 5.0, 2.5, 2.5, 5.0, 20.0, 2.5, 2.5, 10.0, 2.5, 5.0, 5.0, 10.0)
    var eFlag = initialEFlag; private set
    var sFlag = 0; private set
    var label = ""; private set
    val selected = MutableList(14) { false }
    val checked get() = MutableList(14) { eFlag and (1 shl it) != 0 }
    val toasts = mutableListOf<String>()
    val writes = mutableListOf<List<Any>>()
    val props = mutableListOf<List<Int>>()
    val weapons = mutableListOf<List<Int>>()
    val urls = mutableListOf<String>()
    val dispatch = mutableListOf<List<Any>>()
    val layers = mutableListOf<Layer>()
    val events = mutableListOf<String>()
    var restart = 0; private set
    private var prompt: ((Int) -> Unit)? = null

    /**
     * 공개 메서드 `onCreate`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun onCreate() {
        addLayer("MsgBox", 3, "내부 테스트 도구에 대해서는, 도움말 설명을 먼저 확인해 보시는 것을 권장합니다.") { if (it == 0) helper() }
    }

    private fun addLayer(layer: String, flag: Int?, txt: String?, fn: ((Int) -> Unit)? = null) {
        layers += Layer(layer, flag, txt); prompt = fn
    }

    private fun helper() {
        urls += "https://www.google.com"
    }

    /**
     * 공개 메서드 `item`
     *
     * ### 파라미터
    - `index` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `event` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun item(index: Int, event: Int) {
        if (event != 2) return
        val bit = 1 shl index
        if (rFlag and bit != 0) {
            if (activate(bit) != 0) return
            val on = eFlag and bit == 0; eFlag = if (on) eFlag or bit else eFlag and bit.inv()
            toasts += (if (on) "활성화" else "사용 불가") + " " + names[index]
        } else select(index, !selected[index], 1)
    }

    private fun select(index: Int, on: Boolean, source: Int = 0) {
        if (on && source and 1 != 0) { /* recovered config has no click restrictions */
        }
        val bit = 1 shl index
        if (on) {
            if (sFlag and bit != 0) return; sFlag = sFlag or bit
        } else {
            if (sFlag and bit == 0) return; sFlag = sFlag and bit.inv()
        }
        var count = 0
        var total = 0.0
        for (i in names.indices) if (sFlag and (1 shl i) != 0) {
            count++; total += gold[i]
        }
        label = "선택했습니다${count}항, 총${minOf(total, 50.0).format()}원"; selected[index] = on
    }

    private fun Double.format() = if (this % 1.0 == 0.0) toInt().toString() else toString()

    /** Source _ACTIVATE returns a status bitmask; only bit 0 prevents toggle. */
    private fun activate(bit: Int): Int = when (bit) {
        1 -> if (unitCount == 0) {
            toasts += "게임 시작 후에 사용해 주세요~"; 2
        } else {
            for (id in 100..102) if (id == 100 || id == 101) props += listOf(
                id,
                99,
                0
            ); toasts += "아이템이 가득 찼습니다. 배낭에서 확인해 주세요~"; 1
        }

        2 -> if (unitCount == 0) {
            toasts += "게임 시작 후에 사용해 주세요~"; 2
        } else {
            val treasures = inventory.filter { it.treasure }; treasures.forEach {
                if (it.property) props += listOf(
                    it.id,
                    1,
                    0
                ) else weapons += listOf(it.id, 14)
            }; writes += listOf(
                "TREASURE",
                "[${treasures.joinToString(",") { it.id.toString() }}]"
            ); toasts += "모든 보물을 획득했습니다. 보물 도감에서 확인하세요~"; 1
        }

        else -> 0
    }

    /**
     * 공개 메서드 `button`
     *
     * ### 파라미터
    - `index` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `event` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun button(index: Int, event: Int) {
        if (event != 2) return; when (index) {
            0 -> {
                events += "setEFlag:$eFlag"; writes += listOf(
                    "eFlag",
                    eFlag
                ); events += "remove"; events += "setGameSpeed"
            }

            1 -> if (deviceId.isEmpty()) toasts += "장치 코드를 얻지 못하여 활성화 코드 생성 실패!" else if (sFlag != 0) addLayer(
                "MsgBox",
                null,
                "곧 활성화 코드가 생성됩니다. 계속하시겠습니까?"
            ) { ans ->
                if (ans == 0) {
                    var count = 0
                    var money = 0.0; for (i in names.indices) if (sFlag and (1 shl i) != 0) {
                        count++; money += gold[i]
                    }; dispatch += listOf(
                        "COUNT_BASE64",
                        mapOf("money" to money, "count" to count, "sFlag" to sFlag, "eFlag" to eFlag, "rFlag" to rFlag)
                    ); writes += listOf("CHECK_REGISTER", 1)
                }
            } else toasts += "최소한 하나를 선택하여 활성화해야 합니다."

            2 -> helper()
            3 -> addLayer("skmLayer", null, null)
            4 -> names.indices.forEach { if (rFlag and (1 shl it) == 0) select(it, true) }
            5 -> addLayer(
                "MsgBox",
                null,
                "활성화에 성공했는지 확신이 서지 않는다면 이 버튼을 눌러 다시 활성화 여부를 확인할 수 있습니다. 계속하시겠습니까?"
            ) { ans ->
                if (ans == 0) {
                    toasts += "게임을 재시작하여 활성화 여부를 확인하는 중이니 잠시만 기다려 주세요……"; writes += listOf(
                        "CHECK_REGISTER",
                        1
                    ); restart++
                }
            }
        }
    }

    /**
     * 공개 메서드 `answer`
     *
     * ### 파라미터
    - `value` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun answer(value: Int) {
        val fn = prompt; prompt = null; fn?.invoke(value)
    }
}
