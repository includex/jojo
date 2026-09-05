package com.jojo.game
import com.jojo.game.domain.battle.Battlefield

/** An original StageLayer script can also suspend for a timed visual cue. */
/** A source StageLayer may pause its Python Script while a native modal owns input. */
enum class PlaybackState { DIALOGUE, CHOICE, DELAY, MODAL, COMPLETE }

/**
 * data class  `TacticalUnit`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

data class TacticalUnit(
    val id: Int,
    var x: Int,
    var y: Int,
    var direction: Int = 0,
    var action: Int = 0,
    var visible: Boolean = true,
    var posts: Int = 0,
    var ai: Int = 0,
    var aiTargetId: Int = -1,
    var aiTargetX: Int = 0,
    var aiTargetY: Int = 0,
) {
    var visualX: Float = x.toFloat()
    var visualY: Float = y.toFloat()
    var moveFromX: Float = visualX
    var moveFromY: Float = visualY
    var moveElapsed: Float = 0f

    /** Time since the current HallUnit animation/direction clip began. */
    var animationElapsed: Float = 0f

    /** HallUnit._moveDuring samples the tween for z-order every 40 ms. */
    var moveZIndex: Float = 4f * (x + y) - 424f
    var moveDuration: Float = 0f
    var movePath: List<Pair<Int, Int>> = emptyList()

    /** HallUnit/BattleUnit commit their source tile only in move2's final callback. */
    var moveToX: Int = x
    var moveToY: Int = y
    var moveFinalDirection: Int = direction

    /** BattleUnit.move2 exposes its initial direction/action for one render before tween time advances. */
    var moveJustStarted: Boolean = false
}

/**
 * enum class  `ScenarioUnitFaction`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

enum class ScenarioUnitFaction { FRIEND, ENEMY, MINE }

/**
 * data class  `ScenarioBattleUnit`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

data class ScenarioBattleUnit(
    val instanceId: Int,
    val characterId: Int,
    val faction: ScenarioUnitFaction,
    val x: Int,
    val y: Int,
    /** Cocos keeps the prefab node anchor when an authored coordinate is absent. */
    val authoredX: Boolean = true,
    val authoredY: Boolean = true,
    /** BattleScreen.createFriend/createEnemy default direction is down (2). */
    val direction: Int = 2,
    val level: Int = 0,
    /** Enemy row `yj != 0`: original BATTLE_CAMP.REINFORCEMENTS. */
    val reinforcement: Boolean = false,
    var hidden: Boolean = false,
    var ai: Int = 0,
    var aiTargetId: Int = -1,
    var aiTargetX: Int = 0,
    var aiTargetY: Int = 0,
    /** BATTLE_UNIT_FALG.DEATH_MSG; createMine enables it by default. */
    var deathMessageEnabled: Boolean = faction == ScenarioUnitFaction.MINE,
    /** Stable battle-instance slot, distinct from the camp-local instance ID. */
    val battleSlot: Int = BattleSlotLayout.slotFor(faction, instanceId),
)

val ScenarioBattleUnit.battleId: String
    get() = BattleSlotLayout.battleId(faction, battleSlot)

val ScenarioBattleUnit.stageKey: String
    get() = BattleSlotLayout.stageKey(faction, battleSlot)

/** One authored Stage.setObjects/setObject invocation, before presentation filtering. */
data class ScenarioMapObjectsCall(
    val enabled: Boolean,
    val terrainId: Int,
    /** Source argument order is significant when several doors form one callback. */
    val objects: List<Object>,
) {
    /**
     * data class  `Object`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Object(val objectId: Int, val x: Int, val y: Int)
}

/**
 * data class  `ScriptedAttackAction`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

data class ScriptedAttackAction(val attackerId: Int, val targetId: Int, val flag: Int)

/**
 * data class  `ScenarioJoinBattleLimit`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

data class ScenarioJoinBattleLimit(
    val minimum: Int,
    val maximum: Int,
    val requiredUnitIds: List<Int>,
    val excludedUnitIds: List<Int>
)

/**
 * HallLayer._startBattle's resolved view of an authored setJoinBattle call.
 * The script arguments remain available on [ScenarioStage], while this value
 * captures the extra rules applied immediately before StartBattleScreen opens.
 */
/**
 * data class  `ScenarioBattleEntryPlan`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

data class ScenarioBattleEntryPlan(
    val selectionLimit: ScenarioJoinBattleLimit,
    /** Non-null when HallLayer bypasses StartBattleScreen altogether. */
    val directBattleRoster: List<Int>?,
)

