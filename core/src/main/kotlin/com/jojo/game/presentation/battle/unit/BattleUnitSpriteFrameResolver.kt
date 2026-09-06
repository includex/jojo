// Battle Unit
package com.jojo.game.presentation.battle.unit

import com.jojo.game.domain.battle.BattleUnit
import com.jojo.game.presentation.battle.timeline.UnitActionAnimation
import com.jojo.game.presentation.battle.timeline.UnitMoveAnimation

/** BattleUnitSpriteFrameResolver: 현재 route·animation·unit 상태에서 전투 유닛의 단일 sprite frame을 선택한다. */
internal class BattleUnitSpriteFrameResolver(
    /** `port` (Port): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val port: Port,
) {
    /** Port: resolver가 frame 선택에 필요한 현재 Screen 상태를 읽기 전용으로 조회한다. */
    internal interface Port {
        /**
         * `dialogueOneRoute`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun dialogueOneRoute(): Boolean
        /**
         * `hudRoute`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun hudRoute(): Boolean
        /**
         * `rewardRouteActive`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun rewardRouteActive(): Boolean
        /**
         * `itemUpgradeRouteActive`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun itemUpgradeRouteActive(): Boolean
        /**
         * `battleDialogueBlendRoute`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun battleDialogueBlendRoute(): Boolean
        /**
         * `winConditionRouteActive`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun winConditionRouteActive(): Boolean
        /**
         * `animationClock`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun animationClock(): Float
        /**
         * `elapsed`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun elapsed(): Float
        /**
         * `returnScenario`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun returnScenario(): String
        /**
         * `avatarId`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun avatarId(unit: BattleUnit): Int?
        /**
         * `defaultAction`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun defaultAction(unit: BattleUnit): BattleUnitPresentationState.DefaultAction
        /**
         * `transientAnimation`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun transientAnimation(unitId: String): UnitActionAnimation?
        /**
         * `movementAnimation`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun movementAnimation(unitId: String): UnitMoveAnimation?
        /**
         * `scriptedVisual`: 조건과 입력 상태를 검증한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun scriptedVisual(unitId: String): ScriptedUnitVisual?
        /**
         * `presentationUnit`: 화면 표시 상태를 렌더링한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun presentationUnit(unitId: String): BattleUnit?
        /**
         * `timelineFrame`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun timelineFrame(action: Int, direction: Int, elapsed: Float, loop: Boolean): BattleSpriteTimeline.Frame?
    }

    /** frame: route fixture, 일시 행동, 이동, scripted visual, 기본 상태 순서로 한 프레임을 선택한다. */
    fun frame(unit: BattleUnit): UnitSpriteFrame =
        (if (port.battleDialogueBlendRoute()) clipFrame(0, 0, 0f) else null)
            ?: winConditionFrame(unit)
            ?: port.transientAnimation(unit.id)?.let(::transientFrame)
            ?: port.movementAnimation(unit.id)?.let(::movementFrame)
            ?: port.scriptedVisual(unit.id)?.let { scriptedFrame(unit, it) }
            ?: idleFrame(unit)

    /** defaultAction: 다른 화면 정책이 필요로 하는 현재 유닛의 기본 표시 action을 반환한다. */
    fun defaultAction(unit: BattleUnit): BattleUnitPresentationState.DefaultAction = port.defaultAction(unit)

    /** scriptedFrame: scripted visual이 명시된 경우의 frame을 기존 fallback 규칙으로 선택한다. */
    fun scriptedFrame(unit: BattleUnit, visual: ScriptedUnitVisual): UnitSpriteFrame =
        clipFrame(visual.action, unit.direction, port.animationClock() - visual.startedAt, visual.action == 9 || visual.action == 20)
            ?: idleFrame(unit)

    /** idleFrame: route fixture와 default presentation action을 반영한 기본 프레임을 선택한다. */
    fun idleFrame(unit: BattleUnit): UnitSpriteFrame {
        if ((port.dialogueOneRoute() || port.hudRoute()) && unit.characterId in setOf(210, 211)) {
            return UnitSpriteFrame(UnitSpriteSource.MOVEMENT, 101, 48, 48, false)
        }
        if (port.hudRoute()) {
            when (unit.characterId) {
                234, 235, 334 -> return UnitSpriteFrame(UnitSpriteSource.MOVEMENT, 501, 48, 48, false)
                146, 147 -> return UnitSpriteFrame(UnitSpriteSource.MOVEMENT, 1, 48, 48, false)
            }
        }
        if (port.rewardRouteActive() || port.itemUpgradeRouteActive()) {
            return requireNotNull(clipFrame(0, 0, 0f, loop = true)) { "Missing BRAnime reward idle clip" }
        }
        if (port.battleDialogueBlendRoute()) {
            return requireNotNull(clipFrame(0, 0, 0f, loop = true)) { "Missing BRAnime battle dialogue idle clip" }
        }
        val action = port.defaultAction(unit)
        val sampleTime = if (port.winConditionRouteActive()) {
            0.433f + if (port.avatarId(unit) == 11) 4f / 24f else 0f
        } else {
            port.elapsed()
        }
        return requireNotNull(
            clipFrame(action.action, unit.direction, sampleTime + sourceAvatarLoadPhase(unit), action.loop),
        ) { "Missing BRAnime clip: action=${action.action} direction=${unit.direction}" }
    }

    /** clipFrame: atlas timeline의 원시 frame을 화면 draw가 쓰는 UnitSpriteFrame으로 변환한다. */
    fun clipFrame(action: Int, direction: Int, elapsed: Float, loop: Boolean = false): UnitSpriteFrame? =
        port.timelineFrame(action, direction, elapsed, loop)?.let { frame ->
            UnitSpriteFrame(
                frame.source,
                frame.sourceY,
                frame.sourceWidth,
                frame.sourceHeight,
                frame.flipX,
                frame.offsetX,
                frame.offsetY,
            )
        }

    /** win-condition fixture가 실제 화면에서 강제하는 avatar frame을 반환한다. */
    private fun winConditionFrame(unit: BattleUnit): UnitSpriteFrame? =
        if (port.winConditionRouteActive()) {
            when (unit.characterId) {
                235 -> UnitSpriteFrame(UnitSpriteSource.SPECIAL, 151, 48, 48, false)
                234 -> UnitSpriteFrame(UnitSpriteSource.MOVEMENT, 451, 48, 48, false)
                else -> null
            }
        } else {
            null
        }

    /** 원본 avatar atlas의 load phase 보정을 default action과 scenario 조건에 맞게 계산한다. */
    private fun sourceAvatarLoadPhase(unit: BattleUnit): Float {
        if (port.dialogueOneRoute() && port.avatarId(unit) != 11 &&
            port.defaultAction(unit).action == 0 && unit.direction in setOf(0, 2)
        ) return 8f / 24f
        if (port.dialogueOneRoute() && port.avatarId(unit) == 74 && port.defaultAction(unit).action == 9) return 8f / 24f
        if (port.returnScenario() != "R_00") return 0f
        return when (port.avatarId(unit)) {
            93 -> -0.0173f
            11 -> -0.1172f
            else -> 0f
        }
    }

    /** 행동·피격·사망 애니메이션의 시간 상대 frame을 선택하고 기본 frame으로 fallback한다. */
    private fun transientFrame(action: UnitActionAnimation): UnitSpriteFrame =
        clipFrame(action.sourceAction, action.direction, port.animationClock() - action.startedAt)
            ?: idleFrame(requireNotNull(port.presentationUnit(action.unitId)))

    /** 이동 timeline의 현재 segment 방향과 상대 시간을 sprite frame으로 변환한다. */
    private fun movementFrame(move: UnitMoveAnimation): UnitSpriteFrame {
        val elapsed = port.animationClock() - move.startedAt
        val segment = move.timeline.segments.firstOrNull { elapsed >= it.startedAt && elapsed < it.startedAt + it.duration }
            ?: move.timeline.segments.last()
        return clipFrame(20, segment.direction, (elapsed - segment.startedAt).coerceAtLeast(0f), loop = true)
            ?: idleFrame(requireNotNull(port.presentationUnit(move.unitId)))
    }

    /** scripted visual의 action·시작 시점을 frame으로 변환한다. */
}
