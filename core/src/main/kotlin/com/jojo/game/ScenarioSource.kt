package com.jojo.game

/** Public source-first entry point used by the LibGDX game screens. */
object ScenarioSource {
    fun loadFirstInteractiveSegment(moduleName: String = "R_00", functionName: String = "scene1"): ScenarioTimeline =
        ScenarioMetadataReader.loadFirstInteractiveSegment(moduleName, functionName)
}