/**
 * data class  `ScenarioJoinEquipment`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

data class ScenarioJoinEquipment(
    val unitId: Int,
    val weapon: Int,
    val weaponLevel: Int,
    val armor: Int,
    val armorLevel: Int,
    val auxiliary: Int
)

/** Arguments retained from BattleScreen.reward(t, items, end). */
data class ScenarioRewardRequest(
    val bonusMoney: Int = 0,
    /** Alternating item id / level entries, exactly as authored by S_*.py. */
    val items: List<Int> = emptyList(),
    val end: Boolean = false,
)

/**
 * data class  `ScenarioUnitHideRequest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

data class ScenarioUnitHideRequest(
    val unitId: Int,
    val hideType: Int,
    /** Exact BattleUnit instance selected by searchUnitByRect; null for unit().hide. */
    val battleUnitId: String? = null,
    /** ctrlUnitHide resumes the Python helper only after the final sorted unit. */
    val resumesScript: Boolean = true,
    /** BAI_TUI checks the retire line before a master upgrades the shared type to death. */
    val showsRetireMessage: Boolean = hideType == 1,
)

/**
 * data class  `ScenarioUnitShowRequest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

data class ScenarioUnitShowRequest(
    val unitId: Int,
    val x: Int = -1,
    val y: Int = -1,
    val direction: Int = -1,
    val flags: Int = 0,
)

/**
 * BattleUnit-only tail of `setPosts`: model data is already written before
 * this is queued.  The renderer keeps the old avatar visible until its
 * asynchronous picture completion, and resumes the Python script only for
 * the source `flags & 16` branch.
 */
/**
 * data class  `ScenarioUnitPostsRequest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

data class ScenarioUnitPostsRequest(
    val unitId: Int,
    val oldAvatarId: Int,
    val newAvatarId: Int,
    val pausesScript: Boolean,
)

/** A portrait explicitly positioned by an original event script. */
data class ScenarioHead(val characterId: Int, var x: Int = 0, var y: Int = 0, var visible: Boolean = true) {
    var visualX: Float = x.toFloat()
    var visualY: Float = y.toFloat()
    var moveFromX: Float = visualX
    var moveFromY: Float = visualY
    var moveElapsed: Float = 0f
    var moveDuration: Float = 0f
    var opacity: Float = 1f
    var fadeFrom: Float = opacity
    var fadeTo: Float = opacity
    var fadeElapsed: Float = 0f
    var fadeDuration: Float = 0f
}

/**
 * data class  `ScenarioSoundEffect`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

data class ScenarioSoundEffect(val soundId: Int, val mode: Int)

/** Camera/barrier side effect of BattleScreen.setObject2/playMagicMeff. */
data class ScenarioMapPresentationRequest(
    val x: Int,
    val y: Int,
    val duration: Float,
    val magicCallId: Int? = null,
)

/** Synchronous BattleScreen.center request; repeated equal coordinates are distinct dispatches. */
data class ScenarioCameraCenterRequest(val x: Int, val y: Int)

/**
 * Source BattleScreen calls whose model mutation is not their complete contract.
 *
 * These requests deliberately retain the authored callback boundary.  The AST
 * may enqueue them before the renderer-side consumer is installed, so this is
 * a FIFO rather than a single mutable slot: collapsing two consecutive
 * `unit.heightLight()` calls (S_00 does exactly that) loses a visible episode.
 */
