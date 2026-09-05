package com.jojo.game

/** Lifecycle state for recovered framework factories; native audio is a boundary sink. */
class LayeredSceneState {
    data class Layer(val id: Int, val name: String, val modal: Boolean)

    val layers = mutableListOf<Layer>()
    val stack = mutableListOf<Int>()
    val queued = mutableListOf<Layer>()
    var modeling = false
    val released = mutableListOf<Int>()
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

    fun remove(id: Int) {
        val l = layers.firstOrNull { it.id == id }
            ?: return; layers.remove(l); stack.remove(id); released += id; if (l.modal && queued.isNotEmpty()) {
            val q = queued.removeAt(0); add(q.id, q.name, 1)
        }
    }
}

/**
 * class  `SceneLayerController`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class SceneLayerController(private val scene: LayeredSceneState, private val id: Int, private val end: () -> Unit) {
    fun removeFromParent() = scene.remove(id)
    fun cancel(result: Int) {
        if (result == 1) end()
    }
}

/**
 * class  `AudioService`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class AudioService(private val sink: (String) -> Unit) {
    var music = true
    var effect = true
    fun bg(res: String) {
        if (music) sink("music:$res")
    }

    fun fx(res: String, loop: Boolean) {
        if (effect) sink("effect:$res:$loop")
    }

    fun stopAll() {
        sink("stopEffects")
    }
}

/**
 * class  `ServiceFlow`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class ServiceFlow(private val sink: (String) -> Unit, private val done: () -> Unit) {
    var attached = true
    fun touch(button: Int, event: Int) {
        if (event != 2) return; if (button == 0) {
            attached = false; done()
        } else if (button == 1) sink("layer:skm:4")
    }
}

/**
 * class  `ServiceMenuState`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class ServiceMenuState(flag: Int) {
    var attached = true
    val visible = List(5) { i -> (if (flag and 4 != 0) flag or 16 else flag) and (1 shl i) != 0 }
    fun touch(event: Int) {
        if (event != 2) attached = false
    }
}
