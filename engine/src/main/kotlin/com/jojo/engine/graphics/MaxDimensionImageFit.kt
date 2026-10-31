package com.jojo.engine.graphics

/** Scales by the longest source dimension and centers in the anchor rectangle. */
object MaxDimensionImageFit {
    fun fit(sourceWidth: Int, sourceHeight: Int, anchorX: Float, anchorY: Float, anchorWidth: Float, anchorHeight: Float): Bounds {
        val sourceMax = maxOf(sourceWidth, sourceHeight).coerceAtLeast(1)
        val scale = anchorHeight / sourceMax
        val width = sourceWidth * scale
        val height = sourceHeight * scale
        return Bounds(anchorX + (anchorWidth - width) / 2f, anchorY + (anchorHeight - height) / 2f, width, height)
    }

    /** 초상화의 왼쪽 아래 좌표와 출력 크기다. */
    data class Bounds(val x: Float, val y: Float, val width: Float, val height: Float)
}
