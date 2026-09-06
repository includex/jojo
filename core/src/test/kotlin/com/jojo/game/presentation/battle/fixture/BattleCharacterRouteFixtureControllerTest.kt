// Battle Fixture Test
package com.jojo.game.presentation.battle.fixture

import com.jojo.game.presentation.battle.timeline.BattleCharacterMaterial
import com.jojo.game.presentation.battle.timeline.BattleCharacterPresentation
import com.jojo.game.presentation.battle.timeline.BattleCharacterStrictState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** 전투 캐릭터 경로 fixture 검증: 경로별 유닛, 표시 상태, 좌표 및 asset 계획이 원본 캡처 계약을 보존하는지 확인한다. */
class BattleCharacterRouteFixtureControllerTest {
    /** 경로별 샘플 계획: 여섯 캐릭터 경로가 원본 순서와 캐릭터 ID를 유지하는지 검증한다. */
    @Test
    fun `경로별 캐릭터 순서를 선택한다`() {
        val expected = mapOf(
            BattleCharacterStrictState.HP_CAMPS_PARTIAL to listOf(235, 210, 234, 211),
            BattleCharacterStrictState.OUTLINE_HIGHLIGHT to listOf(210, 211),
            BattleCharacterStrictState.HIT_IMPACT to listOf(210),
            BattleCharacterStrictState.CLEANUP to listOf(210),
            BattleCharacterStrictState.DEATH_ACTION to listOf(210),
            BattleCharacterStrictState.DEATH_HIDDEN to listOf(210),
        )

        expected.forEach { (route, characterIds) ->
            val actual = BattleCharacterRouteFixtureController().install(route) { it.characterId }
            assertEquals(characterIds, actual)
        }
    }

    /** HP 진영 계획: 네 진영의 HP, 위치, 프레임 방향 및 asset 선택을 정확히 보존한다. */
    @Test
    fun `HP 진영 샘플의 렌더 계획을 보존한다`() {
        val samples = BattleCharacterRouteFixtureController()
            .install(BattleCharacterStrictState.HP_CAMPS_PARTIAL) { it }
        assertNotNull(samples)

        assertEquals(
            listOf(
                "235:FAMOUS_ENEMY:96:29:352.0:192.0:3:67186736",
                "210:MINE:119:89:640.0:96.0:0:33632304",
                "234:ENEMY:96:43:832.0:96.0:3:67186736",
                "211:FRIEND:119:71:544.0:0.0:0:33632304",
            ),
            samples.map { sample ->
                listOf(
                    sample.characterId,
                    sample.presentation.camp,
                    sample.presentation.maxHp,
                    sample.presentation.hp,
                    sample.unitLeft,
                    sample.unitBottom,
                    sample.frameDirection,
                    sample.assetFrameId.substringAfterLast('#'),
                ).joinToString(":")
            },
        )
    }

    /** 강조 및 피격 계획: 확대 frame, offset, 피해 label 영역과 material 전이를 원본 값으로 재현한다. */
    @Test
    fun `강조와 피격 샘플의 표시 상태를 생성한다`() {
        val outlineSamples = BattleCharacterRouteFixtureController()
            .install(BattleCharacterStrictState.OUTLINE_HIGHLIGHT) { it }
        assertNotNull(outlineSamples)
        assertEquals(listOf(128f, 128f), outlineSamples.map { it.avatarWidth })
        assertEquals(listOf(16f, 16f), outlineSamples.map { it.avatarOffsetX })

        val outline = outlineSamples[0].presentation.create("outline")
        val highlight = outlineSamples[1].presentation.create("highlight")
        assertEquals(BattleCharacterPresentation.GONG_JI2, outline.action)
        assertEquals(BattleCharacterMaterial.OUTLINE, outline.material)
        assertEquals(BattleCharacterMaterial.HIGHLIGHT, highlight.material)
        assertEquals(1.6f, highlight.materialValue)

        val hitSample = BattleCharacterRouteFixtureController()
            .install(BattleCharacterStrictState.HIT_IMPACT) { it }
            ?.single()
        assertNotNull(hitSample)
        assertEquals(BattleCharacterFixtureBounds(611.3f, 159.76f, 57.4f, 64.48f), hitSample.harmBounds)
        assertEquals("50475056", hitSample.assetFrameId)
        val hit = hitSample.presentation.create("hit")
        assertEquals(83, hit.hp)
        assertEquals(30, hit.harm?.value)
        assertEquals(BattleCharacterMaterial.HIGHLIGHT, hit.material)
        assertEquals(1f, hit.materialValue)
    }

    /** 정리 및 사망 계획: 피격 정리와 사망 표시·숨김의 최종 프레젠테이션 상태를 구분한다. */
    @Test
    fun `정리와 사망 단계의 최종 상태를 생성한다`() {
        val cleanup = presentation(BattleCharacterStrictState.CLEANUP)
        assertEquals(83, cleanup.hp)
        assertEquals(BattleCharacterPresentation.STAND, cleanup.action)
        assertNull(cleanup.harm)

        val deathAction = presentation(BattleCharacterStrictState.DEATH_ACTION)
        assertEquals(0, deathAction.hp)
        assertEquals(BattleCharacterPresentation.DEATH, deathAction.action)
        assertTrue(deathAction.visible)
        assertFalse(deathAction.infoVisible)

        val deathHidden = presentation(BattleCharacterStrictState.DEATH_HIDDEN)
        assertEquals(119, deathHidden.hp)
        assertFalse(deathHidden.visible)
        assertEquals(1, deathHidden.retreatCount)
    }

    /** 설치 수명 주기: null 경로를 무시하고 최초 유효 경로만 화면 어댑터에 전달한다. */
    @Test
    fun `fixture를 한 번만 설치한다`() {
        val controller = BattleCharacterRouteFixtureController()
        var adapted = 0

        assertNull(controller.install<Int>(null) { adapted++ })
        assertNotNull(controller.install(BattleCharacterStrictState.HIT_IMPACT) { adapted++ })
        assertNull(controller.install(BattleCharacterStrictState.DEATH_ACTION) { adapted++ })
        assertEquals(1, adapted)
    }

    /** 단일 프레젠테이션 생성: 지정 경로의 유일한 샘플을 테스트용 유닛 ID와 결합한다. */
    private fun presentation(route: BattleCharacterStrictState): BattleCharacterPresentation =
        requireNotNull(BattleCharacterRouteFixtureController().install(route) { it.presentation.create("unit") }).single()
}
