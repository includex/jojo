// Battle
package com.jojo.game.presentation.battle.script

import com.jojo.game.domain.battle.*

import com.jojo.game.domain.scenario.ScenarioMapPresentationRequest
import com.jojo.game.domain.scenario.ScenarioUnitHideRequest
import com.jojo.game.domain.scenario.ScenarioUnitPostsRequest
import com.jojo.game.domain.scenario.ScenarioUnitShowRequest
import com.jojo.game.domain.scenario.ScriptedUnitAction
import com.jojo.game.presentation.battle.unit.ScriptedUnitVisual
/**
 * `ScriptedUnitPresentationLifecycle`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

internal class ScriptedUnitPresentationLifecycle {
    /**
     * `ActiveHide`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    internal data class ActiveHide(
        /**
         * `request` (ScenarioUnitHideRequest,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val request: ScenarioUnitHideRequest,
        /**
         * `battleUnitId` (String,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val battleUnitId: String,
        /**
         * `endsAt` (Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val endsAt: Float,
        /**
         * `originalHp` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val originalHp: Int,
    )
    /**
     * `PendingHide`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    internal data class PendingHide(
        /**
         * `request` (ScenarioUnitHideRequest,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val request: ScenarioUnitHideRequest,
        /**
         * `battleUnitId` (String,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val battleUnitId: String,
    )
    /**
     * `ActiveShow`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    internal data class ActiveShow(
        /**
         * `request` (ScenarioUnitShowRequest,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val request: ScenarioUnitShowRequest,
        /**
         * `battleUnitId` (String,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val battleUnitId: String,
        /**
         * `endsAt` (Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val endsAt: Float,
    )
    /**
     * `ActivePosts`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    internal data class ActivePosts(
        /**
         * `request` (ScenarioUnitPostsRequest,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val request: ScenarioUnitPostsRequest,
        /**
         * `battleUnitId` (String,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val battleUnitId: String,
    )
    /**
     * `ActiveMap`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    internal data class ActiveMap(
        /**
         * `request` (ScenarioMapPresentationRequest,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val request: ScenarioMapPresentationRequest,
        /**
         * `endsAt` (Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val endsAt: Float,
    )
    /**
     * `ActiveAction`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    internal data class ActiveAction(
        /**
         * `request` (ScriptedUnitAction,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val request: ScriptedUnitAction,
        /**
         * `battleUnitId` (String,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val battleUnitId: String,
        /**
         * `endsAt` (Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val endsAt: Float,
    )

    /**
     * `hide` (ActiveHide?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var hide: ActiveHide? = null
    /**
     * `pendingHide` (PendingHide?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var pendingHide: PendingHide? = null
    /**
     * `show` (ActiveShow?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var show: ActiveShow? = null
    /**
     * `posts` (ActivePosts?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var posts: ActivePosts? = null
    /**
     * `map` (ActiveMap?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var map: ActiveMap? = null
    /**
     * `action` (ActiveAction?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var action: ActiveAction? = null
    /**
     * `visualStates` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val visualStates = mutableMapOf<String, ScriptedUnitVisual>()
    private val visualRevisions = mutableMapOf<String, Long>()

    /**
     * `visual`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun visual(unitId: String): ScriptedUnitVisual? = visualStates[unitId]

    /**
     * `setVisual`: 현재 상태를 갱신한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun setVisual(unitId: String, visual: ScriptedUnitVisual) {
        visualRevisions[unitId] = (visualRevisions[unitId] ?: 0L) + 1L
        visualStates[unitId] = visual
    }

    /**
     * `clearVisual`: 현재 상태를 갱신한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun clearVisual(unitId: String) {
        visualRevisions[unitId] = (visualRevisions[unitId] ?: 0L) + 1L
        visualStates.remove(unitId)
    }

    /** Source `defaultAction` is a semantic command that replaces any held scripted pose. */
    fun applyDefaultAction(unitId: String, apply: () -> Unit) {
        clearVisual(unitId)
        apply()
    }

    /** Install a hit pose at its authored hit event unless another command supersedes it. */
    fun scheduleVisual(
        unitId: String,
        visual: ScriptedUnitVisual,
        schedule: (Float, () -> Unit) -> Unit,
        isCurrent: () -> Boolean,
    ) {
        val revision = visualRevisions[unitId] ?: 0L
        schedule(visual.startedAt) {
            if ((visualRevisions[unitId] ?: 0L) == revision && isCurrent()) setVisual(unitId, visual)
        }
    }

    /** A later blocked hit returns to defaultAction without reviving an older held pose. */
    fun scheduleVisualClear(
        unitId: String,
        startsAt: Float,
        schedule: (Float, () -> Unit) -> Unit,
        isCurrent: () -> Boolean,
    ) {
        val revision = visualRevisions[unitId] ?: 0L
        schedule(startsAt) {
            if ((visualRevisions[unitId] ?: 0L) == revision && isCurrent()) clearVisual(unitId)
        }
    }

    /**
     * `activeHide` (ActiveHide? get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val activeHide: ActiveHide? get() = hide
    /**
     * `awaitingHideDialogue` (PendingHide? get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val awaitingHideDialogue: PendingHide? get() = pendingHide
    /**
     * `activeShow` (ActiveShow? get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val activeShow: ActiveShow? get() = show
    /**
     * `activePosts` (ActivePosts? get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val activePosts: ActivePosts? get() = posts
    /**
     * `activeMap` (ActiveMap? get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val activeMap: ActiveMap? get() = map
    /**
     * `activeAction` (ActiveAction? get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val activeAction: ActiveAction? get() = action

    /**
     * `hideBusy` (Boolean get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val hideBusy: Boolean get() = hide != null || pendingHide != null
    /**
     * `showBusy` (Boolean get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val showBusy: Boolean get() = show != null
    /**
     * `actionBusy` (Boolean get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val actionBusy: Boolean get() = action != null

    /**
     * `awaitHideDialogue`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun awaitHideDialogue(request: ScenarioUnitHideRequest, battleUnitId: String) {
        pendingHide = PendingHide(request, battleUnitId)
    }

    /**
     * `takeHideDialogue`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun takeHideDialogue(): PendingHide? = pendingHide.also { pendingHide = null }

    /**
     * `startHide`: 흐름을 실행하거나 다음 단계로 전달한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun startHide(request: ScenarioUnitHideRequest, battleUnitId: String, endsAt: Float, originalHp: Int) {
        hide = ActiveHide(request, battleUnitId, endsAt, originalHp)
    }

    /**
     * `finishHide`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun finishHide() {
        hide = null
    }

    /**
     * `startShow`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun startShow(request: ScenarioUnitShowRequest, battleUnitId: String, endsAt: Float) {
        show = ActiveShow(request, battleUnitId, endsAt)
    }

    /**
     * `finishShow`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun finishShow() {
        show = null
    }

    /**
     * `startPosts`: 흐름을 실행하거나 다음 단계로 전달한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun startPosts(request: ScenarioUnitPostsRequest, battleUnitId: String) {
        posts = ActivePosts(request, battleUnitId)
    }

    /**
     * `finishPosts`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun finishPosts() {
        posts = null
    }

    /**
     * `startMap`: 흐름을 실행하거나 다음 단계로 전달한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun startMap(request: ScenarioMapPresentationRequest, endsAt: Float) {
        map = ActiveMap(request, endsAt)
    }

    /**
     * `finishMap`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun finishMap() {
        map = null
    }

    /**
     * `startAction`: 흐름을 실행하거나 다음 단계로 전달한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun startAction(request: ScriptedUnitAction, battleUnitId: String, endsAt: Float) {
        action = ActiveAction(request, battleUnitId, endsAt)
    }

    /**
     * `finishAction`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun finishAction() {
        action = null
    }
}
