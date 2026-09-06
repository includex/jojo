// Battle Fixture
package com.jojo.game.presentation.battle.fixture

import com.jojo.game.presentation.battle.timeline.BattleCharacterCamp
import com.jojo.game.presentation.battle.timeline.BattleCharacterPresentation
import com.jojo.game.presentation.battle.timeline.BattleCharacterStrictState
import com.jojo.game.presentation.battle.timeline.BattleHideType

/** 전투 캐릭터 경로 fixture 조정기: 캡처 경로별 표시 계획을 한 번만 선택하고 화면 어댑터로 실객체를 조립한다. */
internal class BattleCharacterRouteFixtureController {
    /** 설치 여부: 렌더 프레임마다 같은 캐릭터 fixture가 다시 생성되는 것을 막는다. */
    private var installed = false

    /** 경로 설치: 선택한 경로의 불변 샘플 계획을 화면이 제공한 어댑터로 변환한다. */
    fun <T> install(
        route: BattleCharacterStrictState?,
        adapter: (BattleCharacterFixtureSamplePlan) -> T,
    ): List<T>? {
        if (route == null || installed) return null
        installed = true
        return plan(route).samples.map(adapter)
    }

    /** 경로 계획 선택: 캐릭터 상태 검증 경로에 대응하는 샘플 순서와 표시 값을 반환한다. */
    private fun plan(route: BattleCharacterStrictState): BattleCharacterRouteFixturePlan =
        BattleCharacterRouteFixturePlan(
            route = route,
            samples = when (route) {
                BattleCharacterStrictState.HP_CAMPS_PARTIAL -> listOf(
                    sample(235, BattleCharacterCamp.FAMOUS_ENEMY, 96, 29, 352f, 192f, MOVEMENT_20),
                    sample(210, BattleCharacterCamp.MINE, 119, 89, 640f, 96f, MOVEMENT_11, frameDirection = 0),
                    sample(234, BattleCharacterCamp.ENEMY, 96, 43, 832f, 96f, MOVEMENT_20),
                    sample(211, BattleCharacterCamp.FRIEND, 119, 71, 544f, 0f, MOVEMENT_11, frameDirection = 0),
                )

                BattleCharacterStrictState.OUTLINE_HIGHLIGHT -> listOf(
                    sample(
                        210,
                        BattleCharacterCamp.MINE,
                        119,
                        119,
                        640f,
                        96f,
                        ATTACK_FRAME,
                        presentationStage = BattleCharacterFixturePresentationStage.ATTACK_OUTLINE,
                        width = 128f,
                        height = 128f,
                        offsetX = 16f,
                    ),
                    sample(
                        211,
                        BattleCharacterCamp.MINE,
                        119,
                        119,
                        544f,
                        0f,
                        ATTACK_FRAME,
                        presentationStage = BattleCharacterFixturePresentationStage.ATTACK_HIGHLIGHT,
                        width = 128f,
                        height = 128f,
                        offsetX = 16f,
                    ),
                )

                BattleCharacterStrictState.HIT_IMPACT -> listOf(
                    sample(
                        210,
                        BattleCharacterCamp.ENEMY,
                        119,
                        113,
                        640f,
                        96f,
                        HIT_FRAME,
                        presentationStage = BattleCharacterFixturePresentationStage.HIT_IMPACT,
                        offsetX = 16f,
                        harmBounds = BattleCharacterFixtureBounds(611.3f, 159.76f, 57.4f, 64.48f),
                    ),
                )

                BattleCharacterStrictState.CLEANUP -> listOf(
                    sample(
                        210,
                        BattleCharacterCamp.ENEMY,
                        119,
                        113,
                        640f,
                        96f,
                        MOVEMENT_11,
                        presentationStage = BattleCharacterFixturePresentationStage.HIT_CLEANUP,
                        frameDirection = 0,
                    ),
                )

                BattleCharacterStrictState.DEATH_ACTION -> listOf(
                    sample(
                        210,
                        BattleCharacterCamp.ENEMY,
                        119,
                        119,
                        640f,
                        96f,
                        DEATH_FRAME,
                        presentationStage = BattleCharacterFixturePresentationStage.DEATH_ACTION,
                    ),
                )

                BattleCharacterStrictState.DEATH_HIDDEN -> listOf(
                    sample(
                        210,
                        BattleCharacterCamp.ENEMY,
                        119,
                        119,
                        640f,
                        96f,
                        MOVEMENT_11,
                        presentationStage = BattleCharacterFixturePresentationStage.DEATH_HIDDEN,
                    ),
                )
            },
        )

