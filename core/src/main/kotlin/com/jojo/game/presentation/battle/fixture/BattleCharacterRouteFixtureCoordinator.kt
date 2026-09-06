// Battle Fixture
package com.jojo.game.presentation.battle.fixture

import com.jojo.game.domain.battle.BattleUnit
import com.jojo.game.presentation.battle.evidence.BattleCharacterRouteRenderEventInput
import com.jojo.game.presentation.battle.evidence.BattleCharacterRouteRenderEventRecorder
import com.jojo.game.presentation.battle.evidence.BattleCharacterRouteRenderEventSample
import com.jojo.game.presentation.battle.timeline.BattleCharacterDrawEvent
import com.jojo.game.presentation.battle.timeline.BattleCharacterStateRenderer
import com.jojo.game.presentation.battle.timeline.BattleCharacterStrictState
import com.jojo.game.presentation.battle.unit.UnitSpriteFrame

/** 전투 캐릭터 fixture 화면 포트: 경로 계획이 필요한 실제 유닛과 스프라이트 프레임만 조회한다. */
internal interface BattleCharacterRouteFixturePort {
    /** 유닛 조회: 계획의 캐릭터 ID에 대응하는 실제 전장 유닛을 반환한다. */
    fun unit(characterId: Int): BattleUnit?

    /** 스프라이트 프레임 조회: 지정 동작·방향·시각에 해당하는 atlas 프레임을 반환한다. */
    fun spriteFrame(action: Int, direction: Int, elapsed: Float, loop: Boolean): UnitSpriteFrame?

    /** 대기 프레임 조회: 지정 동작 프레임이 없을 때 사용할 실제 유닛의 기본 프레임을 반환한다. */
    fun idleSpriteFrame(unit: BattleUnit): UnitSpriteFrame
}

/** 전투 캐릭터 fixture 조정자: 경로 설치, 그리기 명령 계산, 증거 JSONL 조립을 화면 렌더링과 분리한다. */
internal class BattleCharacterRouteFixtureCoordinator(
    private val controller: BattleCharacterRouteFixtureController = BattleCharacterRouteFixtureController(),
) {
    private var samples: List<BattleCharacterRouteSample> = emptyList()

    /** 경로 설치: 최초 유효 경로의 샘플 계획을 화면 포트가 제공한 실제 유닛과 결합한다. */
    fun install(route: BattleCharacterStrictState?, port: BattleCharacterRouteFixturePort) {
        controller.install(route) { fixture ->
            val unit = requireNotNull(port.unit(fixture.characterId)) {
                "battle-character fixture requires source unit ${fixture.characterId}"
            }
            val harmBounds = fixture.harmBounds
            BattleCharacterRouteSample(
                unit = unit,
                state = fixture.presentation.create(unit.id),
                unitLeft = fixture.unitLeft,
                unitBottom = fixture.unitBottom,
                frameTime = fixture.frameTime,
                assetFrameId = fixture.assetFrameId,
                avatarWidth = fixture.avatarWidth,
                avatarHeight = fixture.avatarHeight,
                avatarOffsetX = fixture.avatarOffsetX,
                avatarOffsetY = fixture.avatarOffsetY,
                harmRect = harmBounds?.let { floatArrayOf(it.x, it.y, it.width, it.height) },
                frameDirection = fixture.frameDirection,
            )
        }?.let { samples = it }
    }

    /** 그리기 샘플: 각 fixture의 선택 프레임과 avatar·HP·피해 숫자 명령을 순서대로 계산한다. */
    fun drawSamples(port: BattleCharacterRouteFixturePort): List<BattleCharacterRouteDrawSample> = samples.map { sample ->
        val frame = port.spriteFrame(
            sample.state.action, sample.frameDirection, sample.frameTime, loop = sample.state.action == 0,
        ) ?: port.idleSpriteFrame(sample.unit)
        BattleCharacterRouteDrawSample(sample, frame, commands(sample, frame))
    }

    /** 증거 JSONL: 현재 경로의 계산된 그리기 명령을 원본 맵 배경 뒤에 결합해 직렬화한다. */
    fun jsonl(route: BattleCharacterStrictState, port: BattleCharacterRouteFixturePort): String =
        BattleCharacterRouteRenderEventRecorder.jsonl(
            BattleCharacterRouteRenderEventInput(
                route = route,
                samples = drawSamples(port).map { sample -> BattleCharacterRouteRenderEventSample(sample.commands) },
            ),
        )

    /** 그리기 명령 계산: sprite frame의 atlas crop과 fixture 전용 피해 숫자 영역을 표시 상태 명령으로 변환한다. */
    private fun commands(sample: BattleCharacterRouteSample, frame: UnitSpriteFrame): List<BattleCharacterDrawEvent> =
        BattleCharacterStateRenderer.commands(
            sample.state, sample.unitLeft, sample.unitBottom, sample.assetFrameId,
            avatarWidth = sample.avatarWidth, avatarHeight = sample.avatarHeight,
            avatarOffsetX = sample.avatarOffsetX, avatarOffsetY = sample.avatarOffsetY,
            avatarSourceRect = listOf(0, frame.sourceY, frame.sourceWidth, frame.sourceHeight),
            avatarFlipX = frame.flipX, avatarFlipY = false,
        ).map { event ->
            if (event.drawType == "label" && sample.harmRect != null) event.copy(
                x = sample.harmRect[0], y = sample.harmRect[1], width = sample.harmRect[2], height = sample.harmRect[3],
                blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
            ) else event
        }
}

/** 전투 캐릭터 fixture 그리기 샘플: 실제 draw primitive가 사용할 유닛, 스프라이트 프레임, 표시 명령을 묶는다. */
internal data class BattleCharacterRouteDrawSample(
    /** 실제 전투 샘플: texture 선택과 표시 material 판단에 사용할 유닛·상태다. */
    val sample: BattleCharacterRouteSample,
    /** 선택 프레임: atlas texture crop과 좌우 반전에 사용할 스프라이트 영역이다. */
    val frame: UnitSpriteFrame,
    /** 표시 명령: avatar 뒤의 HP bar·피해 숫자 그리기 순서를 정의한다. */
    val commands: List<BattleCharacterDrawEvent>,
)
