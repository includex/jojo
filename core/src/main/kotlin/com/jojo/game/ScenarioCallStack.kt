package com.jojo.game

import java.util.ArrayDeque

internal class ScenarioCallStack {
    val frames = ArrayDeque<Frame>()

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
            frames.addLast(Frame(RuntimeFunction(target.name, entry, emptyMap(), target.labelEntrypoints), sourceFunction = sourceFunction))
        } ?: frames.addLast(Frame(target, target.labels.getValue(label) + 1))
    }
}