    /** 샘플 계획 생성: 반복되는 기본 프레임 크기와 애니메이션 시각을 적용해 한 캐릭터 계획을 만든다. */
    private fun sample(
        characterId: Int,
        camp: BattleCharacterCamp,
        maxHp: Int,
        hp: Int,
        unitLeft: Float,
        unitBottom: Float,
        assetFrameId: String,
        presentationStage: BattleCharacterFixturePresentationStage = BattleCharacterFixturePresentationStage.IDLE,
        frameTime: Float = .1f,
        width: Float = 96f,
        height: Float = 96f,
        offsetX: Float = 0f,
        offsetY: Float = 0f,
        harmBounds: BattleCharacterFixtureBounds? = null,
        frameDirection: Int = 3,
    ) = BattleCharacterFixtureSamplePlan(
        characterId = characterId,
        presentation = BattleCharacterFixturePresentationPlan(camp, maxHp, hp, presentationStage),
        unitLeft = unitLeft,
        unitBottom = unitBottom,
        frameTime = frameTime,
        assetFrameId = assetFrameId,
        avatarWidth = width,
        avatarHeight = height,
        avatarOffsetX = offsetX,
        avatarOffsetY = offsetY,
        harmBounds = harmBounds,
        frameDirection = frameDirection,
    )

    private companion object {
        /** 아군 이동 프레임: 기본 자세와 정리 경로가 사용하는 원본 atlas 식별자다. */
        const val MOVEMENT_11 = "assets/Game/native/19/19ac1287-4d09-45f4-bf9a-f5eb8b21795c.89d84.png#33632304"

        /** 적군 이동 프레임: HP 진영 경로에서 적 캐릭터가 사용하는 원본 atlas 식별자다. */
        const val MOVEMENT_20 = "assets/Game/native/3f/3f8fbf89-4dd0-4d0b-88e0-9c7927fe5693.3f9c2.png#67186736"

        /** 공격 프레임: 윤곽선과 피격 강조 전 상태를 비교하는 원본 atlas 식별자다. */
        const val ATTACK_FRAME = "assets/Game/native/dc/dcad67fe-5825-49d1-b6e2-ce5356f376e4.b8507.png#134234176"

        /** 피격 프레임: 피해 숫자와 강조 material을 함께 검증하는 프레임 식별자다. */
        const val HIT_FRAME = "50475056"

        /** 사망 프레임: 사망 동작이 끝나기 전 표시 상태를 검증하는 원본 atlas 식별자다. */
        const val DEATH_FRAME = "assets/Game/native/19/19ac1287-4d09-45f4-bf9a-f5eb8b21795c.89d84.png#151072816"
    }
}

/** 전투 캐릭터 경로 계획: 선택한 캡처 경로와 렌더 순서를 보존하는 캐릭터 샘플 목록이다. */
internal data class BattleCharacterRouteFixturePlan(
    /** 캡처 경로: 샘플 목록이 증명하는 캐릭터 표시 상태다. */
    val route: BattleCharacterStrictState,
    /** 샘플 계획: 화면이 원본 순서대로 실객체로 변환해야 하는 표시 항목이다. */
    val samples: List<BattleCharacterFixtureSamplePlan>,
)

