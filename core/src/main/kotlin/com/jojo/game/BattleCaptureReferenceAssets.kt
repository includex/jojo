package com.jojo.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.Disposable

internal enum class BattleCaptureReferenceFrame(val fileName: String, val errorLabel: String) {
    WIN_RESULT("source-r00-win-result.rgba", "R_00 win-result"),
    SAVE("source-save.rgba", "S_00 SaveLayer"),
    LOAD("source-load.rgba", "S_00 LoadGameLayer"),
    SETTING("source-setting.rgba", "S_00 SettingLayer"),
    HELPER("source-helper.rgba", "S_00 HelperLayer"),
    WIN_CONDITION("source-win-condition.rgba", "S_00 WinConBoxLayer"),
    MENU("source-menu.rgba", "S_00 MenuLayer"),
    DIALOGUE_ONE("source-r00-dialogue-1.rgba", "dialogue-1"),
    DIALOGUE_TWO("source-dialogue-2.rgba", "dialogue-2"),
    TERRAIN("source-terrain.rgba", "S_00 TerrainLayer"),
    PROPERTY("source-property.rgba", "S_00 PropertyLayer"),
    TREASURE("source-treasure.rgba", "S_00 TreasureLayer"),
    FORCES("source-forces.rgba", "S_00 ForcesListLayer"),
    UNIT_INFO("source-unit-info.rgba", "S_00 UnitInfoLayer"),
}

/** Lazily owns source-comparison framebuffers used only by addressed capture routes. */
internal class BattleCaptureReferenceAssets : Disposable {
    private val frames = BattleCaptureReferenceFrame.entries.associateWith { frame ->
        lazy { load(frame) }
    }

    fun texture(frame: BattleCaptureReferenceFrame): Texture? = frames.getValue(frame).value

    private fun load(frame: BattleCaptureReferenceFrame): Texture? =
        Gdx.files.internal("reference/${frame.fileName}").takeIf { it.exists() }?.let { raw ->
            val bytes = raw.readBytes()
            check(bytes.size == WIDTH * HEIGHT * 4) { "Invalid ${frame.errorLabel} reference framebuffer" }
            val pixmap = Pixmap(WIDTH, HEIGHT, Pixmap.Format.RGBA8888)
            try {
                pixmap.pixels.put(bytes).rewind()
                Texture(pixmap).also {
                    it.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest)
                }
            } finally {
                pixmap.dispose()
            }
        }

    override fun dispose() {
        frames.values.filter { it.isInitialized() }.mapNotNull { it.value }.forEach(Texture::dispose)
    }

    private companion object {
        const val WIDTH = 2560
        const val HEIGHT = 1376
    }
}
