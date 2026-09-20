// Verification
package com.jojo.game.verification

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.jojo.game.application.runtime.BattleRuntimeScreenProbe
import com.jojo.game.domain.scenario.PlaybackState
import com.jojo.game.application.runtime.RuntimeBattleDriver
import com.jojo.game.application.runtime.RuntimeBattleCommand
import com.jojo.game.application.runtime.RuntimeBattleFrame
import com.jojo.game.application.runtime.RuntimeBattlePresentation
import com.jojo.game.application.runtime.RuntimeBattleActionSample
import com.jojo.game.application.runtime.RuntimeBattleRoute

/** VerificationBattleDriver: 외부에서 이름을 지정한 전투 실행을 위한 검증 전용 결정적 입력기이다. */
internal class VerificationBattleDriver(private val state: String?) : RuntimeBattleDriver {
    /** endTurnIssued: 검증 대상의 현재 상태 값을 담는다. */
    private var endTurnIssued = false

    /** 다음 대사 넘김 탭을 넣을 수 있는 가장 이른 시각이다. 한 프레임에 여러 번 누르지 않게 막는다. */
    private var nextTapAt = Float.NEGATIVE_INFINITY

    /** commands: 검증 입력을 처리하고 관련 상태를 갱신한다. */
    override fun commands(frame: RuntimeBattleFrame, probe: BattleRuntimeScreenProbe): List<RuntimeBattleCommand> {
        if (state in PLAYER_CONTROL_STATES) advanceOpeningDialogue(frame, probe)
        if (state == "enemy-turn" && !endTurnIssued && probe.turnPhase == "PLAYER_INPUT" && probe.outcome == null) {
            endTurnIssued = true
            return listOf(RuntimeBattleCommand.EndTurn)
        }
        return emptyList()
    }

    /**
     * 여는 대사 넘기기: 전투 각본의 대사는 플레이어가 눌러야 넘어간다. 아무도 넘기지 않으면
     * 대본이 대사 하나에서 멈춰 전투가 아군 조작 구간에 **영원히 들어가지 않는다**.
     *
     * 위임 진행 화면은 원본에서도 `_ctrlHelper`가 있을 때 — 곧 조작 구간에서만 확인을 받는다.
     * 원본 하네스도 확인 뒤 `TuoGuanLayer`가 뜰 때까지 최대 3초를 기다린다. 포트 쪽은
     * 대사를 넘겨 줄 주체가 없어 그 지점에 닿지 못했고, 그래서 이 픽스처는 확인창이 남은
     * 화면을 위임 배너라고 적고 있었다. `FullBattleTraceDriver`가 쓰는 것과 같은 방식이다.
     */
    private fun advanceOpeningDialogue(frame: RuntimeBattleFrame, probe: BattleRuntimeScreenProbe) {
        if (probe.outcome != null || frame.elapsed < nextTapAt) return
        if (probe.playback != PlaybackState.DIALOGUE && !probe.winConditionsOpen) return
        val (x, y) = if (probe.playback == PlaybackState.DIALOGUE) {
            probe.battleMenuButtonScreenX to probe.battleMenuButtonScreenY + DIALOGUE_TAP_OFFSET_Y
        } else {
            probe.winConditionButtonScreenX to probe.winConditionButtonScreenY
        }
        Gdx.input.inputProcessor?.let {
            it.touchDown(x, y, 0, Input.Buttons.LEFT)
            it.touchUp(x, y, 0, Input.Buttons.LEFT)
        }
        nextTapAt = frame.elapsed + DIALOGUE_INTERVAL_SECONDS
    }

    private companion object {
        /** 아군 조작 구간에 실제로 들어가야 찍을 수 있는 캡처 상태들이다. */
        val PLAYER_CONTROL_STATES = setOf("battle-auto-battle-active-fixture")

        /** 대사 넘김 간격이다. `FullBattleTraceDriver`와 같은 값을 쓴다. */
        const val DIALOGUE_INTERVAL_SECONDS = .35f

        /** 대사 넘김 탭이 전투 메뉴 버튼과 겹치지 않도록 아래로 내리는 화면 오프셋이다. */
        const val DIALOGUE_TAP_OFFSET_Y = 200
    }
}

