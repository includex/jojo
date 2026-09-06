// Dialogue
package com.jojo.game.presentation.shared.dialogue

import com.badlogic.gdx.graphics.Texture

/** 원본 Cocos face 노드처럼 최대 변 기준으로 초상화 크기를 계산하는 유틸리티다. */
internal object DialoguePortraitGeometry {
    /** 기준 상자 중앙을 유지하면서 원본 비율의 초상화 경계를 계산한다. */
    fun fit(texture: Texture, anchorX: Float, anchorY: Float, anchorWidth: Float, anchorHeight: Float): Bounds {
        val sourceMax = maxOf(texture.width, texture.height).coerceAtLeast(1)
        val scale = anchorHeight / sourceMax
        val width = texture.width * scale
        val height = texture.height * scale
        return Bounds(anchorX + (anchorWidth - width) / 2f, anchorY + (anchorHeight - height) / 2f, width, height)
    }

    /** 초상화의 왼쪽 아래 좌표와 출력 크기다. */
    data class Bounds(val x: Float, val y: Float, val width: Float, val height: Float)
}
