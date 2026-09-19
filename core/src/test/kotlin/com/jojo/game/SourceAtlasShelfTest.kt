package com.jojo.game

import com.jojo.game.presentation.scenario.assets.SourceAtlasShelf
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class SourceAtlasShelfTest {
    @Test fun observedOpeningInsertionSequenceComesFromAssetSizes() {
        val shelf = SourceAtlasShelf()
        val sizes = listOf(19 to 17, 24 to 24, 344 to 84, 192 to 240, 344 to 84, 192 to 240, 192 to 240)
        assertEquals(listOf(2, 23, 49, 395, 589, 935, 1129).map { it to 2 }, sizes.map { shelf.insert(it.first, it.second) })
    }

    @Test fun rowWrapUsesTallestPreviouslyInsertedTextureAndRejectsOverflow() {
        val shelf = SourceAtlasShelf(32)
        assertEquals(2 to 2, shelf.insert(10, 8))
        assertEquals(14 to 2, shelf.insert(10, 4))
        assertEquals(2 to 12, shelf.insert(10, 16))
        assertEquals(14 to 12, shelf.insert(10, 8))
        assertNull(shelf.insert(10, 4))
    }

    @Test fun aNewSceneStartsWithAnEmptyShelf() {
        val old = SourceAtlasShelf()
        old.insert(192, 240)
        assertEquals(196 to 2, old.insert(192, 240))
        assertEquals(2 to 2, SourceAtlasShelf().insert(192, 240))
    }
}
