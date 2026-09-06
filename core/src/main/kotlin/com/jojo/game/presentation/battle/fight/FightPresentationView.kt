// Battle
package com.jojo.game.presentation.battle.fight

import com.jojo.game.presentation.battle.*
internal data class FightFighterSnapshot(
    val characterId: Int?,
    val created: Boolean,
    val action: Int?,
    val actionElapsedSeconds: Float,
    val parentX: Float,
    val parentScaleX: Float,
    val zIndex: Int,
)

/** FightSpeechSnapshot: 전투 화면에 전달할 불변 표시 상태를 보관한다. */
internal data class FightSpeechSnapshot(
    val active: Boolean,
    val renderedText: String,
)
internal data class FightPresentationSnapshot(
    val backgroundIndex: Int,
    val introBackgroundActive: Boolean,
    val duelBackgroundActive: Boolean,
    val startRevealGroup: Int,
    val startCrossFade: Float,
    val startLabelsActive: Boolean,
    val mineIndex: Int,
    val mine: FightFighterSnapshot,
    val enemy: FightFighterSnapshot,
    val mineSpeech: FightSpeechSnapshot,
    val enemySpeech: FightSpeechSnapshot,
)
internal data class FightUnitRenderIdentity(
    val name: String?,
    val introName: String?,
    val portraitFaceId: Int?,
    val avatarId: Int?,
)

/** FightFighterView: 전투 화면에 전달할 불변 표시 상태를 보관한다. */
internal data class FightFighterView(
    val side: FightSide,
    val slot: Int,
    val name: String?,
    val introName: String?,
    val portraitFaceId: Int?,
    val avatarId: Int?,
    val created: Boolean,
    val action: Int?,
    val actionElapsedSeconds: Float,
    val parentX: Float,
    val parentScaleX: Float,
    val zIndex: Int,
    val speech: FightSpeechSnapshot,
)

/** FightPresentationView: 전투 화면에 전달할 불변 표시 상태를 보관한다. */
internal data class FightPresentationView(
    val backgroundIndex: Int,
    val introBackgroundActive: Boolean,
    val duelBackgroundActive: Boolean,
    val startRevealGroup: Int,
    val startCrossFade: Float,
    val startLabelsActive: Boolean,
    val mine: FightFighterView,
    val enemy: FightFighterView,
) {

    fun fighterAt(slot: Int): FightFighterView = when (slot) {
        mine.slot -> mine
        enemy.slot -> enemy
        else -> error("FightLayer slot must be 0 or 1: $slot")
    }
}

internal fun FightPresentationState.renderSnapshot(): FightPresentationSnapshot = FightPresentationSnapshot(
    backgroundIndex = backgroundIndex,
    introBackgroundActive = introBackgroundActive,
    duelBackgroundActive = duelBackgroundActive,
    startRevealGroup = startRevealGroup,
    startCrossFade = startCrossFade,
    startLabelsActive = startLabelsActive,
    mineIndex = mineIndex,
    mine = mine.renderSnapshot(),
    enemy = enemy.renderSnapshot(),
    mineSpeech = mineSpeech.renderSnapshot(),
    enemySpeech = enemySpeech.renderSnapshot(),
)

private fun FightUnitPresentation.renderSnapshot() = FightFighterSnapshot(
    characterId = characterId,
    created = created,
    action = action,
    actionElapsedSeconds = actionElapsedSeconds,
    parentX = parentX,
    parentScaleX = parentScaleX,
    zIndex = zIndex,
)

private fun FightSpeechPresentation.renderSnapshot() = FightSpeechSnapshot(
    active = active,
    renderedText = renderedText,
)
internal object FightPresentationViewBuilder {
    fun build(
        snapshot: FightPresentationSnapshot,
        mineIdentity: FightUnitRenderIdentity,
        enemyIdentity: FightUnitRenderIdentity,
    ): FightPresentationView {
        require(snapshot.mineIndex in 0..1) { "FightLayer mine slot must be 0 or 1: ${snapshot.mineIndex}" }
        fun fighter(
            side: FightSide,
            slot: Int,
            source: FightFighterSnapshot,
            identity: FightUnitRenderIdentity,
            speech: FightSpeechSnapshot,
        ) = FightFighterView(
            side = side,
            slot = slot,
            name = identity.name,
            introName = identity.introName,
            portraitFaceId = identity.portraitFaceId,
            avatarId = identity.avatarId,
            created = source.created,
            action = source.action,
            actionElapsedSeconds = source.actionElapsedSeconds,
            parentX = source.parentX,
            parentScaleX = source.parentScaleX,
            zIndex = source.zIndex,
            speech = speech,
        )
        return FightPresentationView(
            backgroundIndex = snapshot.backgroundIndex,
            introBackgroundActive = snapshot.introBackgroundActive,
            duelBackgroundActive = snapshot.duelBackgroundActive,
            startRevealGroup = snapshot.startRevealGroup,
            startCrossFade = snapshot.startCrossFade,
            startLabelsActive = snapshot.startLabelsActive,
            mine = fighter(FightSide.MINE, snapshot.mineIndex, snapshot.mine, mineIdentity, snapshot.mineSpeech),
            enemy = fighter(
                FightSide.ENEMY,
                if (snapshot.mineIndex == 0) 1 else 0,
                snapshot.enemy,
                enemyIdentity,
                snapshot.enemySpeech,
            ),
        )
    }
}
