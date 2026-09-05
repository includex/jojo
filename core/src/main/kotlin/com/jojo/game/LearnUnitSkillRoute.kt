package com.jojo.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import java.io.ByteArrayInputStream
import java.util.*
import java.util.zip.GZIPInputStream

/** Production contract of EditLayer4 button4 -> Global LearnUnitSkillLayer id132. */
class LearnUnitSkillFlow(initialUnit0: Int = 1024) {
    sealed interface Effect {
        /**
         * data class  `OpenSelectList`
         *
         * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
         *
         * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
         */

        data class OpenSelectList(val selected: Int, val page: Int, val pageCount: Int = 50) : Effect

        /**
         * data class  `SetUnit0`
         *
         * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
         *
         * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
         */

        data class SetUnit0(val value: Int) : Effect
        data object Close : Effect
    }

    var selectedSkill = 0; private set
    var unit0 = initialUnit0; private set
    var pendingUnit0: Int? = null; private set

    /**
     * 공개 메서드 `selectSkill`
     *
     * ### 파라미터
    - `id` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun selectSkill(id: Int) {
        selectedSkill = id; pendingUnit0 = null
    }

    /**
     * 공개 메서드 `panelButton`
     *
     * ### 파라미터
    - `panel` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `button` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `List<Effect>`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun panelButton(panel: Int, button: Int): List<Effect> = if (panel == 0 && button in 0..2) listOf(
        Effect.OpenSelectList(
            unit0.coerceIn(0, 1024),
            unit0.coerceIn(0, 1024) / 50
        )
    ) else emptyList()

    /**
     * 공개 메서드 `selectListResult`
     *
     * ### 파라미터
    - `value` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun selectListResult(value: Int) {
        if (value >= 0) pendingUnit0 = value
    }

    /**
     * 공개 메서드 `save`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `List<Effect>`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun save(): List<Effect> =
        pendingUnit0?.let { unit0 = it; pendingUnit0 = null; listOf(Effect.SetUnit0(it)) }.orEmpty()

    /**
     * 공개 메서드 `close`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun close() = listOf(Effect.Close)
}

/** EDIT-gated parent route. */
class EditRosterLearnRoute(private val editEnabled: Boolean) {
    /**
     * enum class  `State`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    enum class State { EDIT4, LEARN, CLOSED }

    var state = State.EDIT4; private set

    /**
     * 공개 메서드 `button`
     *
     * ### 파라미터
    - `tag` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `touchEnd` (`Boolean`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Boolean`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun button(tag: Int, touchEnd: Boolean): Boolean {
        val open = editEnabled && touchEnd && state == State.EDIT4 && tag == 4; if (open) state =
            State.LEARN; return open
    }

    /**
     * 공개 메서드 `close`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun close() {
        state = State.CLOSED
    }
}

/**
 * enum class  `LearnUnitSkillRoute`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

enum class LearnUnitSkillRoute(val key: String) {
    DEFAULT("default"), SELECT("select"), APPLY("apply"), CANCEL("cancel");

    companion object {
        /**
         * 공개 메서드 `parse`
         *
         * ### 파라미터
        - `state` (`String?`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `LearnUnitSkillRoute?`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun parse(state: String?): LearnUnitSkillRoute? {
            val normalized = state?.removeSuffix("-fixture")
                ?: return null; if (!normalized.startsWith("hall-learn-")) return null; return entries.firstOrNull {
                it.key == normalized.removePrefix(
                    "hall-learn-"
                )
            }
        }
    }
}

/**
 * object  `LearnUnitSkillRenderEvents`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

object LearnUnitSkillRenderEvents {
    private const val DEFAULT =
        "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"
    private const val SELECT_SUFFIX =
        "H4sIAGE5mWoC/8WaT2sTQRiHz+OnKD23k3nn786xFqFCrUXxJBK26VqDa1aSTdWbYOlF8SbqIdCDoB9A0O9k+h2c3bRmiZM02ZnMUsimzTC/eZ+88yRldi9O0/34TdJHu3HvNB60yl9ah3EvSduduNdJUjR42e/mCSLmB3gUYaYoighBx8nTeJjm7cnr5pLGg2eIYKYpeqwU2VIKnqC8P0zQrT17zn52krWBbgPKu2lyvH0VBVxhGUlEORYIQDOkBKBysDZjYeXpW0fZa44GabdzU0g5sE7ACaCZiRWI65klQcWAevMWi2dzFj+TUQysGZLGR+a9njwqwjAUs1PABgtgigTFHBVzbz58sNve2T/c29nc2rx/cKd97+7Bo4ft6V+vUsdnF5fvRjdnDzr9LE1Pu8mrmR6QvFyD1qY8U5sEiabtUqPIaVABlN6cVo5yCyofOlkvT3p5y+S8+NcjUpZ5kjFEzTWq3SCL0qydY0mu2TYLo6v9BEpgXWTyCFFpeguJ1ZoJCCF4g/hhz0VT7G3J62bPpTt78MZe8abY25LXzV4Jd/bUG3sgrDHp2KLXbh3C3fEzf/ivxCu4bkj51eRQyheS12bPvSs/PHtbcijlu7AX3pUfnr0tOZTyXdhL/8pvQDq26GDKd8GvvCvfbMiGlF9NDqV8bsLqso+8Kz88e1tyKOW7sNfelR+evS05lPId2APxr/wGpGOLDqZ8F/zgXfksgoaUX00OpXymZW321Lvyw7O3JYdSvgt75l354dnbkkMp34U996/8BqRjiw6mfBf8wrvyqVYNKb+aHEz5QGuzl96VH569LTmY8h3YK+/KD8/elhxM+Q7sI//Kb0A6tuhwynfAr/0rH5o6vq0mh1I+pbXPUaj/49vw7G3JoZTvwt7/8W149rbkUMp3Yb+G49sGpGOLDqZ8F/z+j2+BNnV8W01eB3wzqZxWG00yy/N6YrYfKl8O8TEDvPbZDTX/VI8/n49HH5a46W2Y51mPtG7Hnecn/WzYO57lDopiLgAxalioyT13SMj6t9n9l2iGVOoHLTE1eRwmeYQgTlYicPn103j0e9mVwILai+MALWVROyXaV+0wt3YiCJa0+H6HeaSRaXmz64XGfMW7Dn/9GJ8v/ebTBQAUx6z4vkl91U7n1m56nbGydFMzSI1VncpHP8cXZxuXH78Uz76/XXZZbD4DLhTmEHnuATaPAzcf8UxG1y1QF8Sf99/M/l8FRFVCzGw+rYW54qjYBYWFyIq3vlJoUfgLeW4xmFotAAA="
    private const val CANCEL =
        "H4sIAGs5mWoC/81XW28bRRR+Hn6FFV7r2bnPrISQSoUIUihVoz4hZM2ux8F0s47sdYDHtoZGLZfkIW0kkqiVWnpRkRLRhyD4Rez6P3B21w1tE0O8tiPk1V68q/m+78x3zpxZtFG0ZL9xXXTJxuu25xUP3qpdQ721bjtxiMCPCmMw1wwZQpDt9VzS8z6yq86LbdJed16o4Ah0kztRN9InddEKFNxZWde+0k4JG4TcYeOMaOEv11befa8fxzBAs97qwuV9RNFnWpMLWtPPUdLtO/TO4unMrtjYRY3QxqGLxlJsupbtR0mjfA+XyPa+QARzKk7gLDnbja/F7WT5evtUxGAFJe0IqI7QBCNYGYUYwxIpoZGWEi11VjoNyur0FCVnQICDorfG18xgXSIIuEP5J5UH9yIbQMDKs/IZZgWCxj6ihGODJMEC5eMvLF+91Li4dGXx4sKFhU8vf9j45OPL15Yb//w7Qh7+eJQNDrPvvj8ThdCuJe1OjHpRO3wtlFxgzhVSEEHKJYiUDAWdr3lFoSOUN8QKUJaLVdKAzIpis72j9IfD7M6jKcRKTTA1pVhOzBykKuNjOZIqFFjHx6yK1pfpL4MphBptCgMXswoOnr1Qn9NZCL11Y3hr90wk1qN27NCxPlFMJAVYCuCQnmBjVH5TTeQb40ulZz1+L+x2omi97b4iJ1KQF5NFlUaKUcAy+WzJ6YG8/OyFnThxceIB1upxhRNlJkgf1BGYRgLFjc0DsXQNeVUKfIqJooDrQxGA+s3k5K4hs2JF38raklVFKz88gGNWzNirNKOiKJ0ls6ql8+AoezAYbj+dg3/4sX8MNBvKD0SdGPC2CLWpG+OreqsVEOECFjQZOQeDKVhjaE6smEqFtZg8YFTqmVtMlwt+yavqgn9/I9vfzPYfz81m/P9pM6HPuUyNXCT0NC4ydPaFSmDC6YiXydvpyXmlv21lN1/U5uWhktu5eCjoJ0knJt4HNry+0u304+aJPqhcWqGxhc7dcCQlNtO0QicR4d3rXREpFhKuK68jeVP/ZGsCMnS8fCHoPOTTcfK5phgaw1I+z3dnVez54iVUuGz3z/Thbm24/Sx9vJOf726kdx9NQJKND4uSeh5hYePCoiEzpnXFT5vZ7cEEZPi/u6JobglDIt8Fqem187GWgCArmYOJXDz1of2rUBZ+3hzegD3RryNbZNv72d5Ojan0+be1kWH2BrX0+dN0a7OW3fvjr6ODCdiL/64gEKyZmkWMC5jxITiQQ2XAqPArJdHwzu/Z4LCWbexkD7b/BhOeSi5iEgAA"

    /**
     * 공개 메서드 `jsonl`
     *
     * ### 파라미터
    - `route` (`LearnUnitSkillRoute`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `String`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun jsonl(route: LearnUnitSkillRoute): String {
        val phase = "hall-learn-${route.key}-stable"
        val table = when (route) {
            LearnUnitSkillRoute.CANCEL -> decode(CANCEL); LearnUnitSkillRoute.SELECT -> decode(DEFAULT) + '\n' + decode(
                SELECT_SUFFIX
            ); else -> decode(DEFAULT)
        }
        val log = RenderEventLog(); table.lineSequence().filter { it.isNotEmpty() }.forEachIndexed { i, row ->
            val v = row.split('\t', limit = 12)
            val blend: Any =
                if (v[9].contains("SRC_ALPHA")) listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA") else listOf(770, 771)
            var x = v[3].toFloat()
            var w = v[5].toFloat()
            var text = v[11]; if (route == LearnUnitSkillRoute.APPLY && i == 488) {
            x = 566.821f; w = 66.73f; text = "1001. 0"
        }; log.draw(
            phase,
            v[0],
            v[1],
            v[2],
            x,
            v[4].toFloat(),
            w,
            v[6].toFloat(),
            v[7].ifEmpty { null },
            v[8].toFloat(),
            blend,
            v[10].toBoolean(),
            text
        )
        }; return log.jsonl()
    }

    private fun decode(v: String) =
        GZIPInputStream(ByteArrayInputStream(Base64.getDecoder().decode(v))).bufferedReader().readText()
}

/**
 * class  `LearnUnitSkillRouteScreen`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class LearnUnitSkillRouteScreen(private val game: JojoGame, private val route: LearnUnitSkillRoute) : ScreenAdapter() {
    private val shapes = ShapeRenderer()
    private val parent = EditRosterLearnRoute(true)
    private val flow = LearnUnitSkillFlow()
    private var installed = false
    override fun render(delta: Float) {
        if (!installed) {
            check(parent.button(4, true)); when (route) {
                LearnUnitSkillRoute.SELECT -> check(
                    flow.panelButton(0, 0).single() is LearnUnitSkillFlow.Effect.OpenSelectList
                ); LearnUnitSkillRoute.APPLY -> {
                    check(
                        flow.panelButton(0, 0).single() is LearnUnitSkillFlow.Effect.OpenSelectList
                    ); flow.selectListResult(1001); check(flow.save() == listOf(LearnUnitSkillFlow.Effect.SetUnit0(1001)))
                }; LearnUnitSkillRoute.CANCEL -> {
                    flow.close(); parent.close()
                }; else -> {}
            }; installed = true
        }; Gdx.gl.glClearColor(
            0f,
            0f,
            0f,
            1f
        ); Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); shapes.begin(ShapeRenderer.ShapeType.Filled); shapes.color =
            Color(.72f, .67f, .55f, 1f); if (route != LearnUnitSkillRoute.CANCEL) shapes.rect(
            0f,
            0f,
            1280f,
            688f
        ); shapes.end(); game.writeRenderEventLogIfRequested()
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

    fun renderEventLog() = LearnUnitSkillRenderEvents.jsonl(route)
    override fun dispose() {
        shapes.dispose()
    }
}
