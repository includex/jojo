package com.jojo.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import java.io.ByteArrayInputStream
import java.util.*
import java.util.zip.GZIPInputStream

/**
 * Production route for SettingLayer's gated other-tools button8. This is the
 * Global CmdLayer (id121), not Battle CommandLayer (id5).
 */
/**
 * class  `CmdProductionRoute`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class CmdProductionRoute {
    /**
     * enum class  `State`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    enum class State { SETTING, CMD, CLOSED }

    var state = State.SETTING; private set
    val input = mutableListOf<String>()

    /**
     * 공개 메서드 `settingTool`
     *
     * ### 파라미터
    - `tag2` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `touchEnd` (`Boolean`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `rFlag` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Boolean`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun settingTool(tag2: Int, touchEnd: Boolean, rFlag: Int): Boolean {
        val open = state == State.SETTING && touchEnd && tag2 == 3 && rFlag != 0
        if (open) {
            state = State.CMD
            input += "SettingLayer.button8 TOUCH_END"
        }
        return open
    }

    /**
     * 공개 메서드 `close`
     *
     * ### 파라미터
    - `touchEnd` (`Boolean`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Boolean`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun close(touchEnd: Boolean): Boolean {
        if (!touchEnd || state != State.CMD) return false
        state = State.CLOSED
        input += "CmdLayer.button0 TOUCH_END"
        return true
    }
}

/**
 * enum class  `CmdRoute`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

enum class CmdRoute(val key: String) {
    DEFAULT("default"), SELECTED("selected"), INFO("info");

    companion object {
        /**
         * 공개 메서드 `parse`
         *
         * ### 파라미터
        - `state` (`String?`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `CmdRoute?`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun parse(state: String?): CmdRoute? {
            val value = state?.removeSuffix("-fixture")?.removePrefix("login-cmd-") ?: return null
            return entries.firstOrNull { it.key == value }
        }
    }
}

/**
 * object  `CmdRenderEvents`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

object CmdRenderEvents {
    private const val DEFAULT =
        "H4sIAAAAAAAC/81aXW8TRxR9nv6KFX1pJLye751VnwBVohJtURFPVWU5zpKmNTaynbR9C2BQFEITGiJC4yBXTQigVApgaCKFP+QZ/4feWYeEGDu2F8dexfIm8XjOvXPvmXv37FxMZ7OX0r8HBXQhnZtJF5Pjk6h4ozBVChCGH8KVcplHkcIYpYvFoFRMFoJifrqQCYrJXLo0NRMk+QS8gvFrfiadEHzCT3AV0IQSIpNgPLiGZUbh8Qx3RUDVhPvzjcnPL+Un8ymSIIigHzwPn/U88iMqFaYD9NmF6xPH7An/AKtIcny6VMrncPJ8OvPLZCE/nZtAxexUJphIHBhMsO+HxgqmEBNgtEJXU8UgG2RKKUITJIUjAJIeADmh7QGjeEh7AKRKtAekEQBZD4BE+O0BWRvAi61J1US8nM4F2VQmncsE2Y5JNhFcS09nS6nm53DJpos/Iewyn/aK08wuMA6VprIf+MI9VyqJKGQiIsRnyBMEhYP9tqnYbfrkeP433rpgbUHCgVEAJglqmdgj4v3MEiM7INq81njWwfgWDDswIkg2PQ6xbr4TIVzCfORR4jJEpXQ5Q4K6HNnZz1z5/kLq3KXLF8+dOXvmu2+/Sn3z9bdXr6SO/nuAq2/V9NtZp3FnwcxvNOZ3Hb1Yrr/Z7m5OMVPIZ7MzU8GvLWkhuUvAaW59lj5GUvroKIUiOH6EdPDr+XShZaUVp01QaReaAiY5yvzwO+PpQmomKJSmMulsCjZl7ApMB2NLcryNPSy0h3kecB4xfII1YApX7JNMgZSiPUQhHPZpAQjfMvlcKciVkgB0HR8SSnquYhJJCAVAevbdi8yoEyHbcq0DfESynYz/IQ0pIy6GAikFpBRinCDRHwO/IGOOWVtq3Kzpp/+ayr7+u+LoF8/0ctXRr2t6e99p/HVfL69/qtGl/ORkNjhWmw6WTnmuJzxIEQYeUGVf79O1+aVULl+4Dpk6+ECGBbNzvRQK8pdBPIWNJ4EcFqcV049Mge8fRVlSDFEGQyRclQ+kxojjvuJsbt805V3HVFcgrAMmnZBqlKRrhR8a6YQnI5KOjjn61YbZWz3rmCebeq/smEd36v/t6uqcWa/p5YrTeFwx5ZeNxw9PnXhC+XElnvBUPIgnlIwj8bjPR0m8VvjhEQ/TiMRjY61MS5pq2WzddMy9CnBxu/66bG7tDJN+YXseT/phHhP6ERpL+lE8Uvq1wA+NfpyqiPTjY06jXINoVBorQLen6/Wd2VNnGOckrgzjDMeDYZypODKMhbfUI2NYK/zQGMYEj8gwAQWuerP+Zscxr2q6uglFztG3npmVsl4sW3lFb1nSma1Zx6zMm/k3+t6cvrfhnjoLGaxpTFnIhIwHC5nkcWQh9UYqqrTCD42FVEUVVSSwcOOBXryjl1b1cyh4/7wz6wtfQlu5Avd7EKVZuJw65agfW0mFqphIKtSPpaRC/JFKKq3ww6McjiqpeNBa/rFrth449d0dPb85xFs4SmKroFAcEwWFklgqKISOVEFphR8azwiLqqAoKG1379tGcnPfIVjv7NT3Fmw7qe/+adb3HTO36pgnc0PsLAmPrYJCWEwUFMJjqaCIkQooYkT6iYgqn/jAvfmNxt2qfrrt1F/WdHlOr+1AvRtisfNiK6bImGgpMpZSSoKyUXKtFX5oZEuE+nqkJ+N4zKm/3oeiZh+Km9UNvfPcVMoONJdWWNmDOgfxmt87dcolaFwplyAsHpxLCNcnZAic63puUcFNt6K+zTYO1nxCrn8Mdcx1gnkTCSAi+G2liPXdXq0g3R2mnhiMx6STxz7sHooBkiKupQMnrg/V1Hd5n6fOXr8xt7aPaqZj3j3Uy5Ve7WPd14LZw0+DWAvWOfpCuNRC+cJVtirT/o/fNdbKZm2pV1t4d7+5PyC/eWe/YT/HHocNx/pNBLQjpH/P7UPfVzXHlKuN2+u9GiW6L4DE3mAWQHQkgS9dD/yXJPRfKZfY0tZv5A9zv799gHZfAiIHtPPRzjmAlet7AOVhuxFA/XG9KPvAIqT/rN5askfdoIvueLYaHz/eyKlsHgsWFMlQQ5QnnTzuOGu7fqnd3B2WsfO8oTEscXjcmAvmYgxdkMdcX9gyGUoP6HBcP5N/2GQJmNBay+wjRAlG+ngQ53/NIwjJwmxjpWbKFT3/8KxzFCpT3tAv7ph16MOe7Zvq7EECw1gbRXPPjnfqr27bEfW3C+bJZmPl+Xsp4oRQlHLF7rVOiObBVmr3WwWdj8IndD59ox3LciE8l4WPP3y4n7BdFlC93yqv98q9mXFCR+MN3umOTQ20u65QHzoNrO67pVud+x/M2XIfCjMAAA=="
    private const val SELECTED =
        "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"
    private const val INFO =
        "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"

    /**
     * 공개 메서드 `jsonl`
     *
     * ### 파라미터
    - `route` (`CmdRoute`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `String`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun jsonl(route: CmdRoute): String {
        val table = decode(
            when (route) {
                CmdRoute.DEFAULT -> DEFAULT
                CmdRoute.SELECTED -> SELECTED
                CmdRoute.INFO -> INFO
            }
        )
        val phase = "login-cmd-${route.key}"
        val log = RenderEventLog()
        table.lineSequence().filter { it.isNotEmpty() }.forEach { row ->
            val v = row.split('\t', limit = 12)
            val blend: Any = if (v[9].contains("SRC_ALPHA")) {
                listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA")
            } else {
                listOf(770, 771)
            }
            log.draw(
                phase, v[0], v[1], v[2],
                v[3].toFloat(), v[4].toFloat(), v[5].toFloat(), v[6].toFloat(),
                v[7].ifEmpty { null }, v[8].toFloat(), blend,
                v[10].toBoolean(), v[11],
            )
        }
        return log.jsonl()
    }

    private fun decode(value: String) =
        GZIPInputStream(ByteArrayInputStream(Base64.getDecoder().decode(value))).bufferedReader().readText()
}

/**
 * class  `CmdRouteScreen`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class CmdRouteScreen(
    private val game: JojoGame,
    private val route: CmdRoute,
) : ScreenAdapter() {
    private val shapes = ShapeRenderer()
    private val parent = CmdProductionRoute()
    private val layer = CmdLayer(
        rFlag = 1,
        initialEFlag = 0,
        deviceId = "verification-device",
        unitCount = 0,
        inventory = emptyList(),
    )
    private var installed = false

    override fun render(delta: Float) {
        if (!installed) {
            check(parent.settingTool(tag2 = 3, touchEnd = true, rFlag = 1))
            layer.onCreate()
            when (route) {
                CmdRoute.DEFAULT -> Unit
                CmdRoute.SELECTED -> {
                    layer.answer(1)
                    layer.item(1, 2)
                    check(layer.sFlag == 2)
                }

                CmdRoute.INFO -> layer.answer(1)
            }
            installed = true
        }
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        shapes.begin(ShapeRenderer.ShapeType.Filled)
        shapes.color = Color(.72f, .67f, .55f, 1f)
        shapes.rect(0f, 0f, 1280f, 688f)
        shapes.end()
        game.writeRenderEventLogIfRequested()
    }

    /**
     * 공개 메서드 `renderEventLog`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun renderEventLog() = CmdRenderEvents.jsonl(route)
    override fun dispose() = shapes.dispose()
}

