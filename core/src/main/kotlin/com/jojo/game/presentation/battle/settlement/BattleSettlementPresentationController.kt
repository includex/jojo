// Battle
package com.jojo.game.presentation.battle.settlement

import com.jojo.game.domain.battle.settlement.BattleSettlementPlan
import com.jojo.game.domain.battle.settlement.SettlementGrowthGrant
import com.jojo.game.domain.battle.settlement.SettlementInfoDelta
import com.jojo.game.domain.battle.settlement.SettlementInfoPanel

/** SettlementInfoView: 정산 Info 표시 정보이며, 화면에 필요한 전투 정보를 만들고 표시한다. */
internal data class SettlementInfoView(
    val unitId: String,
    val panel: SettlementInfoPanel,
    val startedAt: Float,
    val deltas: List<SettlementInfoDelta> = emptyList(),
    val grants: List<SettlementGrowthGrant> = emptyList(),
    val title: String,
)

/** SettlementInfo2View: 전투 화면에 전달할 불변 표시 상태를 보관한다. */
internal data class SettlementInfo2View(val text: String, val startedAt: Float, val endsAt: Float)
internal class BattleSettlementPresentationController {
    /** Effect: 전투 화면의 입력 또는 처리 결과를 전달하는 메시지이다. */
    sealed interface Effect {
        data class Focus(val unitId: String, val forceCenter: Boolean) : Effect
        data class Sound(val index: Int) : Effect
        data class Info2(val text: String, val startedAt: Float) : Effect
        data class Actions(val unitId: String, val actionId: Int) : Effect
        data class UnitInfo(val plan: com.jojo.game.domain.battle.settlement.SettlementUnitPlan, val startedAt: Float) : Effect
        data class GrowthInfo(val unitId: String, val grants: List<SettlementGrowthGrant>, val startedAt: Float) : Effect
        data class Meff(val effectId: Int, val targetIds: List<String>) : Effect
        data class ItemUpgrade(val result: com.jojo.game.domain.campaign.CampaignEquipmentExperienceResult) : Effect
        /** HideState: 전투 화면에 전달할 불변 표시 상태를 보관한다. */
        data class HideState(val unitIds: List<String>) : Effect
        data class Refresh(val unitIds: List<String>) : Effect
        data class Default(val unitId: String) : Effect
        data class Finished(val plan: BattleSettlementPlan, val local: Boolean) : Effect
    }
    private data class Active(
        val plan: BattleSettlementPlan,
        val operations: List<TurnSettlementOp>,
        val local: Boolean,
        var index: Int = 0,
        var actionIndex: Int = 0,
        var waitUntil: Float = 0f,
        var waiting: Waiting = Waiting.None,
    )
    private sealed interface Waiting {
        data object None : Waiting
        data object Action : Waiting
        data object Meff : Waiting
        data object ItemUpgrade : Waiting
        data class Timed(val until: Float) : Waiting
    }

    private var active: Active? = null
    private var info: SettlementInfoView? = null
    private var info2: SettlementInfo2View? = null

    fun isActive(): Boolean = active != null
    fun infoView(): SettlementInfoView? = info
    fun info2View(): SettlementInfo2View? = info2

    fun setInfoTitle(title: String) {
        info = info?.copy(title = title)
    }

    fun start(plan: BattleSettlementPlan, operations: List<TurnSettlementOp>, local: Boolean): Boolean {
        check(active == null) { "overlapping settlement presentation" }
        if (operations.isEmpty()) return true
        active = Active(plan, operations, local)
        return false
    }

