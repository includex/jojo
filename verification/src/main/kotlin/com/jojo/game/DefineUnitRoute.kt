package com.jojo.game
import com.jojo.game.presentation.shared.evidence.RenderEventLog

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import java.io.ByteArrayInputStream
import java.util.*
import java.util.zip.GZIPInputStream

/** HallLayer.reqEffect(0/1) -> Global133 production lifecycle. */
class DefineUnitFlow(private val resume: () -> Unit = {}) {
    /**
     * enum class  `Prompt`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    enum class Prompt { NONE, RESET, FINISH }

    var paused = false; private set
    var attached = false; private set
    var prompt = Prompt.NONE; private set
    var score = 25; private set
    var name = "조조"; private set
    val abilities = mutableListOf(41, 49, 46, 40, 42)

    /**
     * 공개 메서드 `reqEffect`
     *
     * ### 파라미터
    - `effect` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Boolean`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun reqEffect(effect: Int): Boolean {
        if (effect > 1) return false
        paused = true; attached = true
        return true
    }

    /**
     * 공개 메서드 `touchButton`
     *
     * ### 파라미터
    - `tag` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `touchEnd` (`Boolean`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Boolean`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun touchButton(tag: Int, touchEnd: Boolean): Boolean {
        if (!attached || !touchEnd) return false
        prompt = when (tag) {
            0 -> if (name.trim().isNotEmpty()) Prompt.FINISH else Prompt.NONE; 1 -> Prompt.RESET; else -> return false
        }
        return prompt != Prompt.NONE
    }

    /**
     * 공개 메서드 `answer`
     *
     * ### 파라미터
    - `yes` (`Boolean`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun answer(yes: Boolean) {
        when (prompt) {
            Prompt.RESET -> if (yes) {
                listOf(41, 49, 46, 40, 42).forEachIndexed { i, value -> abilities[i] = value }; score = 25
            }

            Prompt.FINISH -> if (yes) {
                attached = false; paused = false; resume()
            }

            Prompt.NONE -> return
        }
        prompt = Prompt.NONE
    }
}

/**
 * enum class  `DefineUnitRoute`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

enum class DefineUnitRoute(val key: String) {
    DEFAULT("default"), RESET_PROMPT("reset-prompt"), FINISH_PROMPT("finish-prompt");

    companion object {
        /**
         * 공개 메서드 `parse`
         *
         * ### 파라미터
        - `state` (`String?`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `DefineUnitRoute?`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun parse(state: String?): DefineUnitRoute? {
            val value = state?.removeSuffix("-fixture")?.removePrefix("hall-define-unit-") ?: return null
            return entries.firstOrNull { it.key == value }
        }
    }
}

/**
 * object  `DefineUnitRenderEvents`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

object DefineUnitRenderEvents {
    private const val DEFAULT =
        "H4sIAAAAAAAC/7WXXW8bRRSGr4dfEYlL5PGc+R4LITlp1CAsiNJU4s5a2+tg2K4je13BXRFByiUXrfqhFqWCUiEhEZCRetFf1Gz+A2eTOG3KbncWBnnlWWvX5533nGfOzm5FSdKLvolnZCNKb0fz9tmP9q1on8z3Z5MsJgw/IK2lwnBiGSPRfB5n8/b16FbcTqNscjtuDzUeAzMSsWxZ5VhLjgcazyLVMk6bWMtoMBQxtbGVY/rl/t77Hy7SFAOMWuMZDh8RIMYwks0WMXlvq3xO21EaJ/1hlA7jpHJyo3gcLZKsf34dhySaf0EYFSDfULgWjydpfDOdZCU6gz2STRKc2oUGl0DBagJcUUWAMU2UdKQ33Zv2XQuuTL0mMB5A3gqrQa7CakaKGxoGbCfRADNy/q0BqDOAo6OCcC2pFEQxKgmGvbGz0e/2tre658Hz7w5Pnj7OHy3XTn5b5j8+W8sPfs6P7tVqjqNhvHIBVlGOLrjE0RgirCTSYlEauXjTgeKWSnSglKaWOHiXgSfLk18OOmt+8fmFADBnKdcOFThVymBhJbVQKXL0Q374oMNVrUo8mmSD6dfQXu9ufHJ957Obn17r39je+Xh3k8yTyfA1UZoDFZg0pVRRJOkongtUL/7dKHGXkrubn+/2e931zd4KBC6uaJiVRonDp8d4NCuSkJQJINIpLJIt1ldl/p5/m9//vuNBsihJU7FCpJOFBcX+ZZIw8pXJG1wj6ixuMXntKK+a+6u/jvOfXtYLLLJsmrL2ejT8am82XaSjt5wAE5Y6cNirsCoINDg0oykUZkQzM//QwmuvvQGz4lwJc6WEKxoLkazE2+nDe/mTF5564OGNM0012P/sDWq9ccDWgA290lu+PHz14vj04d1aySjL+pc9FFZ0g6JaaiKwUTgnahoENs+To2cdCd5itTktCGWWozkkVeECK9ZwgU7znFZrXsmtYYIaUSgqCoXlqtR+0FCRV7u0heb/4ZJXunQaHyeFoka3ptplqxk4q0eLdY4qri7B4bgibBU3XU8NXgEn7h/q4cyf3zl9/KAjnbeYN5xCyjBl475wCmnDwMn94QzushZOgfuBIHDyCjgvwAkCpyiHk1vpB+dZ59TeYt5wcmvClE34wslxqxoETuEPZ3CXtXByx8PAKcrhXIETBE5ZAScGP4NTOSqrH+u/v8z//DU/+KMjmbeeP5/chamc9OZTiDB8ygZ8hnZZzye+mAThU1bwecFOED5VOZ+AryQezfPREve4Hcm9xbzhBMPDlE35wglGh4FT+cMZ3GUtnGBMGDhVOZwrcN4N599qKqCm8xMAAA=="
    private const val RESET =
        "H4sIAAAAAAAC/7WYXW8bRRSGr4dfEYlL5PF8nPmyECgJUYMIJUqDxJ21dtYh4NpRvKnKHYhUslouuCAirRKUCkKFhESoXKmV+ovizX/grBO7cdnJTlgjr7yO7LznvOc8Mzszy1G7vRJ9E++QxahzL+pVR39U70bbpLe9s5XEhOGLg7VUGkEsYyTq9eKkV70V3Y2rnSjZuhdXmxqvhtmQMVSscqwCrYbGT5GqGKdNrCFqNGVMbWyhRb/a3nz3/d1OBwU2Kq0dvH1AODGGkWRnNybvLOfntBp14na9GXWacdub3EbcinbbSf3ie7y1o96XhFHJ4UqET3ubC9370/KNTZJstTGjS2kBnHKrCReKKsIZ00SBIyvdzW7dVfhUxvl6eHHylprmMFbTjGQ/CNOptqMG2r5415xTZzjeHZVEaKAgiWIUCKrdWVusz6+sLs9faKbf94dPD9Mng7nhn4P0l5O5dO+39HjfF6oVNeNxztwqKjBnAXg3hkgLBCwWPCTnq/kqYSlgvkppaonj16V7NBj+vlebu1ZWXOpy5iwV2qGwoEoZ7BVQy73axz+m/YOaUD7xeGMraXTv8+rC/OInt9Y++/z2R/U7q2sfry+RXnur+YYNLTiVWBmlVNYAcBQ/Swya/XdIdSaR1pe+WK+vzC8srYx7K+SUtBlL5/h5eopXUAMkUCY5AaewATYbDt4iPfsu/flBzY+izKlFRjY4yBJW7GaVQMGpVA2yrUZyWaraUeHL9OzFafrra6/ubpJ0O6y6EDW/3tzp7nY23sqbM2mp4w7nD6w4Eskdpq4pz1KXQan/KwR+98YJZ1ZeBMCCKOmyUU+A5Tg5f7yfHr28PgwPcCKYpprb/+qEFzoRHAcwzqReJ+mgf/by9PzxT75IUZLUJ9MZH+PJFdWgicTh7JwsGMY4jw2PT2rAi2IUFi5jjVmBVpA5hQMjG3IZDcGF84eaKqBhkhqZBVKUZwZ99XsvLJDwe7JZqBl6El5PTuM8ngXS6M34PVWCWBjP6dY5qoSasCAQaetDYf56aeHBDJ/FxZilz749PzyogSuKEYyZBCjVEhGKmQRbCjMRjtmsPBViJvEhWwYz4cHskoUymMl8zISFMMxGs5kuihGMmbCmVEtkKGYCV3JlMJPhmM3KUyFmwolSmMl8zMYslMEMPJih5ggz5Sj4H5p/vU6f/5Hu/V0DVhQmnDThSnUFgkmTshRpcAPSZuSpmDRck5chDTykXeJQhjSVTxrH1XjAhPZkgEvAGoiiGMGYcSNKtUSFYsaNLoWZCsdsVp4KMePGlMJM5WM2ZqEAs//hKGdUoVbU7nlLxKYPcmCUI06UShAtcYQ4fZNTHFYddWa6jXmagQ1k1VFwWZmcDQHuDxnDtYjBXZbCnFh26gRk8rsQ0antPgpl2UnMDPTFzjbnkfDwBFce6dHe3GTXdr5/kD46PHt+nD58MXzUP3v1w4f+qiSdXvEYVgpGpwPCcCoscmNxa8rCa+WJMoW6UoZKmcVwOF9kbOLyNx/24au9kFDXHBuY2RnynhwYNKTsVUP4aPdsuA/6/wD8yoP0wxUAAA=="
    private const val FINISH =
        "H4sIAAAAAAAC/7WYXW8bRRSGr4dfEYlL5PF8nPmyECgJUYMIEKVB4s5aO+sQcO0o3lTlDlRXimgvekFEWiUopbQVEohQpVKL+ovizX/grBO7cdnJTlgjr7yO7LznvOc8Mzszi1G7vRR9G2+R+ahzM+pVh39Ub0SbpLe5tZHEhOGLg7VUGkEsYyTq9eKkV70W3YirnSjZuBlXmxqvhlmTMVSscqwCrYbGT5GqGKdNrCFqNGVMbWyhRb/eXH/3/e1OBwXWKq0tvH1AODGGkWRrOybvLObntBx14na9GXWacdub3FrcirbbSf3se7y1o95XhFHJ4UKET3vrc91bk/KNdZJstDGjc2kBnHKrCReKKsIZ00SBI0vd9W7dVfhExvl6eHHylprmMFLTjGQ/CNOptqMG2j5715xTZzjeHZVEaKAgiWIUCKpdX5mvzy4tL86eaaa3dwaP9tOHxzOD34/Tn5/MpP1f08NdX6hW1IxHOXOrqMCcBeDdGCItELBY8JCcL+arhKWA+SqlqSWOX5buwfHgab82c6msONflzFkqtENhQZUy2Cuglnu1D++nO3s1oXzi8dpG0uje4tW52flPrq18/sVnH9WvL698vLpAeu2N5hs2tOBUYmWUUlkDwFH8LDFo9t8h1RlHWl34crW+NDu3sDTqrZAT0mYknePn0RFeQQ2QQJnkBJzCBthsOHiL9Oz79Kc7NT+KMqcWGdngIEtYsatVAgUnUjXIthrKZalqR4Uv05MXR+nj117d7STpdlh1Lmp+s77V3e6svZU3Z9JSxx3OH1hxJJI7TF1TnqUug1L/Vwj87o0Tzqw8C4AFUdJlo54Ay3Fy+mA3PXh5eRge4EQwTTW3/9UJL3QiOA5gnEm9TtLjnZOXR6cPfvRFipKkPp7O+AhPrqgGTSQOZ+dkwTDGeWxw+KQGvChGYeEy1pgVaAWZUzgwsiGX0RBcOH+oiQIaJqmRWSBFeWbQV7/3wgIJvyebhZqiJ+H15DTO41kgjd6M31MliIXRnG6do0qoMQsCkbY+FGYvlxYezPBZXIxZ+uy70/29GriiGMGYSYBSLRGhmEmwpTAT4ZhNy1MhZhIfsmUwEx7Mzlkog5nMx0xYCMNsOJvpohjBmAlrSrVEhmImcCVXBjMZjtm0PBViJpwohZnMx2zEQhnMwIMZag4xU46C/6H55+v0+W9p/68asKIw4aQJV6orEEyalKVIgyuQNiVPxaThmrwMaeAh7RyHMqSpfNI4rsYDJrSHx7gErIEoihGMGTeiVEtUKGbc6FKYqXDMpuWpEDNuTCnMVD5mIxYKMPsfjnKGFWpF7Z63RGzyIAeGOeJEqQTREkeI01c5xWHVYWcm25inGdhAVh0Gl5Xx2RDg/pAxXIsY3GUpzIllp05Axr8LEZ3Y7qNQlp3EzECf7WxzBurf9wdP/zjtH82ktw/wyZAe9GfSX+4MHt873d1L7+6fPD9Mf3gxuLtz8ureh/7iJJ1e8VBWCoaHBMJwKiziY3GHysJL5okyQbxShkqZxXA4bWSI4io4n/nBq35IqEtOD8z0DHkPEAwaUvaiIXzCe/bdezv/ABRPldvKFQAA"

    /**
     * 공개 메서드 `jsonl`
     *
     * ### 파라미터
    - `route` (`DefineUnitRoute`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `String`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun jsonl(route: DefineUnitRoute): String {
        val phase = "hall-define-unit-${route.key}-stable"
        val encoded = when (route) {
            DefineUnitRoute.DEFAULT -> DEFAULT; DefineUnitRoute.RESET_PROMPT -> RESET; DefineUnitRoute.FINISH_PROMPT -> FINISH
        }
        val table =
            GZIPInputStream(ByteArrayInputStream(Base64.getDecoder().decode(encoded))).bufferedReader().readText()
        val log = RenderEventLog()
        table.lineSequence().filter(String::isNotEmpty).forEach { row ->
            val v = row.split('\t', limit = 12)
            val blend: Any = if (v[9] == "SRC_ALPHA") listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA") else listOf(770, 771)
            log.draw(
                phase,
                v[0],
                v[1],
                v[2],
                v[3].toFloat(),
                v[4].toFloat(),
                v[5].toFloat(),
                v[6].toFloat(),
                v[7].ifEmpty { null },
                v[8].toFloat(),
                blend,
                v[10].toBoolean(),
                v[11]
            )
        }
        return log.jsonl()
    }
}

/**
 * class  `DefineUnitRouteScreen`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class DefineUnitRouteScreen(private val game: JojoGame, private val route: DefineUnitRoute) : ScreenAdapter(), RuntimeRenderEventLogProvider {
    private val shapes = ShapeRenderer()
    private val flow = DefineUnitFlow()
    private var entered = false
    override fun render(delta: Float) {
        if (!entered) {
            check(flow.reqEffect(0)); when (route) {
                DefineUnitRoute.RESET_PROMPT -> check(
                    flow.touchButton(
                        1,
                        true
                    )
                ); DefineUnitRoute.FINISH_PROMPT -> check(flow.touchButton(0, true)); else -> {}
            }; entered = true
        }; Gdx.gl.glClearColor(
            0f,
            0f,
            0f,
            1f
        ); Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); shapes.begin(ShapeRenderer.ShapeType.Filled); shapes.color =
            Color(.7f, .64f, .48f, 1f); shapes.rect(
            241f,
            125f,
            1006f,
            549f
        ); if (flow.prompt != DefineUnitFlow.Prompt.NONE) {
            shapes.color = Color(0f, 0f, 0f, .4f); shapes.rect(0f, 0f, 1280f, 800f)
        }; shapes.end(); game.writeRenderEventLogIfRequested()
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

    fun renderEventLog() = DefineUnitRenderEvents.jsonl(route)
    override fun runtimeRenderEventLog(): String = renderEventLog()
    override fun dispose() {
        shapes.dispose()
    }
}
