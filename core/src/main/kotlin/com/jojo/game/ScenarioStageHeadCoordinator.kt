package com.jojo.game

/** Owns the independent lifecycle and fade/move state of scenario head nodes. */
internal class ScenarioStageHeadCoordinator {
    val heads = linkedMapOf<Int, ScenarioHead>()

    fun head(id: Int): ScenarioHead = heads.getOrPut(id) { ScenarioHead(id) }

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

    fun hide(id: Int): Float {
        val head = heads[id] ?: return 0f
        head.visible = false
        head.fadeFrom = head.opacity
        head.fadeTo = 0f
        head.fadeElapsed = 0f
        head.fadeDuration = 1f
        return 1f
    }

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

    fun finish() = heads.values.forEach { head ->
        head.visualX = head.x.toFloat()
        head.visualY = head.y.toFloat()
        head.moveDuration = 0f
        head.opacity = head.fadeTo
        head.fadeDuration = 0f
    }
}
