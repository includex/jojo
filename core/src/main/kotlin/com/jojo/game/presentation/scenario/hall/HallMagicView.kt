// 시나리오 거점 마법 상세 표시 모델
package com.jojo.game.presentation.scenario.hall

import com.jojo.game.presentation.shared.overlay.MagicUiList

/** HallMagicView: 마법 목록 항목을 거점 상세 모달이 직접 그릴 수 있는 불변 값으로 정리한다. */
internal data class HallMagicView(
    /** 화면 제목에 표시할 마법 이름이다. */
    val name: String,
    /** 위력 라벨에 붙일 백분율 값이다. */
    val power: Int,
    /** 시전에 필요한 MP 수치다. */
    val cost: Int,
    /** 상세 패널 본문에 표시할 설명이다. */
    val intro: String,
    /** 마법 아이콘 텍스처 프레임 번호다. */
    val iconFrame: Int,
    /** 공격 가능 범위 텍스처 프레임 번호다. */
    val hitAreaFrame: Int,
    /** 효과 적용 범위 텍스처 프레임 번호다. */
    val effectAreaFrame: Int,
) {
    /** companion object: 공용 마법 목록 모델에서 거점 전용 표시 모델을 만든다. */
    companion object {
        /** from: nullable 위력 값을 0으로 정규화하고 원본 아이콘 번호를 화면 프레임 번호로 바꾼다. */
        fun from(magic: MagicUiList.Magic) = HallMagicView(
            name = magic.name,
            power = magic.power ?: 0,
            cost = magic.cost,
            intro = magic.intro,
            iconFrame = magic.icon + 1,
            hitAreaFrame = magic.hit + 1,
            effectAreaFrame = magic.eff + 1,
        )
    }
}
