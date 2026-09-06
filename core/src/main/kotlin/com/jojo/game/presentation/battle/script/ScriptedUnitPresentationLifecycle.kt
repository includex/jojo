// Battle
package com.jojo.game.presentation.battle.script

import com.jojo.game.domain.battle.*

import com.jojo.game.domain.scenario.ScenarioMapPresentationRequest
import com.jojo.game.domain.scenario.ScenarioUnitHideRequest
import com.jojo.game.domain.scenario.ScenarioUnitPostsRequest
import com.jojo.game.domain.scenario.ScenarioUnitShowRequest
import com.jojo.game.domain.scenario.ScriptedUnitAction
import com.jojo.game.presentation.battle.unit.ScriptedUnitVisual
internal class ScriptedUnitPresentationLifecycle {
    internal data class ActiveHide(
        val request: ScenarioUnitHideRequest,
        val battleUnitId: String,
        val endsAt: Float,
        val originalHp: Int,
    )
    internal data class PendingHide(
        val request: ScenarioUnitHideRequest,
        val battleUnitId: String,
    )
    internal data class ActiveShow(
        val request: ScenarioUnitShowRequest,
        val battleUnitId: String,
        val endsAt: Float,
    )
    internal data class ActivePosts(
        val request: ScenarioUnitPostsRequest,
        val battleUnitId: String,
    )
    internal data class ActiveMap(
        val request: ScenarioMapPresentationRequest,
        val endsAt: Float,
    )
    internal data class ActiveAction(
        val request: ScriptedUnitAction,
        val battleUnitId: String,
        val endsAt: Float,
    )

    private var hide: ActiveHide? = null
    private var pendingHide: PendingHide? = null
    private var show: ActiveShow? = null
    private var posts: ActivePosts? = null
    private var map: ActiveMap? = null
    private var action: ActiveAction? = null
    private val visualStates = mutableMapOf<String, ScriptedUnitVisual>()

    fun visual(unitId: String): ScriptedUnitVisual? = visualStates[unitId]

    fun setVisual(unitId: String, visual: ScriptedUnitVisual) {
        visualStates[unitId] = visual
    }

    fun clearVisual(unitId: String) {
        visualStates.remove(unitId)
    }

    val activeHide: ActiveHide? get() = hide
    val awaitingHideDialogue: PendingHide? get() = pendingHide
    val activeShow: ActiveShow? get() = show
    val activePosts: ActivePosts? get() = posts
    val activeMap: ActiveMap? get() = map
    val activeAction: ActiveAction? get() = action

    val hideBusy: Boolean get() = hide != null || pendingHide != null
    val showBusy: Boolean get() = show != null
    val actionBusy: Boolean get() = action != null

    fun awaitHideDialogue(request: ScenarioUnitHideRequest, battleUnitId: String) {
        pendingHide = PendingHide(request, battleUnitId)
    }

    fun takeHideDialogue(): PendingHide? = pendingHide.also { pendingHide = null }

    fun startHide(request: ScenarioUnitHideRequest, battleUnitId: String, endsAt: Float, originalHp: Int) {
        hide = ActiveHide(request, battleUnitId, endsAt, originalHp)
    }

    fun finishHide() {
        hide = null
    }

    fun startShow(request: ScenarioUnitShowRequest, battleUnitId: String, endsAt: Float) {
        show = ActiveShow(request, battleUnitId, endsAt)
    }

    fun finishShow() {
        show = null
    }

    fun startPosts(request: ScenarioUnitPostsRequest, battleUnitId: String) {
        posts = ActivePosts(request, battleUnitId)
    }

    fun finishPosts() {
        posts = null
    }

    fun startMap(request: ScenarioMapPresentationRequest, endsAt: Float) {
        map = ActiveMap(request, endsAt)
    }

    fun finishMap() {
        map = null
    }

    fun startAction(request: ScriptedUnitAction, battleUnitId: String, endsAt: Float) {
        action = ActiveAction(request, battleUnitId, endsAt)
    }

    fun finishAction() {
        action = null
    }
}