    fun tick(now: Float, autoCloseInfo2: (String) -> Boolean): List<Effect> {
        val effects = mutableListOf<Effect>()
        while (true) {
            val state = active ?: return effects
            when (val waiting = state.waiting) {
                is Waiting.Timed -> if (now < waiting.until) return effects else completeCurrent()
                Waiting.Action, Waiting.Meff, Waiting.ItemUpgrade -> return effects
                Waiting.None -> Unit
            }
            val operation = state.operations.getOrNull(state.index) ?: run {
                val completed = state.plan to state.local
                active = null; info = null; info2 = null
                effects += Effect.Finished(completed.first, completed.second)
                return effects
            }
            when (operation) {
                is TurnSettlementOp.Focus -> {
                    effects += Effect.Focus(operation.unitId, operation.forceCenter)
                    if (operation.seconds > 0f) state.waiting = Waiting.Timed(now + operation.seconds) else completeCurrent()
                }
                is TurnSettlementOp.Sound -> { effects += Effect.Sound(operation.soundIndex); completeCurrent() }
                is TurnSettlementOp.Info2 -> {
                    val duration = if (autoCloseInfo2(operation.text)) operation.text.length * .04f + 1f else Float.POSITIVE_INFINITY
                    info2 = SettlementInfo2View(operation.text, now, now + duration)
                    effects += Effect.Info2(operation.text, now)
                    state.waiting = Waiting.Timed(now + duration)
                }
                is TurnSettlementOp.Actions -> {
                    effects += Effect.Actions(operation.unitId, operation.actionIds[state.actionIndex])
                    state.waiting = Waiting.Action
                }
                is TurnSettlementOp.UnitInfo -> {
                    info = SettlementInfoView(operation.plan.unitId, requireNotNull(operation.plan.infoPanel), now, operation.plan.infoDeltas, title = "")
                    effects += Effect.UnitInfo(operation.plan, now)
                    state.waiting = Waiting.Timed(now + operation.plan.infoBarrierSeconds)
                }
                is TurnSettlementOp.GrowthInfo -> {
                    info = SettlementInfoView(operation.unitId, SettlementInfoPanel.MINE, now, grants = operation.grants, title = "")
                    effects += Effect.GrowthInfo(operation.unitId, operation.grants, now)
                    val ticks = operation.grants.sumOf { kotlin.math.abs(it.unitResult?.gained ?: it.equipmentResult?.gained ?: 0).coerceAtMost(5) }
                    state.waiting = Waiting.Timed(now + .1f + ticks * .2f + .3f)
                }
                is TurnSettlementOp.Meff -> { effects += Effect.Meff(operation.effectId, operation.targetIds); state.waiting = Waiting.Meff }
                is TurnSettlementOp.ItemUpgrade -> { effects += Effect.ItemUpgrade(operation.result); state.waiting = Waiting.ItemUpgrade }
                is TurnSettlementOp.HideState -> { effects += Effect.HideState(operation.unitIds); completeCurrent() }
                is TurnSettlementOp.Refresh -> { effects += Effect.Refresh(operation.unitIds); completeCurrent() }
                is TurnSettlementOp.Default -> { effects += Effect.Default(operation.unitId); completeCurrent() }
            }
        }
    }

    fun actionCompleted() {
        val state = active ?: return
        val operation = state.operations.getOrNull(state.index) as? TurnSettlementOp.Actions ?: return
        if (++state.actionIndex < operation.actionIds.size) state.waiting = Waiting.None else completeCurrent()
    }

    fun meffCompleted() = completeIf(Waiting.Meff)
    fun itemUpgradeCompleted() = completeIf(Waiting.ItemUpgrade)

    fun dismissInfo2(now: Float): Boolean {
        val state = active ?: return false
        val overlay = info2 ?: return false
        val revealed = now - overlay.startedAt >= overlay.text.length * .04f
        if (!revealed) {
            val endsAt = if (overlay.endsAt.isFinite()) now + 1f else overlay.endsAt
            info2 = overlay.copy(startedAt = now - overlay.text.length * .04f, endsAt = endsAt)
            if (endsAt.isFinite()) state.waiting = Waiting.Timed(endsAt)
        } else completeCurrent()
        return true
    }

    private fun completeIf(expected: Waiting) {
        if (active?.waiting == expected) completeCurrent()
    }

    private fun completeCurrent() {
        val state = active ?: return
        state.index++; state.actionIndex = 0; state.waiting = Waiting.None; info = null; info2 = null
    }
}
