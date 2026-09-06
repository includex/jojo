// Presentation
package com.jojo.game.presentation.shared.overlay

/** LoadLayer: 로딩 문구 유무에 따라 회전 애니메이션과 라벨 표시를 구성하는 간단한 모달 모델이다. */
class LoadLayer {
    /**
     * `View`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class View(val labelActive: Boolean, val label: String, val anime: String)
    /**
     * `onCreate`: 객체나 결과를 생성한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun onCreate(text: String?) = View(text != null, text ?: "", "rotateBy:2:360:repeatForever")
}

/** ModalLoadProductionRoute: 비동기 검증이 시작·완료될 때 로딩 모달의 부착 상태와 안내 문구를 전환한다. */
class ModalLoadProductionRoute {
    /**
     * `attached` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var attached = false; private set
    /**
     * `text` (String?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var text: String? = null; private set
    /**
     * `getSystemTimeStarted`: 상태나 데이터를 조회한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun getSystemTimeStarted() { attached = true; text = "검증 중……" }
    /**
     * `requestCompleted`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun requestCompleted() { attached = false }
}
