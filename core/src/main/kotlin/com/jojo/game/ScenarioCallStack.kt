package com.jojo.game
import com.jojo.game.domain.scenario.*

import java.util.*

internal class ScenarioCallStack {
    val frames = ArrayDeque<Frame>()

    /**
     * 공개 메서드 `clear`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun clear() = frames.clear()

    fun pushFunction(
        name: String,
        label: String? = null,
        functions: Map<String, RuntimeFunction>,
        moduleName: String,
    ) {
        val function = functions[name] ?: return
        if (label != null) {
            function.labelEntrypoints[label]?.let { entry ->
                frames.addLast(Frame(RuntimeFunction(function.name, entry, emptyMap(), function.labelEntrypoints)))
                return
            }
            error("$moduleName $name has no label $label")
        }
        frames.addLast(Frame(function))
    }

    /**
     * 공개 메서드 `jumpToLabel`
     *
     * ### 파라미터
    - `label` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `functions` (`Map<String, RuntimeFunction>`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun jumpToLabel(label: String, functions: Map<String, RuntimeFunction>) {
        val current = frames.peekLast()
        val currentIndex = current?.function?.labels?.get(label)
        if (currentIndex != null) {
            current.index = currentIndex + 1
            return
        }
        val target = functions.values.firstOrNull { label in it.labels || label in it.labelEntrypoints } ?: return
        val sourceFunction = current?.sourceFunction ?: target.name
        while (frames.peekLast()?.sourceFunction == sourceFunction) frames.removeLast()
        target.labelEntrypoints[label]?.let { entry ->
            frames.addLast(
                Frame(
                    RuntimeFunction(target.name, entry, emptyMap(), target.labelEntrypoints),
                    sourceFunction = sourceFunction
                )
            )
        } ?: frames.addLast(Frame(target, target.labels.getValue(label) + 1))
    }
}