/** 전투 캐릭터 샘플 계획: 실전 유닛과 무관한 캐릭터 ID, 표시 상태, 좌표 및 asset 선택을 정의한다. */
internal data class BattleCharacterFixtureSamplePlan(
    /** 캐릭터 ID: 화면 어댑터가 실제 전투 유닛을 찾을 때 사용하는 데이터 ID다. */
    val characterId: Int,
    /** 표시 계획: HP 진영과 애니메이션 단계로 캐릭터 프레젠테이션을 생성한다. */
    val presentation: BattleCharacterFixturePresentationPlan,
    /** 유닛 왼쪽 좌표: 캡처 화면에서 캐릭터 avatar가 시작하는 X 위치다. */
    val unitLeft: Float,
    /** 유닛 아래 좌표: 캡처 화면에서 캐릭터 avatar가 시작하는 Y 위치다. */
    val unitBottom: Float,
    /** 프레임 시각: 지정 동작에서 sprite frame을 선택할 애니메이션 시간이다. */
    val frameTime: Float,
    /** asset 프레임 ID: 렌더 이벤트와 실제 그리기가 공통으로 기록하는 원본 프레임 식별자다. */
    val assetFrameId: String,
    /** avatar 너비: 선택 프레임을 화면에 그릴 가로 크기다. */
    val avatarWidth: Float,
    /** avatar 높이: 선택 프레임을 화면에 그릴 세로 크기다. */
    val avatarHeight: Float,
    /** avatar X 보정: 유닛 기준점에서 프레임 왼쪽으로 이동할 거리다. */
    val avatarOffsetX: Float,
    /** avatar Y 보정: 유닛 기준점에서 프레임 아래로 이동할 거리다. */
    val avatarOffsetY: Float,
    /** 피해 숫자 영역: 피격 경로에서 원본 캡처의 label 위치와 크기를 재현한다. */
    val harmBounds: BattleCharacterFixtureBounds?,
    /** 프레임 방향: sprite timeline에서 원본 방향 frame을 선택할 값이다. */
    val frameDirection: Int,
)

/** 전투 캐릭터 표시 계획: fixture가 요구하는 HP 진영과 애니메이션 전이 단계를 프레젠테이션에 적용한다. */
internal data class BattleCharacterFixturePresentationPlan(
    /** 진영: 캐릭터 아래에 표시할 HP bar 종류를 결정한다. */
    val camp: BattleCharacterCamp,
    /** 최대 HP: HP bar의 전체 길이를 계산하는 기준 값이다. */
    val maxHp: Int,
    /** 현재 HP: fixture가 표시할 HP bar 비율과 피해 반영 이전 값을 결정한다. */
    val hp: Int,
    /** 표시 단계: 공격, 피격, 정리, 사망 중 적용할 상태 전이를 선택한다. */
    val stage: BattleCharacterFixturePresentationStage,
) {
    /** 프레젠테이션 생성: 화면이 확인한 유닛 ID에 fixture 상태 전이를 적용해 렌더 상태를 만든다. */
    fun create(unitId: String): BattleCharacterPresentation =
        BattleCharacterPresentation(unitId, camp, maxHp, hp).also { state ->
            when (stage) {
                BattleCharacterFixturePresentationStage.IDLE -> Unit
                BattleCharacterFixturePresentationStage.ATTACK_OUTLINE -> state.beginAttack()
                BattleCharacterFixturePresentationStage.ATTACK_HIGHLIGHT -> {
                    state.beginAttack()
                    state.animationMaterialEvent(116)
                }

                BattleCharacterFixturePresentationStage.HIT_IMPACT -> {
                    state.hitImpact(30)
                    state.animationMaterialEvent(110)
                }

                BattleCharacterFixturePresentationStage.HIT_CLEANUP -> {
                    state.hitImpact(30)
                    state.finishHit()
                }

                BattleCharacterFixturePresentationStage.DEATH_ACTION -> state.beginHide(BattleHideType.SI_WANG)
                BattleCharacterFixturePresentationStage.DEATH_HIDDEN -> {
                    state.beginHide(BattleHideType.SI_WANG)
                    state.finishHide()
                }
            }
        }
}

/** 전투 캐릭터 표시 단계: 각 캡처 경로가 재현할 프레젠테이션 전이의 완료 지점을 구분한다. */
internal enum class BattleCharacterFixturePresentationStage {
    IDLE,
    ATTACK_OUTLINE,
    ATTACK_HIGHLIGHT,
    HIT_IMPACT,
    HIT_CLEANUP,
    DEATH_ACTION,
    DEATH_HIDDEN,
}

/** 전투 캐릭터 영역: 피해 label처럼 원본 좌표를 직접 사용해야 하는 사각형의 위치와 크기다. */
internal data class BattleCharacterFixtureBounds(
    /** 왼쪽 좌표: 사각형의 X 시작 위치다. */
    val x: Float,
    /** 아래 좌표: 사각형의 Y 시작 위치다. */
    val y: Float,
    /** 너비: 사각형의 가로 길이다. */
    val width: Float,
    /** 높이: 사각형의 세로 길이다. */
    val height: Float,
)
