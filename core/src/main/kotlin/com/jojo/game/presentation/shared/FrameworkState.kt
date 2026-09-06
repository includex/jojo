// Game
package com.jojo.game.presentation.shared

/** LayeredSceneState: 복원한 프레임워크 생성기의 생명주기 상태를 관리한다. */
class LayeredSceneState {
    /**
     * `Layer`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class Layer(val id: Int, val name: String, val modal: Boolean)

    /**
     * `layers` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val layers = mutableListOf<Layer>()
    /**
     * `stack` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val stack = mutableListOf<Int>()
    /**
     * `queued` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val queued = mutableListOf<Layer>()
    /**
     * `modeling` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var modeling = false
    /**
     * `released` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val released = mutableListOf<Int>()
    /**
     * `add`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun add(id: Int, name: String, flags: Int): Layer? {
        val modal = flags and 1 != 0; if (modal && layers.any { it.modal }) {
            queued += Layer(id, name, true); return null
        }; if (modal && modeling) {
            queued += Layer(id, name, true); return null
        }; if (modal) modeling = true; layers.removeAll { it.id == id }; stack.remove(id); return Layer(
            id,
            name,
            modal
        ).also { layers += it; stack += id; modeling = false }
    }

    /**
     * `remove`: 상태와 자원을 정리한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun remove(id: Int) {
        val l = layers.firstOrNull { it.id == id }
            ?: return; layers.remove(l); stack.remove(id); released += id; if (l.modal && queued.isNotEmpty()) {
            val q = queued.removeAt(0); add(q.id, q.name, 1)
        }
    }
}


/**
 * `SceneLayerController`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

class SceneLayerController(private val scene: LayeredSceneState, private val id: Int, private val end: () -> Unit) {
    /**
     * `removeFromParent`: 상태와 자원을 정리한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun removeFromParent() = scene.remove(id)
    /**
     * `cancel`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun cancel(result: Int) {
        if (result == 1) end()
    }
}


/**
 * `AudioService`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

class AudioService(private val sink: (String) -> Unit) {
    /**
     * `music` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var music = true
    /**
     * `effect` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var effect = true
    /**
     * `bg`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun bg(res: String) {
        if (music) sink("music:$res")
    }

    /**
     * `fx`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun fx(res: String, loop: Boolean) {
        if (effect) sink("effect:$res:$loop")
    }

    /**
     * `stopAll`: 입력을 규칙에 따라 계산·변환한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun stopAll() {
        sink("stopEffects")
    }
}


/**
 * `ServiceFlow`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

class ServiceFlow(private val sink: (String) -> Unit, private val done: () -> Unit) {
    /**
     * `attached` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var attached = true
    /**
     * `touch`: 입력을 규칙에 따라 계산·변환한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun touch(button: Int, event: Int) {
        if (event != 2) return; if (button == 0) {
            attached = false; done()
        } else if (button == 1) sink("layer:skm:4")
    }
}


/**
 * `ServiceMenuState`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

class ServiceMenuState(flag: Int) {
    /**
     * `attached` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var attached = true
    /**
     * `visible` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val visible = List(5) { i -> (if (flag and 4 != 0) flag or 16 else flag) and (1 shl i) != 0 }
    /**
     * `touch`: 입력을 규칙에 따라 계산·변환한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun touch(event: Int) {
        if (event != 2) attached = false
    }
}
