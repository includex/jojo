// Battle
package com.jojo.game.presentation.battle.script

import com.jojo.game.domain.battle.*

import com.jojo.game.domain.battle.BattleUnit
import com.jojo.game.domain.scenario.Dialogue
import com.jojo.game.domain.scenario.ScenarioUnitHideRequest
import com.jojo.game.domain.scenario.ScenarioUnitPostsRequest
import com.jojo.game.domain.scenario.ScenarioUnitShowRequest
import com.jojo.game.presentation.battle.timeline.UnitDeathPresentation
internal class ScriptedUnitCallbackCoordinator(
    private val lifecycle: ScriptedUnitPresentationLifecycle,
    private val port: Port,
) {
    /** Port: 전투 표현 계층이 외부 기능과 연결할 때 사용하는 계약이다. */
    internal interface Port {
        fun now(): Float
        fun consumeHide(): ScenarioUnitHideRequest?
        fun consumeShow(): ScenarioUnitShowRequest?
        fun consumePosts(): ScenarioUnitPostsRequest?
        fun dialogueIsActive(): Boolean
        fun presentDialogue(dialogue: Dialogue)
        fun hideUnit(request: ScenarioUnitHideRequest): BattleUnit?
        fun showUnit(request: ScenarioUnitShowRequest): BattleUnit?
        fun postsUnit(request: ScenarioUnitPostsRequest): BattleUnit?
        fun isMineMaster(unitId: String): Boolean
        fun focus(unit: BattleUnit)
        fun hideAction(hideType: Int, selfMaster: Boolean): Int =
            UnitDeathPresentation.hideAction(hideType, selfMaster)
        fun sourceActionDuration(action: Int, direction: Int): Float
        fun beginHideModel(unit: BattleUnit, request: ScenarioUnitHideRequest, originalHp: Int)
        fun registerHideAnimation(
            unit: BattleUnit,
            sourceAction: Int,
            startedAt: Float,
            endsAt: Float,
        )
        fun removeHideAnimation(unitId: String)
        fun completeHideModel(unit: BattleUnit, request: ScenarioUnitHideRequest, originalHp: Int)
        fun completeUnitHide(request: ScenarioUnitHideRequest)
        fun prepareShow(unit: BattleUnit, request: ScenarioUnitShowRequest): ShowStart
        fun finishShow(unitId: String, request: ScenarioUnitShowRequest)
        fun setVisibleWhenShowUnitMissing(unitId: Int)
        fun setOldAvatar(unitId: String, avatarId: Int)
        fun publishLoadedAvatar(unitId: String, avatarId: Int)
        fun resumeScript()
    }
    internal data class ShowStart(val unitId: String, val duration: Float)

    val hideBusy: Boolean get() = lifecycle.hideBusy
    val showBusy: Boolean get() = lifecycle.showBusy

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

    private fun completeWithoutAnimation(request: ScenarioUnitHideRequest) {
        port.completeUnitHide(request)
        if (request.resumesScript) port.resumeScript()
    }

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
