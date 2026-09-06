// Battle
package com.jojo.game.presentation.battle.render

/** 전투 배우 레이어 후보: 화면에 올릴 유닛의 원본 위치와 정렬 기준을 보관한다. */
internal data class BattleActorLayerCandidate(
    val sourceIndex: Int,
    val characterId: Int?,
    val tileY: Int,
    val visible: Boolean,
)

/** 전투 배우 레이어 투영기: 실제 전투 상태를 그리기 순서의 유닛 식별자 목록으로 변환한다. */
internal object BattleActorLayerProjector {
    /** 배우 순서 계산: 일반 전투는 타일 Y축, 대화 혼합 장면은 원본 인물 우선순위로 정렬한다. */
    fun visibleSourceIndexes(
        candidates: List<BattleActorLayerCandidate>,
        dialogueBlendRoute: Boolean,
    ): List<Int> = candidates
        .asSequence()
        .filter(BattleActorLayerCandidate::visible)
        .sortedWith(
            if (dialogueBlendRoute) {
                compareBy<BattleActorLayerCandidate> { dialogueBlendOrder(it.characterId) }
            } else {
                compareBy(BattleActorLayerCandidate::tileY)
            }
        )
        .map(BattleActorLayerCandidate::sourceIndex)
        .toList()

    /** 대화 혼합 순서 계산: 원본 장면에 정의된 인물 순서를 사용하고 미지정 인물은 마지막에 배치한다. */
    private fun dialogueBlendOrder(characterId: Int?): Int = DIALOGUE_BLEND_CHARACTER_ORDER.indexOf(characterId)
        .takeIf { it >= 0 }
        ?: Int.MAX_VALUE

    /** 대화 혼합 인물 순서: 배경 합성 장면의 겹침 순서를 고정한다. */
    private val DIALOGUE_BLEND_CHARACTER_ORDER = listOf(
        480, 483, 484, 146, 147, 481, 482, 485, 478, 479,
        475, 476, 477, 235, 334, 474, 210, 234, 211,
    )
}
