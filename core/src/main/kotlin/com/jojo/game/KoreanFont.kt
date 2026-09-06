package com.jojo.game

import com.badlogic.gdx.Application.ApplicationType
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import java.io.File


/** 실행 환경에 맞는 한국어 비트맵 글꼴을 생성한다. */
object KoreanFont {
    /** 크기·외곽선·색상 설정으로 글꼴을 생성한다. */
    fun create(
        size: Int,
        extraCharacters: String,
        borderWidth: Float = 0f,
        borderColor: Color = Color.CLEAR,
        fillColor: Color = Color.WHITE,
    ): BitmapFont {
        val candidates = listOfNotNull(
            System.getenv("JOJO_FONT_PATH"),
            // 안드로이드 시스템 글꼴을 우선 사용해 APK가 데스크톱 전용 글꼴에 의존하지 않게 한다.
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
                    // 프레임버퍼 비교를 위해 프리타입 힌팅 정책을 명시적으로 선택한다.
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
