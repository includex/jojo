package com.jojo.engine.script

import java.util.ArrayDeque

/** 스크립트 함수 호출 프레임과 레이블 점프를 관리한다. */
class ScriptCallStack {
    val frames = ArrayDeque<ScriptFrame>()

    /** 모든 호출 프레임을 비운다. */
    fun clear() = frames.clear()

    fun pushFunction(
        name: String,
        label: String? = null,
        functions: Map<String, ScriptFunction>,
        moduleName: String,
    ) {
        val function = functions[name] ?: return
        if (label != null) {
            function.labelEntrypoints[label]?.let { entry ->
                frames.addLast(ScriptFrame(ScriptFunction(function.name, entry, emptyMap(), function.labelEntrypoints)))
                return
            }
            error("$moduleName $name has no label $label")
        }
        frames.addLast(ScriptFrame(function))
    }

    /** 현재 함수 또는 대상 함수의 레이블로 실행 위치를 옮긴다. */
    fun jumpToLabel(label: String, functions: Map<String, ScriptFunction>) {
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
                ScriptFrame(
                    ScriptFunction(target.name, entry, emptyMap(), target.labelEntrypoints),
                    sourceFunction = sourceFunction
                )
            )
        } ?: frames.addLast(ScriptFrame(target, target.labels.getValue(label) + 1))
    }
}
