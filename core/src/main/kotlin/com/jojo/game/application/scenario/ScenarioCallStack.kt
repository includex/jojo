// Scenario
package com.jojo.game.application.scenario

import com.jojo.game.*
import com.jojo.game.domain.scenario.*

import java.util.*

/** 시나리오 함수 호출 프레임과 레이블 점프를 관리한다. */
internal class ScenarioCallStack {
    /**
     * `frames` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val frames = ArrayDeque<Frame>()

    /** 모든 호출 프레임을 비운다. */
    fun clear() = frames.clear()

    /**
     * `pushFunction`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun pushFunction(
        name: String,
        label: String? = null,
        functions: Map<String, RuntimeFunction>,
        moduleName: String,
    ) {
        /**
         * `function` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

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

    /** 현재 함수 또는 대상 함수의 레이블로 실행 위치를 옮긴다. */
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
