// Battle Route
package com.jojo.game.presentation.battle.route

import com.jojo.game.application.runtime.RuntimeBattlePresentation
import com.jojo.game.application.runtime.RuntimeBattleRoute
import com.jojo.game.presentation.battle.edit.BattleEditLayer2Route
import com.jojo.game.presentation.battle.timeline.BattleCharacterStrictState

/** BattlePresentationConfiguration: 런타임 전투 경로를 화면 route와 animation clock 정책으로 해석한다. */
internal class BattlePresentationConfiguration(
    /** `presentation` (RuntimeBattlePresentation): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val presentation: RuntimeBattlePresentation,
) {
    /** 현재 화면이 해석할 런타임 전투 route다. */
    private val route get() = presentation.route

    /** 보상 화면으로 고정할 route이면 해당 route를, 아니면 null을 반환한다. */
    val rewardRouteState = route.takeIf { it in REWARD_ROUTES }

    /** 장비 강화 화면 route 여부를 나타낸다. */
    val itemUpgradeRouteState = route.takeIf { it == RuntimeBattleRoute.ITEM_UPGRADE }

    /** 패배 후 재시작 안내 route 여부를 나타낸다. */
    val loseRestartRoute = route == RuntimeBattleRoute.LOSE_RESTART

    /** 라운드 안내 화면 route이면 해당 route를 반환한다. */
    val roundRouteState = route.takeIf { it in ROUND_ROUTES }

    /** 승리 조건 화면 route이면 해당 route를 반환한다. */
    val winConditionRouteState = route.takeIf { it in WIN_ROUTES }

    /** 미니맵 표시 상태 route이면 해당 route를 반환한다. */
    val miniMapRouteState = route.takeIf { it in MINI_MAP_ROUTES }

    /** 자동 전투 안내·활성 route이면 해당 route를 반환한다. */
    val autoBattleRouteState = route.takeIf { it in AUTO_ROUTES }

    /** 전투 명령 메뉴 route이면 해당 route를 반환한다. */
    val battleCommandRouteState = route.takeIf { it in COMMAND_ROUTES }

    /** 캐릭터 연출 route를 화면의 엄격한 fixture 상태로 변환한다. */
    val battleCharacterRouteState = when (route) {
        RuntimeBattleRoute.CHARACTER_HP_CAMPS -> BattleCharacterStrictState.HP_CAMPS_PARTIAL
        RuntimeBattleRoute.CHARACTER_OUTLINE -> BattleCharacterStrictState.OUTLINE_HIGHLIGHT
        RuntimeBattleRoute.CHARACTER_HIT -> BattleCharacterStrictState.HIT_IMPACT
        RuntimeBattleRoute.CHARACTER_CLEANUP -> BattleCharacterStrictState.CLEANUP
        RuntimeBattleRoute.CHARACTER_DEATH_ACTION -> BattleCharacterStrictState.DEATH_ACTION
        RuntimeBattleRoute.CHARACTER_DEATH_HIDDEN -> BattleCharacterStrictState.DEATH_HIDDEN
        else -> null
    }

    /** 편집 화면 route를 BattleEditLayer2가 소비하는 세부 route로 변환한다. */
    val battleEdit2RouteState = when (route) {
        RuntimeBattleRoute.EDIT_INITIAL -> BattleEditLayer2Route.INITIAL
        RuntimeBattleRoute.EDIT_WEATHER -> BattleEditLayer2Route.WEATHER
        RuntimeBattleRoute.EDIT_ROUND -> BattleEditLayer2Route.ROUND
        RuntimeBattleRoute.EDIT_APPLY -> BattleEditLayer2Route.APPLY
        RuntimeBattleRoute.EDIT_CHILD -> BattleEditLayer2Route.CHILD
        RuntimeBattleRoute.EDIT_CHILD_SCENE -> BattleEditLayer2Route.CHILD_SCENE
        RuntimeBattleRoute.EDIT_REGISTER -> BattleEditLayer2Route.REGISTER
        else -> null
    }

    /** 다른 진영 유닛 정보 route 여부를 나타낸다. */
    val otherUnitInfoRoute = route == RuntimeBattleRoute.OTHER_UNIT_INFO

    /** 아군 유닛 정보 route 여부를 나타낸다. */
    val mineUnitInfoRoute = route == RuntimeBattleRoute.MINE_UNIT_INFO

    /** 대사 blend 화면 route 여부를 나타낸다. */
    val battleDialogueBlendRoute = route == RuntimeBattleRoute.DIALOGUE_BLEND

    /** 전투 초기화 화면 route 여부를 나타낸다. */
    val battleInitRoute = route == RuntimeBattleRoute.INITIAL

    /** 지형 정보 화면 route 여부를 나타낸다. */
    val battleTerrainRoute = route == RuntimeBattleRoute.TERRAIN

    /** 전투 메뉴 route 여부를 나타낸다. */
    val battleMenuRoute = route == RuntimeBattleRoute.MENU

    /** 도움말 overlay route 여부를 나타낸다. */
    val helperRoute = route == RuntimeBattleRoute.HELPER

    /** 승리 조건 모달 route 여부를 나타낸다. */
    val winModalRoute = route == RuntimeBattleRoute.WIN_MODAL

    /** 유닛 정보 overlay route 여부를 나타낸다. */
    val unitInfoRoute = route == RuntimeBattleRoute.UNIT_INFO

    /** 패배 결과 화면 route 여부를 나타낸다. */
    val loseResultRoute = route == RuntimeBattleRoute.RESULT_LOSE

    /** 승리 저장 확인 화면 route 여부를 나타낸다. */
    val winResultRoute = route == RuntimeBattleRoute.RESULT_WIN

    /** 시작 대사 화면 route 여부를 나타낸다. */
    val openingSayRoute = route == RuntimeBattleRoute.OPENING_SAY

    /** HUD fixture route 여부를 나타낸다. */
    val hudRoute = route == RuntimeBattleRoute.HUD

    /** 첫 대사 fixture route 여부를 나타낸다. */
    val dialogueOneRoute = route == RuntimeBattleRoute.DIALOGUE_ONE

    /** 적군 AI planner fixture route 여부를 나타낸다. */
    val enemyTurnRoute = route == RuntimeBattleRoute.ENEMY_TURN

    /** 대사 구성 요소별 fixture route이면 해당 route를 반환한다. */
    val dialogueComponentStage = route.takeIf { it in DIALOGUE_COMPONENT_ROUTES }

    /** 기력(JiQi) overlay route 여부를 나타낸다. */
    val jiqiRoute = route == RuntimeBattleRoute.JIQI

    /** 마법 목록·상세 route이면 해당 route를 반환한다. */
    val magickRoute = route.takeIf { it == RuntimeBattleRoute.MAGICK_LIST || it == RuntimeBattleRoute.MAGICK_DETAIL }

    /** 아이템 사용 목록·상세·선택·취소 route이면 해당 route를 반환한다. */
    val usePropertyRoute = route.takeIf { it in USE_PROPERTY_ROUTES }

    /** 현재 UI route와 fixture action sample 정책에 맞는 유닛 animation clock을 반환한다. */
    fun animationClock(elapsed: Float, battleElapsed: Float, actionSampleMode: Boolean): Float = when {
        rewardRouteState != null || winConditionRouteState != null -> 0f
        actionSampleMode -> elapsed
        else -> battleElapsed
    }

    /** 현재 route 정책에 맞는 맵 오브젝트 animation clock을 반환한다. */
    fun mapObjectAnimationClock(battleElapsed: Float): Float = when {
        rewardRouteState != null || winConditionRouteState != null -> 0f
        else -> battleElapsed
    }

    companion object {
        /** 보상 화면으로 해석하는 runtime routes다. */
        private val REWARD_ROUTES = setOf(
            RuntimeBattleRoute.REWARD_BASIC,
            RuntimeBattleRoute.REWARD_CARD1,
            RuntimeBattleRoute.REWARD_CARD2,
        )

        /** 라운드 안내로 해석하는 runtime routes다. */
        private val ROUND_ROUTES = setOf(
            RuntimeBattleRoute.ROUND_NORMAL,
            RuntimeBattleRoute.ROUND_FINAL,
            RuntimeBattleRoute.ROUND_ENEMY,
        )

        /** 승리 조건으로 해석하는 runtime routes다. */
        private val WIN_ROUTES = setOf(RuntimeBattleRoute.WIN_COMPACT, RuntimeBattleRoute.WIN_FULL)

        /** 미니맵 표시 상태로 해석하는 runtime routes다. */
        private val MINI_MAP_ROUTES = setOf(RuntimeBattleRoute.MINI_MAP_SHOWN, RuntimeBattleRoute.MINI_MAP_HIDDEN)

        /** 자동 전투 상태로 해석하는 runtime routes다. */
        private val AUTO_ROUTES = setOf(
            RuntimeBattleRoute.AUTO_PROMPT_OFF,
            RuntimeBattleRoute.AUTO_PROMPT_ON,
            RuntimeBattleRoute.AUTO_ACTIVE,
        )

        /** 전투 명령 메뉴로 해석하는 runtime routes다. */
        private val COMMAND_ROUTES = setOf(
            RuntimeBattleRoute.COMMAND_INITIAL,
            RuntimeBattleRoute.COMMAND_DISABLED,
            RuntimeBattleRoute.COMMAND_CANCEL,
            RuntimeBattleRoute.COMMAND_MAGICK,
            RuntimeBattleRoute.COMMAND_PROPERTY,
        )

        /** 대사 구성 요소별 capture로 해석하는 runtime routes다. */
        private val DIALOGUE_COMPONENT_ROUTES = setOf(
            RuntimeBattleRoute.DIALOGUE_COMPONENT_BACKGROUND,
            RuntimeBattleRoute.DIALOGUE_COMPONENT_CHARACTERS,
            RuntimeBattleRoute.DIALOGUE_COMPONENT_LABELS,
            RuntimeBattleRoute.DIALOGUE_COMPONENT_DIALOGUE,
        )

        /** 아이템 사용 흐름으로 해석하는 runtime routes다. */
        private val USE_PROPERTY_ROUTES = setOf(
            RuntimeBattleRoute.USE_PROPERTY_LIST,
            RuntimeBattleRoute.USE_PROPERTY_DETAIL,
            RuntimeBattleRoute.USE_PROPERTY_SELECT,
            RuntimeBattleRoute.USE_PROPERTY_CANCEL,
        )
    }
}
