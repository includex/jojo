// Battle Fixture
package com.jojo.game.presentation.battle.fixture

import com.jojo.game.domain.battle.BattleUnit
import com.jojo.game.presentation.battle.timeline.BattleCharacterPresentation

/** 전투 캐릭터 경로 샘플: fixture 계획을 실제 전투 유닛 및 프레젠테이션과 결합한 화면 렌더 입력이다. */
internal data class BattleCharacterRouteSample(
    /** 전투 유닛: avatar texture와 sprite frame을 조회할 실제 전장 객체다. */
    val unit: BattleUnit,
    /** 표시 상태: HP 진영과 공격·피격·사망 전이가 적용된 캐릭터 프레젠테이션이다. */
    val state: BattleCharacterPresentation,
    /** 유닛 왼쪽 좌표: 캐릭터 avatar를 그릴 X 기준점이다. */
    val unitLeft: Float,
    /** 유닛 아래 좌표: 캐릭터 avatar를 그릴 Y 기준점이다. */
    val unitBottom: Float,
    /** 프레임 시각: 캐릭터 동작에서 표시할 sprite frame을 고르는 시간이다. */
    val frameTime: Float,
    /** asset 프레임 ID: 렌더 이벤트에 기록할 원본 avatar 프레임 식별자다. */
    val assetFrameId: String,
    /** avatar 너비: 화면에 그릴 캐릭터 프레임의 가로 크기다. */
    val avatarWidth: Float,
    /** avatar 높이: 화면에 그릴 캐릭터 프레임의 세로 크기다. */
    val avatarHeight: Float,
    /** avatar X 보정: 유닛 위치에서 프레임 원점까지의 가로 이동량이다. */
    val avatarOffsetX: Float,
    /** avatar Y 보정: 유닛 위치에서 프레임 원점까지의 세로 이동량이다. */
    val avatarOffsetY: Float,
    /** 피해 label 영역: 피격 숫자를 원본 위치에 배치할 X·Y·너비·높이다. */
    val harmRect: FloatArray?,
    /** 프레임 방향: sprite timeline에서 선택할 캐릭터 방향 값이다. */
    val frameDirection: Int,
)
