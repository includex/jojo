// Battle Evidence
package com.jojo.game.presentation.battle.evidence

import com.jojo.game.application.runtime.RuntimeBattleRoute
import com.jojo.game.presentation.battle.overlay.RoundLayer
import com.jojo.game.presentation.shared.evidence.RenderEventLog

/** BattleRenderEventEvidenceCoordinator: 화면의 live-state Port를 기존 렌더 이벤트 증거 입력과 JSONL로 조립한다. */
internal class BattleRenderEventEvidenceCoordinator(
    /** `port` (Port): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val port: Port,
) {
    /** Screen Port: 증거 조립에 필요한 현재 화면 상태를 불변 입력으로 제공한다. */
    internal interface Port {
        /** 현재 전투 유닛의 animation 반영 렌더 입력을 반환한다. */
        fun unitInputs(): List<BattleRenderEventProjectionUnitInput>

        /** 대화 blend 경로의 전장 표식을 반환한다. */
        fun dialogueMarker(): BattleRenderEventProjectionPoint?

        /** 대화 blend 경로의 화상·본문·화자 입력을 반환한다. */
        fun dialogue(): BattleRenderEventProjectionDialogueInput?

        /** 현재 승리 조건 경로의 제목·본문·하위 문구 입력을 반환한다. */
        fun winConditions(route: BattleRenderEventProjectionWinRoute): BattleRenderEventProjectionWinConditionsInput?

        /** 현재 장비 강화 흐름의 표시 입력을 반환한다. */
        fun itemUpgrade(): BattleRenderEventProjectionItemUpgradeInput?

        /** 현재 보상 흐름의 공개 단계 입력을 반환한다. */
        fun reward(): BattleRenderEventProjectionRewardInput?

        /** 현재 라운드 레이어의 불변 view를 반환한다. */
        fun roundView(): RoundLayer.View?

        /** 현재 소비 아이템 레이어의 불변 evidence view를 반환한다. */
        fun usePropertyView(): BattleUsePropertyRenderEventView

        /** 현재 마법 레이어의 불변 evidence view를 반환한다. */
        fun magickView(): BattleMagickRenderEventView

        /** 현재 JiQi 확률 목록을 반환하며, 레이어가 없으면 null을 반환한다. */
        fun jiqiRates(): List<Int>?
    }

    /** RouteState: 화면이 확정한 경로 플래그를 projector 입력으로 바꾸기 전의 불변 상태다. */
    internal data class RouteState(
        /**
         * `battleInit` (Boolean,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val battleInit: Boolean,
        /**
         * `dialogueBlend` (Boolean,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val dialogueBlend: Boolean,
        /**
         * `winConditionRoute` (RuntimeBattleRoute?,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val winConditionRoute: RuntimeBattleRoute?,
        /**
         * `itemUpgrade` (Boolean,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val itemUpgrade: Boolean,
        /**
         * `reward` (Boolean,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val reward: Boolean,
    )

    /** Projection: evidence projector 입력과 화면이 적용할 보드 좌표 정책을 함께 보관한다. */
    internal data class Projection(
        /**
         * `boardLeft` (Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val boardLeft: Float,
        /**
         * `boardBottom` (Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val boardBottom: Float,
        /**
         * `boardTile` (Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val boardTile: Float,
        /**
         * `input` (BattleRenderEventProjectionInput,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val input: BattleRenderEventProjectionInput,
    )

    /** 주 경로의 unit·dialogue·win·reward 입력을 기존 projector 형식으로 조립한다. */
    fun projection(phase: String, state: RouteState): Projection {
        val winRoute = when (state.winConditionRoute) {
            RuntimeBattleRoute.WIN_COMPACT -> BattleRenderEventProjectionWinRoute.COMPACT
            null -> BattleRenderEventProjectionWinRoute.NONE
            else -> BattleRenderEventProjectionWinRoute.FULL
        }
        return Projection(
            boardLeft = -320f,
            boardBottom = if (state.reward || state.battleInit) 1264f else 1728f,
            boardTile = 96f,
            input = BattleRenderEventProjectionInput(
                phase = phase,
                battleInitRoute = state.battleInit,
                dialogueBlendRoute = state.dialogueBlend,
                winConditionRoute = winRoute,
                itemUpgradeRoute = state.itemUpgrade,
                rewardRoute = state.reward,
                units = port.unitInputs(),
                dialogueMarker = port.dialogueMarker(),
                dialogue = port.dialogue(),
                winConditions = port.winConditions(winRoute),
                itemUpgrade = port.itemUpgrade(),
                reward = port.reward(),
            ),
        )
    }

    /** 주 경로 JSONL: 조립한 증거 입력을 기존 기록 형식으로 변환한다. */
    fun jsonl(projection: Projection): String =
        BattleRenderEventRecorder.jsonl(BattleRenderEventProjector.project(projection.input))

    /** 라운드 레이어 입력을 기존 recorder JSONL 형식으로 기록한다. */
    fun roundJsonl(route: RuntimeBattleRoute?): String =
        BattleRoundRenderEventRecorder.jsonl(BattleRoundRenderEventInput(route, port.roundView()))

    /** 소비 아이템 레이어 입력을 기존 recorder JSONL 형식으로 기록한다. */
    fun usePropertyJsonl(): String = BattleUsePropertyRenderEventRecorder.jsonl(port.usePropertyView())

    /** 마법 레이어 입력을 기존 recorder JSONL 형식으로 기록한다. */
    fun magickJsonl(): String = BattleMagickRenderEventRecorder.jsonl(port.magickView())

    /** JiQi 레이어가 없으면 빈 JSONL을, 있으면 기존 recorder JSONL을 기록한다. */
    fun jiqiJsonl(): String = port.jiqiRates()?.let { rates ->
        BattleJiqiRenderEventRecorder.jsonl(BattleJiqiRenderEventView(rates))
    } ?: RenderEventLog().jsonl()
}