/** VerificationBattlePresentation: 외부 명명 표현 경로가 운영 화면에 들어가지 않도록 격리한다. */
internal object VerificationBattlePresentation {
    /** from: 검증 입력을 처리하고 관련 상태를 갱신한다. */
    fun from(state: String?): RuntimeBattlePresentation {
        val action = when (state) {
            "attack6-f0" -> RuntimeBattleActionSample(6, 1f / 24f)
            "attack6-f1" -> RuntimeBattleActionSample(6, 7f / 24f)
            "attack6-f2" -> RuntimeBattleActionSample(6, 9f / 24f)
            "attack6-f3" -> RuntimeBattleActionSample(6, 11f / 24f)
            "attack25-f0" -> RuntimeBattleActionSample(25, 1f / 24f)
            "attack25-f1" -> RuntimeBattleActionSample(25, 10f / 24f)
            "attack25-f2" -> RuntimeBattleActionSample(25, 12f / 24f)
            "attack25-f3" -> RuntimeBattleActionSample(25, 14f / 24f)
            "attack48-f0" -> RuntimeBattleActionSample(48, 1f / 24f)
            "attack48-f1" -> RuntimeBattleActionSample(48, 19f / 24f)
            "attack48-f2" -> RuntimeBattleActionSample(48, 21f / 24f)
            "attack48-f3" -> RuntimeBattleActionSample(48, 23f / 24f)
            else -> null
        }
        val route = when {
            state in setOf("yingchuan-reward-basic-route", "yingchuan-reward-card1-route", "yingchuan-reward-card2-route") -> when (state) {
                "yingchuan-reward-card1-route" -> RuntimeBattleRoute.REWARD_CARD1
                "yingchuan-reward-card2-route" -> RuntimeBattleRoute.REWARD_CARD2
                else -> RuntimeBattleRoute.REWARD_BASIC
            }
            state == "yingchuan-item-upgrade-panel-route" -> RuntimeBattleRoute.ITEM_UPGRADE
            state == "yingchuan-lose-restart" -> RuntimeBattleRoute.LOSE_RESTART
            state == "battle-round-final-fixture" -> RuntimeBattleRoute.ROUND_FINAL
            state == "battle-round-enemy-fixture" -> RuntimeBattleRoute.ROUND_ENEMY
            state == "battle-round-normal-fixture" -> RuntimeBattleRoute.ROUND_NORMAL
            state == "battle-win-condition-compact-fixture" -> RuntimeBattleRoute.WIN_COMPACT
            state == "battle-win-condition-full-fixture" -> RuntimeBattleRoute.WIN_FULL
            state == "battle-mini-map-shown-fixture" -> RuntimeBattleRoute.MINI_MAP_SHOWN
            state == "battle-mini-map-hidden-fixture" -> RuntimeBattleRoute.MINI_MAP_HIDDEN
            state == "battle-auto-battle-prompt-off-fixture" -> RuntimeBattleRoute.AUTO_PROMPT_OFF
            state == "battle-auto-battle-prompt-on-fixture" -> RuntimeBattleRoute.AUTO_PROMPT_ON
            state == "battle-auto-battle-active-fixture" -> RuntimeBattleRoute.AUTO_ACTIVE
            state == "battle-command-initial-fixture" -> RuntimeBattleRoute.COMMAND_INITIAL
            state == "battle-command-disabled-fixture" -> RuntimeBattleRoute.COMMAND_DISABLED
            state == "battle-command-cancel-fixture" -> RuntimeBattleRoute.COMMAND_CANCEL
            state == "battle-command-magick-fixture" -> RuntimeBattleRoute.COMMAND_MAGICK
            state == "battle-command-property-fixture" -> RuntimeBattleRoute.COMMAND_PROPERTY
            state?.removeSuffix("-fixture") == "battle-character-hp-camps-partial" -> RuntimeBattleRoute.CHARACTER_HP_CAMPS
            state?.removeSuffix("-fixture") == "battle-character-outline-highlight" -> RuntimeBattleRoute.CHARACTER_OUTLINE
            state?.removeSuffix("-fixture") == "battle-character-hit-impact" -> RuntimeBattleRoute.CHARACTER_HIT
            state?.removeSuffix("-fixture") == "battle-character-cleanup" -> RuntimeBattleRoute.CHARACTER_CLEANUP
            state?.removeSuffix("-fixture") == "battle-character-death-action" -> RuntimeBattleRoute.CHARACTER_DEATH_ACTION
            state?.removeSuffix("-fixture") == "battle-character-death-hidden" -> RuntimeBattleRoute.CHARACTER_DEATH_HIDDEN
            state == "battle-other-unit-info-fixture" -> RuntimeBattleRoute.OTHER_UNIT_INFO
            state == "battle-mine-unit-info-fixture" -> RuntimeBattleRoute.MINE_UNIT_INFO
            state == "yingchuan-attack" -> RuntimeBattleRoute.CUTSCENE_ATTACK
            state == "yingchuan-action4" -> RuntimeBattleRoute.CUTSCENE_POST_HIT
            state == "yingchuan-477" -> RuntimeBattleRoute.CUTSCENE_477
            state == "battle-dialogue-blending-fixture" -> RuntimeBattleRoute.DIALOGUE_BLEND
            state == "battle-init-fixture" -> RuntimeBattleRoute.INITIAL
            state == "battle-terrain-layer-fixture" -> RuntimeBattleRoute.TERRAIN
            // `yingchuan-menu`도 같은 경로였으나 이름만 다른 겹말이라 지웠다.
            state == "battle-menu-fixture" -> RuntimeBattleRoute.MENU
            state == "yingchuan-helper" -> RuntimeBattleRoute.HELPER
            state == "yingchuan-win-condition" -> RuntimeBattleRoute.WIN_MODAL
            state == "yingchuan-unit-info" -> RuntimeBattleRoute.UNIT_INFO
            state == "lose-result" -> RuntimeBattleRoute.RESULT_LOSE
            state == "win-result" -> RuntimeBattleRoute.RESULT_WIN
            state == "yingchuan-opening-say" -> RuntimeBattleRoute.OPENING_SAY
            state == "hud" -> RuntimeBattleRoute.HUD
            state == "yingchuan-dialogue-1" -> RuntimeBattleRoute.DIALOGUE_ONE
            state == "enemy-turn" -> RuntimeBattleRoute.ENEMY_TURN
            state == "map-only" -> RuntimeBattleRoute.MAP_ONLY
            state == "yingchuan-selection" -> RuntimeBattleRoute.SELECTION
            state == "yingchuan-terrain" -> RuntimeBattleRoute.MODAL_TERRAIN
            state == "yingchuan-property" -> RuntimeBattleRoute.MODAL_PROPERTY
            state == "yingchuan-treasure" -> RuntimeBattleRoute.MODAL_TREASURE
            state == "yingchuan-setting" -> RuntimeBattleRoute.MODAL_SETTING
            state == "yingchuan-save" -> RuntimeBattleRoute.MODAL_SAVE
            state == "yingchuan-load" -> RuntimeBattleRoute.MODAL_LOAD
            state == "yingchuan-forces" -> RuntimeBattleRoute.MODAL_FORCES
            state == "battle-jiqi-fixture" -> RuntimeBattleRoute.JIQI
            state == "battle-magick-list-fixture" -> RuntimeBattleRoute.MAGICK_LIST
            state == "battle-magick-detail-fixture" -> RuntimeBattleRoute.MAGICK_DETAIL
            state == "battle-use-property-list-fixture" -> RuntimeBattleRoute.USE_PROPERTY_LIST
            state == "battle-use-property-detail-fixture" -> RuntimeBattleRoute.USE_PROPERTY_DETAIL
            state == "battle-use-property-select-fixture" -> RuntimeBattleRoute.USE_PROPERTY_SELECT
            state == "battle-use-property-cancel-fixture" -> RuntimeBattleRoute.USE_PROPERTY_CANCEL
            state?.startsWith("yingchuan-dialogue-components-") == true -> when (state.removePrefix("yingchuan-dialogue-components-")) {
                "background" -> RuntimeBattleRoute.DIALOGUE_COMPONENT_BACKGROUND
                "characters" -> RuntimeBattleRoute.DIALOGUE_COMPONENT_CHARACTERS
                "labels" -> RuntimeBattleRoute.DIALOGUE_COMPONENT_LABELS
                // 원본 SayLayer 캡처와 같은 누적 단계다. panel -> portrait -> speaker -> text 순으로 켠다.
                "panel" -> RuntimeBattleRoute.DIALOGUE_COMPONENT_PANEL
                "portrait" -> RuntimeBattleRoute.DIALOGUE_COMPONENT_PORTRAIT
                "speaker" -> RuntimeBattleRoute.DIALOGUE_COMPONENT_SPEAKER
                "text" -> RuntimeBattleRoute.DIALOGUE_COMPONENT_TEXT
                else -> RuntimeBattleRoute.DIALOGUE_COMPONENT_DIALOGUE
            }
            state?.startsWith("battle-edit2-") == true -> when (state.removePrefix("battle-edit2-").removeSuffix("-fixture")) {
                "initial" -> RuntimeBattleRoute.EDIT_INITIAL
                "weather" -> RuntimeBattleRoute.EDIT_WEATHER
                "round" -> RuntimeBattleRoute.EDIT_ROUND
                "apply" -> RuntimeBattleRoute.EDIT_APPLY
                "child" -> RuntimeBattleRoute.EDIT_CHILD
                "child-scene" -> RuntimeBattleRoute.EDIT_CHILD_SCENE
                else -> RuntimeBattleRoute.NONE
            }
            state == "battle-register-open" -> RuntimeBattleRoute.EDIT_REGISTER
            else -> RuntimeBattleRoute.NONE
        }
        val dialogueStep = state?.removePrefix("yingchuan-dialogue-")
            ?.takeIf { state.startsWith("yingchuan-dialogue-") }
            ?.toIntOrNull()
        return RuntimeBattlePresentation(route, action, dialogueStep)
    }
}
