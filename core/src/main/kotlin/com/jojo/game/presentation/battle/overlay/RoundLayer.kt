// Battle
package com.jojo.game.presentation.battle.overlay

/** 현재 턴과 최대 턴 입력을 화면 문구로 변환하고 일정 시간이 지나면 제거를 알린다. */

class RoundLayer(
    /** `remove` (() -> Unit): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val remove: () -> Unit,
    /** `complete` (() -> Unit): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val complete: () -> Unit,
) {

    /**
     * `CreateArgs`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class CreateArgs(
        /**
         * `roundPresent` (Boolean,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val roundPresent: Boolean,
        /**
         * `round` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val round: Int = 0,
        /**
         * `max` (Int?): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val max: Int? = null,
        /**
         * `maxPresent` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val maxPresent: Boolean = max != null,
    )


    /** 턴 문구와 진영 문구의 표시 여부를 렌더링 입력으로 제공한다. */
    data class View(
        /**
         * `roundLabelsVisible` (Boolean,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val roundLabelsVisible: Boolean,
        /**
         * `campLabelsVisible` (Boolean,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val campLabelsVisible: Boolean,
        /**
         * `roundText` (String,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val roundText: String,
    )

    /**
     * `finished` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var finished = false
    /**
     * `view` (View): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var view: View = View(roundLabelsVisible = false, campLabelsVisible = true, roundText = "")
        private set

    /** 현재 턴·최대 턴 입력을 일반 턴 또는 최종 턴 문구로 변환한다. */
    fun onCreate(args: CreateArgs) {
        val isFinal = args.maxPresent && args.round > (args.max ?: 0)
        val text = if (!args.roundPresent) "" else if (isFinal) "최종 턴" else "제${args.round}턴"
        view = View(
            roundLabelsVisible = args.roundPresent,
            campLabelsVisible = !args.roundPresent,
            roundText = text,
        )
    }

    /** nullable 턴 입력을 CreateArgs로 변환해 라운드 표시를 초기화한다. */
    fun onCreate(round: Int?, max: Int?) = onCreate(
        CreateArgs(roundPresent = round != null, round = round ?: 0, max = max),
    )
    /**
     * `elapsed`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun elapsed(seconds: Float) {
        if (!finished && seconds >= DISPLAY_SECONDS) {
            finished = true
            remove()
            complete()
        }
    }

    companion object {
        /**
         * `DISPLAY_SECONDS` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val DISPLAY_SECONDS = 2f
    }
}
