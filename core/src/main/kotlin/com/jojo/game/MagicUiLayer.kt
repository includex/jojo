package com.jojo.game

/** Headless games of recovered MagickListLayer and MagicLayer UI contracts. */
class MagicUiList(val mp: Int, val maxMp: Int, magics: List<Magic>, uses: Map<Int, Int>) {
    /**
     * data class  `Magic`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Magic(
        val id: Int,
        val name: String,
        val cost: Int,
        val power: Int?,
        val icon: Int,
        val hit: Int,
        val eff: Int,
        val intro: String
    )

    var uses = uses.toMutableMap(); private set
    val rows =
        magics.filter { it.cost != 255 }.sortedWith(compareByDescending<Magic> { uses[it.id] ?: 0 }.thenBy { it.id })

    // The source list's second progress bar is only refreshed by touch
    // handling/long-press completion; its prefab starts at zero.
    var preview = 0f; private set
    var attached = true; private set
    private var pending: Magic? = null
    val events = mutableListOf<String>()

    /**
     * 공개 메서드 `enabled`
     *
     * ### 파라미터
    - `index` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun enabled(index: Int) = rows.getOrNull(index)?.let { mp >= it.cost } == true

    /**
     * 공개 메서드 `start`
     *
     * ### 파라미터
    - `index` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun start(index: Int) {
        val magic = rows.getOrNull(index) ?: return; if (mp < magic.cost) return; preview =
            (mp - magic.cost).coerceAtLeast(0).toFloat() / maxMp.coerceAtLeast(1); pending = magic
    }

    /**
     * 공개 메서드 `end`
     *
     * ### 파라미터
    - `index` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun end(index: Int) {
        val magic = rows.getOrNull(index) ?: return; if (pending !== magic) return; attached = false; uses[magic.id] =
            (uses[magic.id] ?: 0) + 1; events += "remove"; events += "selected:${magic.id}"; pending = null
    }

    /**
     * 공개 메서드 `tick`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Magic?`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun tick(): Magic? {
        val magic = pending ?: return null; pending = null; preview =
            mp.toFloat() / maxMp.coerceAtLeast(1); events += "layer:MagicLayer:${magic.id}"; return magic
    }

    /**
     * 공개 메서드 `cancel`
     *
     * ### 파라미터
    - `event` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun cancel(event: Int) {
        if (event == 2) {
            attached = false; events += "remove"; events += "cancelled"
        }
    }

    companion object {
        const val TOUCH_END = 2
    }
}

/**
 * class  `MagicInfoLayer`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class MagicInfoLayer(val magic: MagicUiList.Magic) {
    var attached = true; private set
    val assets = listOf(
        "asset:Game/Magic/${magic.icon + 1}-1",
        "asset:Game/Hitarea/${magic.hit + 1}-1",
        "asset:Game/Effarea/${magic.eff + 1}-1"
    )

    /**
     * 공개 메서드 `close`
     *
     * ### 파라미터
    - `event` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun close(event: Int) {
        if (event == 2) attached = false
    }
}
