// Game
package com.jojo.game.application.scenario

import com.jojo.game.*

import com.jojo.game.domain.scenario.ScenarioHead

/** ScenarioStageHeadCoordinator: 시나리오 인물 머리 노드의 표시·이동·페이드 상태를 관리한다. */
internal class ScenarioStageHeadCoordinator {
    /**
     * `heads` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val heads = linkedMapOf<Int, ScenarioHead>()

    /**
     * `head`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun head(id: Int): ScenarioHead = heads.getOrPut(id) { ScenarioHead(id) }

    /**
     * `move`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun move(id: Int, x: Int, y: Int): Float {
        val head = head(id)
        val dx = x - head.visualX
        val dy = y - head.visualY
        val duration = kotlin.math.sqrt(dx * dx + dy * dy) * 0.01f
        head.moveFromX = head.visualX
        head.moveFromY = head.visualY
        head.moveElapsed = 0f
        head.moveDuration = duration
        head.x = x
        head.y = y
        head.visible = true
        if (duration <= 0f) {
            head.visualX = x.toFloat()
            head.visualY = y.toFloat()
        }
        return duration
    }

    /**
     * `show`: 화면 표시 상태를 렌더링한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun show(id: Int, x: Int, y: Int): Float {
        val existing = heads[id]
        if (existing != null && existing.visible) {
            existing.x = x
            existing.y = y
            existing.visualX = x.toFloat()
            existing.visualY = y.toFloat()
            existing.moveDuration = 0f
            existing.visible = true
            return 0f
        }
        heads[id] = ScenarioHead(id, x, y).apply {
            visualX = x.toFloat()
            visualY = y.toFloat()
            opacity = 0f
            fadeFrom = 0f
            fadeTo = 1f
            fadeElapsed = 0f
            fadeDuration = 1f
        }
        return 1f
    }

    /**
     * `hide`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun hide(id: Int): Float {
        val head = heads[id] ?: return 0f
        head.visible = false
        head.fadeFrom = head.opacity
        head.fadeTo = 0f
        head.fadeElapsed = 0f
        head.fadeDuration = 1f
        return 1f
    }

    /**
     * `update`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun update(delta: Float) {
        val elapsedDelta = delta.coerceAtLeast(0f)
        heads.values.forEach { head ->
            if (head.moveDuration > 0f) {
                head.moveElapsed = (head.moveElapsed + elapsedDelta).coerceAtMost(head.moveDuration)
                val progress = head.moveElapsed / head.moveDuration
                head.visualX = head.moveFromX + (head.x - head.moveFromX) * progress
                head.visualY = head.moveFromY + (head.y - head.moveFromY) * progress
                if (progress >= 1f) head.moveDuration = 0f
            }
            if (head.fadeDuration > 0f) {
                head.fadeElapsed = (head.fadeElapsed + elapsedDelta).coerceAtMost(head.fadeDuration)
                val progress = head.fadeElapsed / head.fadeDuration
                head.opacity = head.fadeFrom + (head.fadeTo - head.fadeFrom) * progress
                if (progress >= 1f) head.fadeDuration = 0f
            }
        }
    }

    /**
     * `finish`: 조건과 입력 상태를 검증한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun finish() = heads.values.forEach { head ->
        head.visualX = head.x.toFloat()
        head.visualY = head.y.toFloat()
        head.moveDuration = 0f
        head.opacity = head.fadeTo
        head.fadeDuration = 0f
    }
}
