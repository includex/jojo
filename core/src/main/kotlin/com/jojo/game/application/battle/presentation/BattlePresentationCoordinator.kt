// Battle
package com.jojo.game.application.battle.presentation

import com.jojo.game.domain.battle.*
import com.jojo.game.*
import com.jojo.game.application.battle.*
import com.jojo.game.application.battle.ai.*
import com.jojo.game.application.battle.combat.*
import com.jojo.game.application.battle.experience.*
import com.jojo.game.application.battle.movement.*
import com.jojo.game.application.battle.presentation.*
import com.jojo.game.application.battle.round.*
import com.jojo.game.domain.battle.BattleUnitMemento
import com.jojo.game.domain.battle.Battlefield
import com.jojo.game.domain.battle.BattleActionSnapshot

/**
 * `BattlePresentationEnvironment` 클래스: presentation 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

internal data class BattlePresentationEnvironment(
    val battlefield: Battlefield,
    val units: () -> Map<String, BattleUnit>,
    val playerMoney: () -> Int,
    val setPlayerMoney: (Int) -> Unit,
    val enemyMoney: () -> Int,
    val setEnemyMoney: (Int) -> Unit,
    val skillTemps: BattleSkillTemp,
    val moveLength: () -> Int,
    val setMoveLength: (Int) -> Unit,
    val lastMovePaths: MutableMap<String, List<Pair<Int, Int>>>,
    val traceActions: MutableList<String>,
    val getPendingActionTransaction: () -> BattleActionTransaction?,
    val setPendingActionTransaction: (BattleActionTransaction?) -> Unit,
    val getStagedHitSideEffects: () -> MutableList<() -> Unit>?,
    val setStagedHitSideEffects: (MutableList<() -> Unit>?) -> Unit,
    val getStagedCompletionSideEffects: () -> MutableList<() -> Unit>?,
    val setStagedCompletionSideEffects: (MutableList<() -> Unit>?) -> Unit,
)

/** BattlePresentationCoordinator: 실제 전투 결과를 잠시 되돌린 뒤, 표현 완료 순서에 맞춰 적용할 트랜잭션을 만든다. */
internal object BattlePresentationCoordinator {


