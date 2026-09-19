package com.jojo.game.presentation.scenario.assets

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Disposable

/** Cocos's two-pixel shelf allocation; callers request assets when they are rendered. */
internal class SourceAtlasShelf(private val size: Int = 2048) {
    private var x = 2
    private var y = 2
    private var nextY = 2
    fun insert(width: Int, height: Int): Pair<Int, Int>? {
        require(width > 0 && height > 0)
        if (x + width + 2 > size) { x = 2; y = nextY }
        nextY = maxOf(nextY, y + height + 2)
        if (nextY > size) return null
        val point = x to y
        x += width + 2
        return point
    }
}

/** Scene-owned atlas for the source's uncompressed, linear-filtered UI sprite assets. */
internal class SourceSpriteAtlas : Disposable {
    private data class Page(val texture: Texture, val shelf: SourceAtlasShelf)
    private val pages = mutableListOf<Page>()
    private val regions = mutableMapOf<String, TextureRegion>()
    private val standaloneTextures = mutableListOf<Texture>()

    fun file(path: String): TextureRegion? {
        regions[path]?.let { return it }
        val file = Gdx.files.internal(path)
        if (!file.exists()) return null
        val pixels = Pixmap(file)
        return try { insert(path, pixels) } finally { pixels.dispose() }
    }

    fun insert(identity: String, pixels: Pixmap): TextureRegion? {
        regions[identity]?.let { return it }
        // Source stops inserting after the sixth atlas has been created (index == maxAtlasCount).
        // Only the image sprites proven to use the source's default sampler enter this atlas.
        if (pixels.width > 512 || pixels.height > 512 || pages.size >= 6) return standalone(identity, pixels)
        var page = pages.lastOrNull()
        var point = page?.shelf?.insert(pixels.width, pixels.height)
        if (point == null) {
            val empty = Pixmap(2048, 2048, Pixmap.Format.RGBA8888)
            val texture = try { Texture(empty) } finally { empty.dispose() }
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
            texture.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge)
            page = Page(texture, SourceAtlasShelf())
            pages.add(page)
            point = checkNotNull(page.shelf.insert(pixels.width, pixels.height))
        }
        val target = checkNotNull(page)
        val (x, y) = point
        // Match the source's shifted copies, including corner copies only for tiny images.
        if (pixels.width <= 8 || pixels.height <= 8) {
            for (dx in listOf(-1, 1)) for (dy in listOf(-1, 1)) target.texture.draw(pixels, x + dx, y + dy)
        }
        for ((dx, dy) in listOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1, 0 to 0)) {
            target.texture.draw(pixels, x + dx, y + dy)
        }
        return TextureRegion(target.texture, x, y, pixels.width, pixels.height).also { regions[identity] = it }
    }

    private fun standalone(identity: String, pixels: Pixmap): TextureRegion {
        val texture = Texture(pixels)
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
        texture.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge)
        standaloneTextures.add(texture)
        return TextureRegion(texture).also { regions[identity] = it }
    }

    override fun dispose() {
        pages.forEach { it.texture.dispose() }
        pages.clear()
        standaloneTextures.forEach { it.dispose() }
        standaloneTextures.clear()
        regions.clear()
    }
}
