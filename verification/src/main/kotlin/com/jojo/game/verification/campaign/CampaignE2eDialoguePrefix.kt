package com.jojo.game.verification.campaign

import com.jojo.game.application.runtime.ScenarioActorRuntimeProbe
import com.jojo.game.application.runtime.ScenarioRuntimeProbe
import com.jojo.game.domain.scenario.PlaybackState

/** Records authored dialogue pages, independently of repeated frames or reveal inputs. */
internal class CampaignE2eDialoguePrefix(private val stopAt: CampaignE2eStopPoint) {
    data class Page(
        val module: String,
        val sceneIndex: Int,
        val revision: Long,
        val speakerId: String?,
        val text: String,
        val backgroundId: Int,
        val unitIds: List<Int>,
    )

    data class Frame(val time: Float, val playback: String, val dialogueRevision: Long, val actors: List<ScenarioActorRuntimeProbe>)
    private val recordedFrames = mutableListOf<Frame>()
    val frames: List<Frame> get() = recordedFrames.toList()

    private val observed = mutableListOf<Page>()
    val pages: List<Page> get() = observed.toList()

    fun observe(state: ScenarioRuntimeProbe) {
        if (state.module != stopAt.module || state.sceneIndex != stopAt.sceneIndex) return
        recordedFrames += Frame(state.elapsedSeconds, state.playback.name, state.dialogueRevision, state.actors.toList())
        if (state.playback != PlaybackState.DIALOGUE) return
        val text = state.dialogueText ?: return
        if (observed.any { it.revision == state.dialogueRevision }) return
        observed += Page(state.module, state.sceneIndex, state.dialogueRevision, state.dialogueSpeakerId, text, state.backgroundId, state.unitIds.sorted())
    }
}
