package com.jojo.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Application.ApplicationType
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import java.io.File

object KoreanFont {
    fun create(
        size: Int,
        extraCharacters: String,
        borderWidth: Float = 0f,
        borderColor: Color = Color.CLEAR,
        fillColor: Color = Color.WHITE,
    ): BitmapFont {
        val candidates = listOfNotNull(
            System.getenv("JOJO_FONT_PATH"),
            // Android system images ship one of these CJK font locations;
            // FreeType can open the system font directly, avoiding a
            // desktop-only Apple font dependency in the APK.
            if (Gdx.app?.type == ApplicationType.Android) "/system/fonts/NotoSansCJK-Regular.ttc" else null,
            if (Gdx.app?.type == ApplicationType.Android) "/system/fonts/NotoSansKR-Regular.otf" else null,
            if (Gdx.app?.type == ApplicationType.Android) "/system/fonts/NanumGothic.ttf" else null,
            "/System/Library/Fonts/AppleSDGothicNeo.ttc",
            "/System/Library/Fonts/Supplemental/AppleGothic.ttf",
        )
        val fontPath = candidates.firstOrNull { File(it).isFile }
            ?: error("한국어 글꼴을 찾지 못했습니다. JOJO_FONT_PATH를 설정하세요.")
        val generator = FreeTypeFontGenerator(FileHandle(File(fontPath)))
        return try {
            generator.generateFont(
                FreeTypeFontGenerator.FreeTypeFontParameter().apply {
                    this.size = size
                    characters = (FreeTypeFontGenerator.DEFAULT_CHARS + extraCharacters).toSortedSet().joinToString("")
                    this.borderWidth = borderWidth
                    this.borderColor = borderColor
                    this.color = fillColor
                    // Cocos system labels are rasterized by Chromium/Skia,
                    // while the game uses FreeType.  Keep the production
                    // default, but make the hinting policy explicit and
                    // reproducibly selectable for framebuffer comparison.
                    hinting = when (System.getenv("JOJO_FONT_HINTING")?.lowercase()) {
                        "none" -> FreeTypeFontGenerator.Hinting.None
                        "slight" -> FreeTypeFontGenerator.Hinting.Slight
                        "full" -> FreeTypeFontGenerator.Hinting.Full
                        null, "auto" -> FreeTypeFontGenerator.Hinting.AutoMedium
                        else -> error("JOJO_FONT_HINTING must be auto, none, slight, or full")
                    }
                }
            )
        } finally {
            generator.dispose()
        }
    }
}
