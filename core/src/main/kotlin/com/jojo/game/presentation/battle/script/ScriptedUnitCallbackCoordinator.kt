// Battle
package com.jojo.game.presentation.battle.script

import com.jojo.game.domain.battle.*

import com.jojo.game.domain.battle.BattleUnit
import com.jojo.game.domain.scenario.Dialogue
import com.jojo.game.domain.scenario.ScenarioUnitHideRequest
import com.jojo.game.domain.scenario.ScenarioUnitPostsRequest
import com.jojo.game.domain.scenario.ScenarioUnitShowRequest
import com.jojo.game.presentation.battle.timeline.UnitDeathPresentation
/**
 * `ScriptedUnitCallbackCoordinator`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

internal class ScriptedUnitCallbackCoordinator(
    /** `lifecycle` (ScriptedUnitPresentationLifecycle): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val lifecycle: ScriptedUnitPresentationLifecycle,
    /** `port` (Port): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val port: Port,
) {
    /** Port: 전투 표현 계층이 외부 기능과 연결할 때 사용하는 계약이다. */
    internal interface Port {
        /**
         * `now`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun now(): Float
        /**
         * `consumeHide`: 현재 상태를 갱신한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun consumeHide(): ScenarioUnitHideRequest?
        /**
         * `consumeShow`: 현재 상태를 갱신한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun consumeShow(): ScenarioUnitShowRequest?
        /**
         * `consumePosts`: 현재 상태를 갱신한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun consumePosts(): ScenarioUnitPostsRequest?
        /**
         * `dialogueIsActive`: 조건과 입력 상태를 검증한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun dialogueIsActive(): Boolean
        /**
         * `presentDialogue`: 화면 표시 상태를 렌더링한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun presentDialogue(dialogue: Dialogue)
        /**
         * `hideUnit`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun hideUnit(request: ScenarioUnitHideRequest): BattleUnit?
        /**
         * `showUnit`: 화면 표시 상태를 렌더링한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun showUnit(request: ScenarioUnitShowRequest): BattleUnit?
        /**
         * `postsUnit`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun postsUnit(request: ScenarioUnitPostsRequest): BattleUnit?
        /**
         * `isMineMaster`: 조건과 입력 상태를 검증한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun isMineMaster(unitId: String): Boolean
        /**
         * `focus`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun focus(unit: BattleUnit)
        /**
         * `hideAction`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun hideAction(hideType: Int, selfMaster: Boolean): Int =
            UnitDeathPresentation.hideAction(hideType, selfMaster)
        /**
         * `sourceActionDuration`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun sourceActionDuration(action: Int, direction: Int): Float
        /**
         * `beginHideModel`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun beginHideModel(unit: BattleUnit, request: ScenarioUnitHideRequest, originalHp: Int)
        /**
         * `registerHideAnimation`: 조건과 입력 상태를 검증한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun registerHideAnimation(
            unit: BattleUnit,
            sourceAction: Int,
            startedAt: Float,
            endsAt: Float,
        )
        /**
         * `removeHideAnimation`: 상태와 자원을 정리한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun removeHideAnimation(unitId: String)
        /**
         * `completeHideModel`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun completeHideModel(unit: BattleUnit, request: ScenarioUnitHideRequest, originalHp: Int)
        /**
         * `completeUnitHide`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun completeUnitHide(request: ScenarioUnitHideRequest)
        /**
         * `prepareShow`: 화면 표시 상태를 렌더링한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun prepareShow(unit: BattleUnit, request: ScenarioUnitShowRequest): ShowStart
        /**
         * `finishShow`: 화면 표시 상태를 렌더링한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun finishShow(unitId: String, request: ScenarioUnitShowRequest)
        /**
         * `setVisibleWhenShowUnitMissing`: 현재 상태를 갱신한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun setVisibleWhenShowUnitMissing(unitId: Int)
        /**
         * `setOldAvatar`: 현재 상태를 갱신한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun setOldAvatar(unitId: String, avatarId: Int)
        /**
         * `publishLoadedAvatar`: 상태나 데이터를 조회한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun publishLoadedAvatar(unitId: String, avatarId: Int)
        /**
         * `resumeScript`: 입력을 규칙에 따라 계산·변환한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun resumeScript()
    }
    /**
     * `ShowStart`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    internal data class ShowStart(val unitId: String, val duration: Float)

    /**
     * `hideBusy` (Boolean get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val hideBusy: Boolean get() = lifecycle.hideBusy
    /**
     * `showBusy` (Boolean get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val showBusy: Boolean get() = lifecycle.showBusy

    /**
     * `driveHide`: 흐름을 실행하거나 다음 단계로 전달한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun driveHide() {
        val active = lifecycle.activeHide
        if (active != null) {
            if (port.now() < active.endsAt) return
            port.removeHideAnimation(active.battleUnitId)
            port.completeUnitHide(active.request)
            port.hideUnit(active.request)?.let { port.completeHideModel(it, active.request, active.originalHp) }
            lifecycle.finishHide()
            if (active.request.resumesScript) port.resumeScript() else driveHide()
            return
        }
        val pending = lifecycle.awaitingHideDialogue
        if (pending != null) {
            if (port.dialogueIsActive()) return
            lifecycle.takeHideDialogue()
            val unit = port.hideUnit(pending.request)
            if (unit == null) {
                completeWithoutAnimation(pending.request)
                if (!pending.request.resumesScript) driveHide()
            } else {
                startHide(pending.request, unit)
            }
            return
        }
        val request = port.consumeHide() ?: return
        val unit = port.hideUnit(request)
        if (unit == null || !unit.visible) {
            completeWithoutAnimation(request)
            if (!request.resumesScript) driveHide()
            return
        }
        port.focus(unit)
        val effectiveType = if (request.hideType == 1 && port.isMineMaster(unit.id)) 2 else request.hideType
        val effective = request.copy(hideType = effectiveType)
        val message = unit.retireMessage.takeIf { unit.deathMessageEnabled }
        if (request.showsRetireMessage && message != null) {
            lifecycle.awaitHideDialogue(effective, unit.id)
            port.presentDialogue(Dialogue(unit.characterId?.toString(), message))
        } else {
            startHide(effective, unit)
        }
    }

    /**
     * `completeWithoutAnimation`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun completeWithoutAnimation(request: ScenarioUnitHideRequest) {
        port.completeUnitHide(request)
        if (request.resumesScript) port.resumeScript()
    }

    /**
     * `startHide`: 흐름을 실행하거나 다음 단계로 전달한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun startHide(request: ScenarioUnitHideRequest, unit: BattleUnit) {
        port.focus(unit)
        val sourceAction = port.hideAction(request.hideType, port.isMineMaster(unit.id))
        val originalHp = unit.hitPoints
        val startedAt = port.now()
        val endsAt = startedAt + port.sourceActionDuration(sourceAction, unit.direction)
        port.beginHideModel(unit, request, originalHp)
        port.registerHideAnimation(unit, sourceAction, startedAt, endsAt)
        lifecycle.startHide(request, unit.id, endsAt, originalHp)
    }

    /**
     * `driveShow`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun driveShow() {
        val active = lifecycle.activeShow
        if (active != null) {
            if (port.now() < active.endsAt) return
            lifecycle.clearVisual(active.battleUnitId)
            port.finishShow(active.battleUnitId, active.request)
            lifecycle.finishShow()
            port.resumeScript()
            return
        }
        val request = port.consumeShow() ?: return
        val existing = port.showUnit(request)
        if (existing == null) {
            port.setVisibleWhenShowUnitMissing(request.unitId)
            port.resumeScript()
            return
        }
        val prepared = port.prepareShow(existing, request)
        lifecycle.startShow(request, prepared.unitId, port.now() + prepared.duration)
    }

    /**
     * `drivePosts`: 흐름을 실행하거나 다음 단계로 전달한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun drivePosts() {
        val active = lifecycle.activePosts
        if (active != null) {
            port.publishLoadedAvatar(active.battleUnitId, active.request.newAvatarId)
            lifecycle.finishPosts()
            if (active.request.pausesScript) port.resumeScript()
            return
        }
        val request = port.consumePosts() ?: return
        val unit = port.postsUnit(request)
        if (unit == null) {
            if (request.pausesScript) port.resumeScript()
            return
        }
        port.setOldAvatar(unit.id, request.oldAvatarId)
        lifecycle.startPosts(request, unit.id)
    }
}
