// Shared
package com.jojo.game.presentation.shared.overlay

/** MagicUiList: 마법 목록과 마법 화면의 프레임워크 독립 UI 상태를 구현한다. */
class MagicUiList(val mp: Int, val maxMp: Int, magics: List<Magic>, uses: Map<Int, Int>) {

    /**
     * `Magic`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class Magic(
        /**
         * `id` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val id: Int,
        /**
         * `name` (String,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val name: String,
        /**
         * `cost` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val cost: Int,
        /**
         * `power` (Int?,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val power: Int?,
        /**
         * `icon` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val icon: Int,
        /**
         * `hit` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val hit: Int,
        /**
         * `eff` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val eff: Int,
        /**
         * `intro` (String): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val intro: String
    )

    /**
     * `uses` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var uses = uses.toMutableMap(); private set
    /**
     * `rows` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val rows =
        magics.filter { it.cost != 255 }.sortedWith(compareByDescending<Magic> { uses[it.id] ?: 0 }.thenBy { it.id })

    // 원본 목록의 두 번째 진행 막대는 터치 처리나 길게 누르기 완료 때만 갱신되며,
    // 프리팹의 초기값은 0이다.
    var preview = 0f; private set
    /**
     * `attached` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var attached = true; private set
    /**
     * `pending` (Magic?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var pending: Magic? = null
    /**
     * `events` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val events = mutableListOf<String>()


    /**
     * `enabled`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun enabled(index: Int) = rows.getOrNull(index)?.let { mp >= it.cost } == true


    /**
     * `start`: 흐름을 실행하거나 다음 단계로 전달한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun start(index: Int) {
        val magic = rows.getOrNull(index) ?: return; if (mp < magic.cost) return; preview =
            (mp - magic.cost).coerceAtLeast(0).toFloat() / maxMp.coerceAtLeast(1); pending = magic
    }


    /**
     * `end`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun end(index: Int) {
        val magic = rows.getOrNull(index) ?: return; if (pending !== magic) return; attached = false; uses[magic.id] =
            (uses[magic.id] ?: 0) + 1; events += "remove"; events += "selected:${magic.id}"; pending = null
    }


    /**
     * `tick`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun tick(): Magic? {
        val magic = pending ?: return null; pending = null; preview =
            mp.toFloat() / maxMp.coerceAtLeast(1); events += "layer:MagicLayer:${magic.id}"; return magic
    }


    /**
     * `cancel`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun cancel(event: Int) {
        if (event == 2) {
            attached = false; events += "remove"; events += "cancelled"
        }
    }

    companion object {
        /**
         * `TOUCH_END` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val TOUCH_END = 2
    }
}


/**
 * `MagicInfoLayer`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

class MagicInfoLayer(val magic: MagicUiList.Magic) {
    /**
     * `attached` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var attached = true; private set
    /**
     * `assets` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val assets = listOf(
        "asset:Game/Magic/${magic.icon + 1}-1",
        "asset:Game/Hitarea/${magic.hit + 1}-1",
        "asset:Game/Effarea/${magic.eff + 1}-1"
    )


    /**
     * `close`: 상태와 자원을 정리한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun close(event: Int) {
        if (event == 2) attached = false
    }
}
