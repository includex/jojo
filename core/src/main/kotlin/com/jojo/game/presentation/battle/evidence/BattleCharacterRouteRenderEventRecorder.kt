// Battle
package com.jojo.game.presentation.battle.evidence

import com.jojo.game.presentation.battle.timeline.BattleCharacterDrawEvent
import com.jojo.game.presentation.battle.timeline.BattleCharacterStateRenderer
import com.jojo.game.presentation.battle.timeline.BattleCharacterStrictState

/** 전투 캐릭터 경로 증거 입력: 경로와 이미 계산된 그리기 샘플을 불변 목록으로 보관한다. */
internal data class BattleCharacterRouteRenderEventInput(
    val route: BattleCharacterStrictState,
    val samples: List<BattleCharacterRouteRenderEventSample>,
)

/** 캐릭터 경로 샘플: 한 캐릭터의 그리기 이벤트 순서를 외부 상태와 분리해 보관한다. */
internal data class BattleCharacterRouteRenderEventSample(
    val events: List<BattleCharacterDrawEvent>,
)

/** 전투 캐릭터 경로 증거 기록기: 배경과 샘플을 원본 순서의 JSONL로 직렬화한다. */
internal object BattleCharacterRouteRenderEventRecorder {
    private val mapEvent = BattleCharacterDrawEvent(
        nodePath = "Canvas/Layer/ScrollView/view/content/map",
        drawType = "sprite",
        x = -320f,
        y = -96f,
        width = 1920f,
        height = 1920f,
        assetFrameId = "assets/Game/native/4a/4afa0804-1ac2-4d59-97e4-1549a9425953.6295a.jpg#<unnamed-frame>",
        materialId = "builtin-2d-sprite (Instance)",
    )

    /** 기록: 맵 배경 뒤에 캐릭터 샘플을 입력 순서대로 붙여 JSONL을 반환한다. */
    fun jsonl(input: BattleCharacterRouteRenderEventInput): String =
        BattleCharacterStateRenderer.jsonl(
            input.route,
            buildList {
                add(mapEvent)
                input.samples.forEach { addAll(it.events) }
            },
        )
}
