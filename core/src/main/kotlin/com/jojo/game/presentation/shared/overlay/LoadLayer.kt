// Presentation
package com.jojo.game.presentation.shared.overlay

/** LoadLayer: 로딩 문구 유무에 따라 회전 애니메이션과 라벨 표시를 구성하는 간단한 모달 모델이다. */
class LoadLayer {
    data class View(val labelActive: Boolean, val label: String, val anime: String)
    fun onCreate(text: String?) = View(text != null, text ?: "", "rotateBy:2:360:repeatForever")
}

/** ModalLoadProductionRoute: 비동기 검증이 시작·완료될 때 로딩 모달의 부착 상태와 안내 문구를 전환한다. */
class ModalLoadProductionRoute {
    var attached = false; private set
    var text: String? = null; private set
    fun getSystemTimeStarted() { attached = true; text = "검증 중……" }
    fun requestCompleted() { attached = false }
}