/**
 * sealed class  `ScenarioScriptPresentationRequest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

sealed class ScenarioScriptPresentationRequest {
    /**
     * data class  `RectangleHighlight`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class RectangleHighlight(
        val x1: Int,
        val y1: Int,
        val x2: Int,
        val y2: Int,
        val durationSeconds: Float = 2.4f,
    ) : ScenarioScriptPresentationRequest()

    /**
     * data class  `UnitHighlight`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class UnitHighlight(
        val unitId: Int,
        /** Unit highlight also opens BattleUnitInfoLayer until its callback. */
        val opensUnitInfo: Boolean = true,
        val durationSeconds: Float = 2.4f,
    ) : ScenarioScriptPresentationRequest()

    /**
     * data class  `GetItem`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class GetItem(
        val itemId: Int,
        val suppliedCountOrLevel: Int,
        val addToInventory: Boolean,
        /** BattleScreen._filterUnit selector (1025 mine, 1027 main actor, or character id). */
        val unitSelector: Int,
        val action: Int,
        val completionMessage: String,
    ) : ScenarioScriptPresentationRequest()

    /**
     * data class  `MapObjects`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class MapObjects(
        val enabled: Boolean,
        val terrainId: Int,
        val objects: List<Object>,
        /** setObjects plays the gate/fire sound only for its first entry. */
        val soundOnFirstObjectOnly: Boolean,
        /** Fire/boat load holds 1 s; a gate open/close tween holds 3.5 s. */
        val durationSeconds: Float = if (objects.any { it.objectId >= 4 }) 3.5f else 1f,
    ) : ScenarioScriptPresentationRequest() {
        /**
         * data class  `Object`
         *
         * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
         *
         * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
         */

        data class Object(val objectId: Int, val x: Int, val y: Int)
    }

    /**
     * data class  `UnitStatusSettlement`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class UnitStatusSettlement(
        val values: List<Map<String, Any?>>,
        /** jiesuan adds a final .1 s callback after its visible operations. */
        val minimumDurationSeconds: Float = .1f,
    ) : ScenarioScriptPresentationRequest()
}

/**
 * Ordered commands sent by the recovered battle script to FightLayer.
 *
 * The booleans deliberately keep the source API's meaning.  Most FightLayer
 * methods select the acting side with `mine`, while death selects the unit to
 * remove with `enemy`.  Conflating the latter with the former reverses the
 * loser in S_01's Guan Yu/Hua Xiong duel.
 */
/**
 * sealed class  `ScenarioFightCommand`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

sealed class ScenarioFightCommand(open val fightId: Long) {
    /**
     * data class  `Start`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Start(
        override val fightId: Long,
        val firstUnitId: Int,
        val secondUnitId: Int,
        val backgroundIndex: Int,
        val previousBackgroundSound: Int,
    ) : ScenarioFightCommand(fightId)

    /**
     * data class  `ShowUnit`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class ShowUnit(
        override val fightId: Long,
        val mine: Boolean,
        val text: String,
        val entryAction: Int,
    ) : ScenarioFightCommand(fightId)

    /**
     * data class  `ShowStart`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class ShowStart(override val fightId: Long) : ScenarioFightCommand(fightId)

    /**
     * data class  `SetAction`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class SetAction(override val fightId: Long, val mine: Boolean, val action: Int) : ScenarioFightCommand(fightId)

    /**
     * data class  `Say`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Say(
        override val fightId: Long,
        val mine: Boolean,
        val text: String,
        /** Authored third argument; FightLayer currently ignores it. */
        val flag: Boolean,
    ) : ScenarioFightCommand(fightId)

    /**
     * data class  `Attack2`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Attack2(
        override val fightId: Long,
        val mine: Boolean,
        val style: Int,
        val defended: Boolean,
    ) : ScenarioFightCommand(fightId)

    /**
     * FightLayer.attack1: the final boolean selects the charged attacker
     * animation and the heavy/light reaction sound; it is not a defend flag.
     */
    /**
     * data class  `Attack1`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Attack1(
        override val fightId: Long,
        val mine: Boolean,
        val style: Int,
        val critical: Boolean,
    ) : ScenarioFightCommand(fightId)

    /**
     * data class  `Death`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Death(override val fightId: Long, val enemy: Boolean) : ScenarioFightCommand(fightId)

    /**
     * data class  `End`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class End(override val fightId: Long) : ScenarioFightCommand(fightId)
}
