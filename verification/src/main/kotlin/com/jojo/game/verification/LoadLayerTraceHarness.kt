package com.jojo.game.verification
import com.jojo.game.presentation.shared.overlay.*

import com.jojo.game.*

/**
 * object  `LoadLayerTraceHarness`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

object LoadLayerTraceHarness {
    @JvmStatic
    fun main(a: Array<String>) {
        val s = java.nio.file.Files.readString(java.nio.file.Path.of(a[0]))
        Regex("\\{\\\"id\\\":\\\"([^\\\"]+)\\\",\\\"text\\\":(null|\\\"([^\\\"]*)\\\")} ").findAll(s).toList()
        val rows = Regex("\\{\\\"id\\\":\\\"([^\\\"]+)\\\",\\\"text\\\":(null|\\\"([^\\\"]*)\\\")}").findAll(s)
            .map { m ->
                val v =
                    LoadLayer().onCreate(if (m.groupValues[2] == "null") null else m.groupValues[3]); "{\"id\":\"${m.groupValues[1]}\",\"view\":{\"labelActive\":${v.labelActive},\"label\":\"${v.label}\",\"anime\":\"${v.anime}\"}}"
            }.joinToString(
            ",",
            "[",
            "]"
        ); java.nio.file.Files.createDirectories(java.nio.file.Path.of(a[1]).parent); java.nio.file.Files.writeString(
            java.nio.file.Path.of(a[1]),
            rows
        )
    }
}
