package com.jojo.game

/** Immutable copy of one mutable FightUnit presentation node. */
internal data class FightFighterSnapshot(
    val characterId: Int?,
    val created: Boolean,
    val action: Int?,
    val actionElapsedSeconds: Float,
    val parentX: Float,
    val parentScaleX: Float,
    val zIndex: Int,
)

internal data class FightSpeechSnapshot(
    val active: Boolean,
    val renderedText: String,
)

/** Deep immutable rendering snapshot taken at one FightLayer presentation tick. */
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
    /**
     * 공개 메서드 `fighterAt`
     *
     * ### 파라미터
    - `slot` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `FightFighterView`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

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

/** Combines a pure state snapshot with identities resolved by BattleScreen. */
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