    /**
     * `runtimeSnapshot`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun runtimeSnapshot(env: BattlePresentationEnvironment): BattleActionSnapshot {
        val all = linkedMapOf<String, BattleUnit>().apply {
            putAll(env.units())
            env.battlefield.pendingPresentationUnits().forEach { unit -> put(unit.id, unit) }
        }
        return BattleActionSnapshot(
            topology = env.battlefield.snapshotTopology(),
            states = all.mapValues { (_, unit) -> BattleUnitMemento.capture(unit) },
            playerMoney = env.playerMoney(),
            enemyMoney = env.enemyMoney(),
            skillTemps = env.skillTemps.snapshot(),
            moveLength = env.moveLength(),
            lastMovePaths = env.lastMovePaths.mapValues { it.value.toList() },
            traceActions = env.traceActions.toList(),
        )
    }


    /**
     * `restoreRuntime`: 해당 흐름을 실행하거나 다음 단계로 전달한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun restoreRuntime(snapshot: BattleActionSnapshot, env: BattlePresentationEnvironment) {
        snapshot.states.values.forEach(BattleUnitMemento::restore)
        env.battlefield.restoreTopology(snapshot.topology, snapshot.states.mapValues { it.value.unit })
        env.setPlayerMoney(snapshot.playerMoney)
        env.setEnemyMoney(snapshot.enemyMoney)
        env.skillTemps.restore(snapshot.skillTemps)
        env.setMoveLength(snapshot.moveLength)
        env.lastMovePaths.clear()
        env.lastMovePaths.putAll(snapshot.lastMovePaths)
        env.traceActions.clear()
        env.traceActions.addAll(snapshot.traceActions)
    }

    /**
     * `createActionTransaction`: 필요한 객체나 결과를 생성한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun createActionTransaction(
        actorId: String,
        before: BattleActionSnapshot,
        after: BattleActionSnapshot,
        hitSideEffects: List<() -> Unit>,
        completionSideEffects: List<() -> Unit>,
        env: BattlePresentationEnvironment,
    ): BattleActionTransaction = BattleActionTransaction(
        actorId = actorId,
        before = before,
        after = after,
        hitSideEffects = hitSideEffects,
        completionSideEffects = completionSideEffects,
        restoreSnapshot = { restoreRuntime(it, env) },
        adjustEconomy = { playerDelta, enemyDelta ->
            env.setPlayerMoney(env.playerMoney() + playerDelta)
            env.setEnemyMoney(env.enemyMoney() + enemyDelta)
        },
        presentationUnit = env.battlefield::presentationUnit,
        activeUnit = env.battlefield::activeUnit,
        onCompleted = { transaction ->
            if (env.getPendingActionTransaction() === transaction) env.setPendingActionTransaction(null)
        },
    )

    /** 지연 전투 행동: 실행 결과와 현재 전투 상태를 연결해 공통 후속 처리를 수행한다. */
    fun <T : TacticalActionResult> resolveDeferredAction(
        actorId: String,
        env: BattlePresentationEnvironment,
        resolve: () -> T,
    ): T {
        check(env.getPendingActionTransaction() == null) { "previous deferred battle action has not completed" }
        /**
         * `before` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val before = runtimeSnapshot(env)
        env.setStagedHitSideEffects(mutableListOf())
        env.setStagedCompletionSideEffects(mutableListOf())
        /**
         * `result` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val result = try {
            resolve()
        } catch (failure: Throwable) {
            env.setStagedHitSideEffects(null)
            env.setStagedCompletionSideEffects(null)
            restoreRuntime(before, env)
            throw failure
        }
        /**
         * `hitSideEffects` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val hitSideEffects = env.getStagedHitSideEffects().orEmpty().toList()
        /**
         * `completionSideEffects` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val completionSideEffects = env.getStagedCompletionSideEffects().orEmpty().toList()
        env.setStagedHitSideEffects(null)
        env.setStagedCompletionSideEffects(null)
        if (result is TacticalActionResult.Rejected) {
            restoreRuntime(before, env)
            return result
        }
        /**
         * `after` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val after = runtimeSnapshot(env)
        restoreRuntime(before, env)
        env.setPendingActionTransaction(
            createActionTransaction(
                actorId,
                before,
                after,
                hitSideEffects,
                completionSideEffects,
                env
            )
        )
        return result
    }

    /**
     * `moveUnitForPresentation`: 화면 표시 상태를 렌더링한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun moveUnitForPresentation(
        id: String,
        targetX: Int,
        targetY: Int,
        moveUnit: (String, Int, Int) -> TacticalActionResult,
        lastMovePath: (String) -> List<Pair<Int, Int>>,
        env: BattlePresentationEnvironment,
    ): Pair<TacticalActionResult, List<Pair<Int, Int>>> {
        /**
         * `path` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var path = emptyList<Pair<Int, Int>>()
        /**
         * `result` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val result = resolveDeferredAction(id, env) {
            moveUnit(id, targetX, targetY).also {
                if (it !is TacticalActionResult.Rejected) path = lastMovePath(id).toList()
            }
        }
        return result to path
    }

    /**
     * `attackForPresentation`: 화면 표시 상태를 렌더링한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun attackForPresentation(
        attackerId: String,
        targetId: String,
        attack: (String, String) -> TacticalActionResult,
        env: BattlePresentationEnvironment,
    ): TacticalActionResult =
        resolveDeferredAction(attackerId, env) { attack(attackerId, targetId) }

    /**
     * `castMagicForPresentation`: 화면 표시 상태를 렌더링한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun castMagicForPresentation(
        attackerId: String,
        targetId: String,
        magicId: Int,
        castMagic: (String, String, Int) -> TacticalActionResult,
        env: BattlePresentationEnvironment,
    ): TacticalActionResult =
        resolveDeferredAction(attackerId, env) { castMagic(attackerId, targetId, magicId) }

    /**
     * `usePropertyForPresentation`: 화면 표시 상태를 렌더링한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun usePropertyForPresentation(
        userId: String,
        targetId: String,
        itemId: Int,
        useProperty: (String, String, Int) -> TacticalActionResult,
        env: BattlePresentationEnvironment,
    ): TacticalActionResult =
        resolveDeferredAction(userId, env) { useProperty(userId, targetId, itemId) }

    /**
     * `hasPendingAiUnits`: 조건과 입력 상태를 검증한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun hasPendingAiUnits(
        isEnded: Boolean,
        activeFaction: Faction,
        units: Collection<BattleUnit>,
    ): Boolean = !isEnded && units.any {
        it.visible && it.effectiveFaction() == activeFaction && !it.hasActed
    }
}
