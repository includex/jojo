// Game
package com.jojo.game.presentation.shared.overlay


/** CmdLayer: 복구된 ui/CmdLayer.js의 기능 활성화 패널 상태를 관리한다. 미등록 기능 선택과 등록 기능 즉시 전환, ItemStore 및 등록 부수효과를 원본 규칙대로 유지한다. */

class CmdLayer(
    val rFlag: Int,
    initialEFlag: Int,
    /** `deviceId` (String): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val deviceId: String,
    /** `unitCount` (Int): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val unitCount: Int,
    /** `inventory` (List<Item>): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val inventory: List<Item>,
) {

    /**
     * `Item`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class Item(val id: Int, val treasure: Boolean, val property: Boolean)


    /**
     * `Layer`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class Layer(val layer: String, val flag: Int?, val txt: String?)

    /**
     * `names` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val names = listOf(
        "원클릭으로 모든 보물 획득", "벤치, 장비 업그레이드 활성화", "업그레이드/전직 시 재계산 활성화", "턴 제한 증가",
        "적군 체력이 남아도 도망가지 않습니다.", "중독되면 죽음; 확장 저장", "편집 기능 활성화", "속도를 10배까지 높일 수 있습니다.",
        "스토리 건너뛰기 활성화", "과일로 오방위 능력치 상승", "전투 상태 패널 사용 불가", "조조 전 원본 아바타와 이미지 사용",
        "만렙 시작", "원클릭으로 모든 아이템"
    )
    /**
     * `gold` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val gold = listOf(10.0, 5.0, 5.0, 2.5, 2.5, 5.0, 20.0, 2.5, 2.5, 10.0, 2.5, 5.0, 5.0, 10.0)

    /**
     * `intros` (상태 값): 각 항목의 상세 정보 문구를 보관한다.
     *
     * 원본 `CmdLayer.getConfig()`는 항목마다 `name`·`gold`·`intro` 세 값을 준다.
     * 이식본은 앞의 둘만 옮겨 와서 상세 정보 단추가 보여 줄 문구가 통째로 없었다.
     */

    val intros = listOf(
        "게임에서 클릭하면 받지 못한 다른 보물들을 채울 수 있으며, 같은 보물은 2개까지 지원합니다.",
        "전투 종료 시 인물 및 장비 레벨을 평균 레벨로 자동 상승",
        "활성화 시 업그레이드/전직마다 재계산, 출전 시 자동 배치 및 원클릭 장비 세팅",
        "활성화 시 전투에 진입하면 턴 상한이 4턴 증가합니다",
        "적군은 무작정 돌진만 합니다.",
        "유닛이 중독되면 사망합니다. 저장 슬롯을 100개로 확장했습니다.",
        "게임 테스트용으로만 사용됩니다",
        "게임 속도 상한 조정",
        "스토리를 건너뛸 수 있습니다.",
        "공훈 모드에서 과일을 먹으면 오위가 상승합니다.",
        "전투 중 체력과 마나, 경험치 변화판이 더 이상 표시되지 않습니다.",
        "프로필 사진, 스토리, 전투 이미지는 구버전 것을 사용하고, 향수를 느끼고 싶으면 사용하세요.",
        "게임을 처음부터 다시 시작하며, 캐릭터 레벨은 바로 만렙입니다.",
        "원클릭으로 모든 아이템을 99개로 채우기",
    )

    /**
     * `eFlag` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var eFlag = initialEFlag; private set
    /**
     * `sFlag` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var sFlag = 0; private set
    /**
     * `label` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var label = ""; private set

    /**
     * `modal` (Layer?): 지금 화면에 떠 있는 안내창이다.
     *
     * `layers`는 붙였던 이력을 모두 쌓아 두므로 지금 무엇이 떠 있는지 알 수 없다.
     * 답을 고르면 사라지고 새로 붙이면 바뀌는 현재 상태를 따로 들고 있어야
     * 화면을 그리는 쪽이 안내창을 언제 그릴지 판단할 수 있다.
     */

    var modal: Layer? = null; private set
    /**
     * `selected` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val selected = MutableList(14) { false }
    /**
     * `checked` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val checked get() = MutableList(14) { eFlag and (1 shl it) != 0 }
    /**
     * `toasts` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val toasts = mutableListOf<String>()
    /**
     * `writes` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val writes = mutableListOf<List<Any>>()
    /**
     * `props` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val props = mutableListOf<List<Int>>()
    /**
     * `weapons` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val weapons = mutableListOf<List<Int>>()
    /**
     * `urls` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val urls = mutableListOf<String>()
    /**
     * `dispatch` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val dispatch = mutableListOf<List<Any>>()
    /**
     * `layers` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val layers = mutableListOf<Layer>()
    /**
     * `events` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val events = mutableListOf<String>()
    /**
     * `restart` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var restart = 0; private set
    /**
     * `prompt` (((Int) -> Unit)?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var prompt: ((Int) -> Unit)? = null


    /**
     * `onCreate`: 객체나 결과를 생성한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun onCreate() {
        addLayer("MsgBox", 3, "내부 테스트 도구에 대해서는, 도움말 설명을 먼저 확인해 보시는 것을 권장합니다.") { if (it == 0) helper() }
    }

    /**
     * `addLayer`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun addLayer(layer: String, flag: Int?, txt: String?, fn: ((Int) -> Unit)? = null) {
        layers += Layer(layer, flag, txt); prompt = fn; modal = Layer(layer, flag, txt)
    }

    /**
     * `helper`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun helper() {
        urls += "https://www.google.com"
    }


    /**
     * `item`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
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

    /**
     * `select`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    /**
     * 상세 정보 단추: 항목 설명을 한 단추 MsgBox로 띄운다.
     *
     * 원본은 `flag: 1`짜리 MsgBox에 그 항목의 `intro`를 넣는다. 이식본에는 이 경로가
     * 아예 없어서, 해당 화면의 검증이 원본 로그를 저장해 두었다가 되읽고 있었다.
     */
    fun itemInfo(index: Int, event: Int) {
        if (event != 2) return
        addLayer("MsgBox", 1, intros[index])
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

    /**
     * `Double`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun Double.format() = if (this % 1.0 == 0.0) toInt().toString() else toString()

    /** 활성화 결과의 첫 비트가 설정 전환 가능 여부를 나타낸다. */
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
     * `button`: 입력을 규칙에 따라 계산·변환한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
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
     * `answer`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun answer(value: Int) {
        val fn = prompt; prompt = null; modal = null; fn?.invoke(value)
    }
}
