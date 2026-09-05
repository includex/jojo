package com.jojo.game.verification.fixture

import com.jojo.game.*
import com.jojo.game.application.battle.LearnUnitSkillRoute
import com.jojo.game.application.navigation.RaffleGateRoute
import com.jojo.game.domain.scenario.ScenarioJoinBattleLimit
import com.jojo.game.presentation.battle.edit.*
import com.jojo.game.presentation.hall.RaffleGateRouteScreen
import com.jojo.game.presentation.overlay.fixture.SystemOverlayFixtureScreen
import com.jojo.game.presentation.title.LoginOptionalOverlayRoute
import com.jojo.game.presentation.title.TitleScreen

/** Verification-only startup catalogue for isolated capture surfaces. */
class CaptureFixtureStartupExtension : RuntimeStartupExtension {
    override fun route(request: RuntimeStartupRequest): Boolean {
        val state = request.state
        fun show(screen: com.badlogic.gdx.Screen): Boolean { request.showScreen(screen); return true }
        when (state) {
            "hall-achievements-fixture" -> return show(AchievementsFixtureScreen(request.game))
            "hall-attribute-fixture" -> return show(AttributeFixtureScreen(request.game))
            "hall-generic-list-fixture" -> return show(GenericListFixtureScreen(request.game))
        }
        when (state?.removeSuffix("-fixture")) {
            "login-modal-load" -> return show(ModalLoadRouteScreen(request.game))
            "raffle-gated" -> return show(RaffleGateRouteScreen(request.game))
        }
        LoginOptionalOverlayRoute.parse(state)?.let { return show(TitleScreen(request.game, initialSettingOpen = true, optionalOverlayRoute = it)) }
        CmdRoute.parse(state)?.let { return show(CmdRouteScreen(request.game, it)) }
        TerminalSceneRoute.parse(state)?.let { return show(TerminalSceneRouteScreen(request.game, it)) }
        LearnUnitSkillRoute.parse(state)?.let { return show(LearnUnitSkillRouteScreen(request.game, it)) }
        DefineUnitRoute.parse(state)?.let { return show(DefineUnitRouteScreen(request.game, it)) }
        BattleUnitEditRoute.parse(state)?.let { return show(BattleUnitEditRouteScreen(request.game, it)) }
        EditRosterRoute.parse(state)?.let { return show(EditRosterRouteScreen(request.game, it)) }
        if (state in HALL_STATES) prepareHall(request)
        if (state in INFO_STATES) return show(InfoLayerFixtureScreen(request.game))
        state?.removeSuffix("-fixture")?.takeIf(NOTICE_STATES::contains)?.let { return show(NoticeInfoFixtureScreen(request.game, it)) }
        if (state in PREPARATION_STATES) {
            request.campaignState.reset()
            request.campaignState.joinedUnits += listOf(0, 157, 181, 182)
            listOf(0, 157, 181, 182).forEach { request.campaignState.setUnitAttribute(it, 18, 3) }
            request.showBattlePreparation("R_00", "S_00", ScenarioJoinBattleLimit(1, 4, listOf(0), emptyList()), 71)
            return true
        }
        state?.removeSuffix("-fixture")?.takeIf(REWARD_STATES::contains)?.let { return show(RewardFixtureScreen(request.game, it)) }
        state?.removeSuffix("-fixture")?.takeIf(DIALOGUE_STATES::contains)?.let { return show(DialogueFixtureScreen(request.game, it)) }
        state?.removeSuffix("-fixture")?.takeIf(CHOICE_STATES::contains)?.let { return show(Choose2FixtureScreen(request.game, it)) }
        state?.removeSuffix("-fixture")?.takeIf(INPUT_STATES::contains)?.let { return show(InputBoxFixtureScreen(request.game, it)) }
        state?.removeSuffix("-fixture")?.takeIf(QUANTITY_STATES::contains)?.let { return show(MsgBox3FixtureScreen(request.game, it)) }
        state?.removeSuffix("-fixture")?.takeIf(OVERLAY_STATES::contains)?.let { return show(SystemOverlayFixtureScreen(request.game, it)) }
        return false
    }

    private fun prepareHall(request: RuntimeStartupRequest) {
        request.campaignState.reset(); request.campaignState.joinedUnits += listOf(0, 157, 181)
        listOf(0, 157, 181).forEach { request.campaignState.setUnitAttribute(it, 18, 3) }
    }

    private companion object {
        val HALL_STATES = setOf("hall-palace-fixture", "hall-section-fixture", "hall-forces-fixture", "hall-property-fixture", "hall-terrain-fixture", "hall-treasure-fixture", "hall-helper-fixture", "hall-equip-fixture", "hall-unit-list-fixture", "hall-unit-list-select-fixture", "hall-unit-list-close-fixture", "hall-equip-confirm-fixture", "hall-equip-confirm-unload-fixture", "hall-exclusive-fixture", "hall-exclusive-tab1-fixture", "hall-magic-fixture", "hall-feats-fixture", "hall-feats-help-fixture", "hall-buy-fixture", "hall-sell-fixture", "hall-skip-open-fixture")
        val INFO_STATES = setOf("info-layer-r00-first-tick", "info-layer-r00-full-autopending", "info-layer-r00-panel-touch", "info-layer-r00-skip")
        val NOTICE_STATES = setOf("notice-hidden", "notice-shown", "notice-messages", "notice-hidden-clear")
        val PREPARATION_STATES = setOf("start-battle-fixture", "start-battle-unit-info-fixture", "battle-view-fixture", "start-battle-sort-open-fixture", "start-battle-sort-select-fixture", "start-battle-sort-cancel-fixture")
        val REWARD_STATES = setOf("reward-basic", "reward-card-1", "reward-card-2")
        val DIALOGUE_STATES = setOf("dialogue-left", "dialogue-right", "dialogue-skip", "dialogue-auto-close")
        val CHOICE_STATES = setOf("choose2-open", "choose2-select")
        val INPUT_STATES = setOf("input-box-empty", "input-box-filled")
        val QUANTITY_STATES = setOf("quantity-buy-initial", "quantity-sell-edited")
        val OVERLAY_STATES = setOf("msgbox-ok", "msgbox-confirm", "toast-stable", "progress-0", "progress-23", "progress-100", "loading-default", "loading-flag1-before", "loading-flag1-after5", "loading-flag2-hidden")
    }
}
