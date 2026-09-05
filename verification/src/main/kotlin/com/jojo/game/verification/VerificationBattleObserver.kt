package com.jojo.game.verification

import com.jojo.game.application.runtime.RuntimeBattleCompletion
import com.jojo.game.application.runtime.RuntimeBattleFrameSnapshot
import com.jojo.game.application.runtime.RuntimeBattleObserver
import com.jojo.game.application.runtime.BattleTraceRuntimeConfig
import com.badlogic.gdx.Gdx
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import com.jojo.game.verification.trace.RuntimeBattleTraceJson

/** Verification-owned sink for neutral battle frame snapshots. */
internal class VerificationBattleObserver(
    private val outputPath: String,
    private val config: BattleTraceRuntimeConfig,
) : RuntimeBattleObserver {
    private val frames = ArrayList<String>()

    override fun onFrame(snapshot: RuntimeBattleFrameSnapshot) {
        snapshot.traceView?.let { frames += RuntimeBattleTraceJson.frame(it) }
            ?: snapshot.payload.takeIf(String::isNotEmpty)?.let(frames::add)
    }

    override fun onCompleted(completion: RuntimeBattleCompletion) {
        val path = Path.of(outputPath)
        path.parent?.let(Files::createDirectories)
        Files.writeString(
            path,
            frames.joinToString("\n", postfix = if (frames.isEmpty()) "" else "\n"),
            StandardOpenOption.CREATE,
            StandardOpenOption.TRUNCATE_EXISTING,
            StandardOpenOption.WRITE,
        )
        if (completion.exitRequested) Gdx.app.exit()
    }
}
