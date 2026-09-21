// Verification
package com.jojo.game.verification

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.jojo.game.application.runtime.BattleRuntimeScreenProbe
import com.jojo.game.application.runtime.RuntimeBattleCommand
import com.jojo.game.application.runtime.RuntimeBattleDriver
import com.jojo.game.application.runtime.RuntimeBattleFrame
import com.jojo.game.application.runtime.RuntimeGridPoint
import com.jojo.game.domain.battle.Faction
import com.jojo.game.domain.scenario.PlaybackState
import kotlin.math.abs

/**
 * 위임 없이 플레이어가 직접 조작하는 전투를 흉내 낸다. 아군 유닛을 누르고, 인접한 적이 있으면 그 적을 눌러
 * 공격하며, 없으면 가장 가까운 적 쪽 이동 가능 칸을 누른 뒤 다시 공격을 시도한다. 더 할 일이 없으면
 * 메뉴 → 턴 종료 → 확인(위임 끔)으로 라운드를 넘긴다. 실제 InputProcessor에 눌림·뗌만 넣는다.
 */
internal class ManualBattleDriver : RuntimeBattleDriver {
    private var nextTapAt = Float.NEGATIVE_INFINITY
    private var lastStage: String? = null
    /** 이번 턴에 이미 이동을 시도한 유닛. 이동 뒤 공격할 적이 없으면 같은 유닛을 다시 고르지 않는다. */
    private val movedThisTurn = mutableSetOf<String>()
    private var seenRound = -1

    override fun commands(frame: RuntimeBattleFrame, probe: BattleRuntimeScreenProbe): List<RuntimeBattleCommand> {
        if (probe.outcome != null) return emptyList()
        if (frame.elapsed < nextTapAt) return emptyList()
        if (probe.playback == PlaybackState.DIALOGUE) {
            tap(probe.battleMenuButtonScreenX, probe.battleMenuButtonScreenY + 200)
            nextTapAt = frame.elapsed + .35f
            lastStage = null
            return emptyList()
        }
        if (probe.winConditionsOpen) {
            tap(probe.winConditionButtonScreenX, probe.winConditionButtonScreenY)
            nextTapAt = frame.elapsed + TAP_INTERVAL
            return emptyList()
        }
        if (!probe.bootstrapComplete || probe.collocation) return emptyList()
        if (probe.battle.snapshot.round != seenRound) {
            seenRound = probe.battle.snapshot.round
            movedThisTurn.clear()
        }
        if (probe.autoBattleOverlay == "PROMPT") {
            // 위임 토글은 건드리지 않고 "예"로 턴만 넘긴다.
            tap(probe.autoBattleConfirmScreenX, probe.autoBattleConfirmScreenY)
            nextTapAt = frame.elapsed + TAP_INTERVAL
            return emptyList()
        }
        if (probe.autoBattleOverlay != "NONE") return emptyList()
        if (probe.battleMenuOpen) {
            tap(probe.menuEndRoundScreenX, probe.menuEndRoundScreenY)
            nextTapAt = frame.elapsed + TAP_INTERVAL
            return emptyList()
        }
        if (probe.turnPhase != "PLAYER_INPUT") return emptyList()

        val units = probe.battle.snapshot.units
        val enemies = units.filter { it.visible && it.hitPoints > 0 && it.effectiveFaction.isEnemyOf(Faction.PLAYER) }
        val mine = units.filter { it.visible && it.hitPoints > 0 && it.effectiveFaction == Faction.PLAYER && !it.hasActed }
        val actor = mine.firstOrNull { it.id !in movedThisTurn || adjacentEnemy(it, enemies) != null }
        if (actor == null) {
            tap(probe.battleMenuButtonScreenX, probe.battleMenuButtonScreenY)
            nextTapAt = frame.elapsed + TAP_INTERVAL
            return emptyList()
        }
        if (probe.selectedUnitId != actor.id) {
            val (x, y) = probe.tileScreenPoint(actor.x, actor.y)
            tap(x, y)
            nextTapAt = frame.elapsed + TAP_INTERVAL
            return emptyList()
        }
        val target = adjacentEnemy(actor, enemies)
        if (target != null) {
            val (x, y) = probe.tileScreenPoint(target.x, target.y)
            tap(x, y)
            nextTapAt = frame.elapsed + ACTION_INTERVAL
            return emptyList()
        }
        if (actor.id in movedThisTurn) {
            tap(probe.battleMenuButtonScreenX, probe.battleMenuButtonScreenY)
            nextTapAt = frame.elapsed + TAP_INTERVAL
            return emptyList()
        }
        val nearest = enemies.minByOrNull { abs(it.x - actor.x) + abs(it.y - actor.y) }
        val reachable = probe.battle.reachableTiles(actor.id).filter { tile ->
            units.none { it.visible && it.hitPoints > 0 && it.x == tile.x && it.y == tile.y && it.id != actor.id }
        }
        val destination = nearest?.let { enemy ->
            reachable.minByOrNull { abs(it.x - enemy.x) + abs(it.y - enemy.y) }
        }
        movedThisTurn += actor.id
        if (destination == null || (destination.x == actor.x && destination.y == actor.y)) {
            tap(probe.battleMenuButtonScreenX, probe.battleMenuButtonScreenY)
            nextTapAt = frame.elapsed + TAP_INTERVAL
            return emptyList()
        }
        val (x, y) = probe.tileScreenPoint(destination.x, destination.y)
        tap(x, y)
        nextTapAt = frame.elapsed + ACTION_INTERVAL
        return emptyList()
    }

    private fun adjacentEnemy(actor: com.jojo.game.application.runtime.RuntimeBattleUnitSnapshot, enemies: List<com.jojo.game.application.runtime.RuntimeBattleUnitSnapshot>) =
        enemies.firstOrNull { enemy -> actor.attackOffsets.any { RuntimeGridPoint(actor.x + it.x, actor.y + it.y) == RuntimeGridPoint(enemy.x, enemy.y) } }

    private fun Faction.isEnemyOf(other: Faction): Boolean =
        (this == Faction.ENEMY || this == Faction.REINFORCEMENTS) != (other == Faction.ENEMY || other == Faction.REINFORCEMENTS)

    private fun tap(x: Int, y: Int) {
        val processor = Gdx.input.inputProcessor ?: return
        processor.touchDown(x, y, 0, Input.Buttons.LEFT)
        processor.touchUp(x, y, 0, Input.Buttons.LEFT)
    }

    private companion object {
        const val TAP_INTERVAL = .4f
        /** 이동·공격 뒤 연출이 끝날 시간을 준다. 상태창과 사망 연출이 겹치지 않게 넉넉히 둔다. */
        const val ACTION_INTERVAL = 1.5f
    }
}
