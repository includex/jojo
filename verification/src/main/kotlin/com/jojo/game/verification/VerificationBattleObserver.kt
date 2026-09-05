package com.jojo.game.verification

import com.jojo.game.application.runtime.RuntimeBattleCompletion
import com.jojo.game.application.runtime.RuntimeBattleFrameSnapshot
import com.jojo.game.application.runtime.RuntimeBattleObserver
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption

/** Verification-owned sink for neutral battle frame snapshots. */
internal class VerificationBattleObserver(
    private val outputPath: String,
) : RuntimeBattleObserver {
    private val frames = ArrayList<String>()

    override fun onFrame(snapshot: RuntimeBattleFrameSnapshot) {
        frames += snapshot.payload
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
    }
}
