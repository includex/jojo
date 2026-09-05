package com.jojo.game
import com.jojo.game.presentation.battle.edit.*

import com.jojo.game.domain.scenario.*

import com.jojo.game.presentation.title.LoginOptionalOverlayRoute
import com.jojo.game.presentation.battle.unit.BattleSpriteFixtureScreen
import com.jojo.game.domain.battle.*
import com.jojo.game.presentation.title.TitleScreen
import com.jojo.game.domain.campaign.*

import com.badlogic.gdx.Screen

/** Routes explicit capture and verification requests before normal startup. */
internal class CaptureFixtureStartupRouter(
    private val game: JojoGame,
    private val captureState: String?,
    private val campaignState: CampaignState,
    private val showScreen: (Screen) -> Unit,
    private val showBattlePreparation: (String, String, ScenarioJoinBattleLimit, Int) -> Unit,
) {
    /**
     * 공개 메서드 `route`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Boolean`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun route(): Boolean {
        when (captureState) {
            "hall-achievements-fixture" -> return show(AchievementsFixtureScreen(game))
            "hall-attribute-fixture" -> return show(AttributeFixtureScreen(game))
            "hall-generic-list-fixture" -> return show(GenericListFixtureScreen(game))
        }
        when (captureState?.removeSuffix("-fixture")) {
            "login-modal-load" -> return show(ModalLoadRouteScreen(game))
            "raffle-gated" -> return show(RaffleGateRouteScreen(game))
        }
        LoginOptionalOverlayRoute.parse(captureState)?.let {
            return show(TitleScreen(game, initialSettingOpen = true, optionalOverlayRoute = it))
        }
        CmdRoute.parse(captureState)?.let { return show(CmdRouteScreen(game, it)) }
        TerminalSceneRoute.parse(captureState)?.let { return show(TerminalSceneRouteScreen(game, it)) }
        LearnUnitSkillRoute.parse(captureState)?.let { return show(LearnUnitSkillRouteScreen(game, it)) }
        DefineUnitRoute.parse(captureState)?.let { return show(DefineUnitRouteScreen(game, it)) }
        BattleUnitEditRoute.parse(captureState)?.let { return show(BattleUnitEditRouteScreen(game, it)) }
        EditRosterRoute.parse(captureState)?.let { return show(EditRosterRouteScreen(game, it)) }

        if (captureState in HALL_FIXTURES) prepareHallFixtureCampaign()
        parseSpriteFixtureRequest(captureState)?.let {
            return show(
                BattleSpriteFixtureScreen(
                    game,
                    it.characterId,
                    it.action,
                    it.direction,
                    it.frameTick,
                    it.faction
                )
            )
        }
        if (captureState in INFO_LAYER_FIXTURES) return show(InfoLayerFixtureScreen(game))
        captureState?.removeSuffix("-fixture")?.takeIf { it in NOTICE_FIXTURES }?.let {
            return show(NoticeInfoFixtureScreen(game, it))
        }
        if (captureState in BATTLE_PREPARATION_FIXTURES) {
            campaignState.reset()
            campaignState.joinedUnits += listOf(0, 157, 181, 182)
            listOf(0, 157, 181, 182).forEach { campaignState.setUnitAttribute(it, 18, 3) }
            showBattlePreparation("R_00", "S_00", ScenarioJoinBattleLimit(1, 4, listOf(0), emptyList()), 71)
            return true
        }
        captureState?.removeSuffix("-fixture")?.takeIf { it in REWARD_FIXTURES }?.let {
            return show(RewardFixtureScreen(game, it))
        }
        captureState?.removeSuffix("-fixture")?.takeIf { it in DIALOGUE_FIXTURES }?.let {
            return show(DialogueFixtureScreen(game, it))
        }
        captureState?.removeSuffix("-fixture")?.takeIf { it in CHOICE_FIXTURES }?.let {
            return show(Choose2FixtureScreen(game, it))
        }
        captureState?.removeSuffix("-fixture")?.takeIf { it in INPUT_BOX_FIXTURES }?.let {
            return show(InputBoxFixtureScreen(game, it))
        }
        captureState?.removeSuffix("-fixture")?.takeIf { it in QUANTITY_FIXTURES }?.let {
            return show(MsgBox3FixtureScreen(game, it))
        }
        captureState?.removeSuffix("-fixture")?.takeIf { it in SYSTEM_OVERLAY_FIXTURES }?.let {
            return show(SystemOverlayFixtureScreen(game, it))
        }
        return false
    }

    private fun show(screen: Screen): Boolean {
        showScreen(screen)
        return true
    }

    private fun prepareHallFixtureCampaign() {
        campaignState.reset()
        campaignState.joinedUnits += listOf(0, 157, 181)
        listOf(0, 157, 181).forEach { campaignState.setUnitAttribute(it, 18, 3) }
    }

    private companion object {
        val HALL_FIXTURES = setOf(
            "hall-palace-fixture", "hall-section-fixture", "hall-forces-fixture", "hall-property-fixture",
            "hall-terrain-fixture", "hall-treasure-fixture", "hall-helper-fixture", "hall-equip-fixture",
            "hall-unit-list-fixture", "hall-unit-list-select-fixture", "hall-unit-list-close-fixture",
            "hall-equip-confirm-fixture", "hall-equip-confirm-unload-fixture", "hall-exclusive-fixture",
            "hall-exclusive-tab1-fixture", "hall-magic-fixture", "hall-feats-fixture", "hall-feats-help-fixture",
            "hall-buy-fixture", "hall-sell-fixture", "hall-skip-open-fixture",
        )
        val INFO_LAYER_FIXTURES = setOf(
            "info-layer-r00-first-tick", "info-layer-r00-full-autopending",
            "info-layer-r00-panel-touch", "info-layer-r00-skip",
        )
        val NOTICE_FIXTURES = setOf("notice-hidden", "notice-shown", "notice-messages", "notice-hidden-clear")
        val BATTLE_PREPARATION_FIXTURES = setOf(
            "start-battle-fixture", "start-battle-unit-info-fixture", "battle-view-fixture",
            "start-battle-sort-open-fixture", "start-battle-sort-select-fixture", "start-battle-sort-cancel-fixture",
        )
        val REWARD_FIXTURES = setOf("reward-basic", "reward-card-1", "reward-card-2")
        val DIALOGUE_FIXTURES = setOf("dialogue-left", "dialogue-right", "dialogue-skip", "dialogue-auto-close")
        val CHOICE_FIXTURES = setOf("choose2-open", "choose2-select")
        val INPUT_BOX_FIXTURES = setOf("input-box-empty", "input-box-filled")
        val QUANTITY_FIXTURES = setOf("quantity-buy-initial", "quantity-sell-edited")
        val SYSTEM_OVERLAY_FIXTURES = setOf(
            "msgbox-ok", "msgbox-confirm", "toast-stable", "progress-0", "progress-23", "progress-100",
            "loading-default", "loading-flag1-before", "loading-flag1-after5", "loading-flag2-hidden",
        )
    }
}

internal data class SpriteFixtureRequest(
    val characterId: Int,
    val action: Int,
    val direction: Int,
    val frameTick: Int,
    val faction: Faction,
)

/** Parses the capture router's `sprite:<character>:<action>:<dir>:<tick>:<camp>` request. */
internal fun parseSpriteFixtureRequest(captureState: String?): SpriteFixtureRequest? {
    val parts = captureState?.takeIf { it.startsWith("sprite:") }?.split(':') ?: return null
    require(parts.size == 6) { "sprite fixture requires character:action:dir:tick:camp" }
    val faction = when (parts[5].toInt()) {
        0 -> Faction.PLAYER
        1 -> Faction.FRIEND
        2 -> Faction.ENEMY
        3 -> Faction.REINFORCEMENTS
        else -> error("sprite fixture camp must be 0, 1, 2, or 3")
    }
    return SpriteFixtureRequest(parts[1].toInt(), parts[2].toInt(), parts[3].toInt(), parts[4].toInt(), faction)
}
