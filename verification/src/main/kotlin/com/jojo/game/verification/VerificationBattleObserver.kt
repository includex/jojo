// Verification
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

/** VerificationBattleObserver: 중립 전투 프레임 스냅샷을 받는 검증 전용 수집기이다. */
internal class VerificationBattleObserver(
    /** outputPath: 검증 산출물을 저장할 경로를 담는다. */
    private val outputPath: String,
    /** config: 검증 실행 설정을 담는다. */
    private val config: BattleTraceRuntimeConfig,
) : RuntimeBattleObserver {
    /** frames: 검증 대상 목록을 담는다. */
    private val frames = ArrayList<String>()

    /** onFrame: 런타임 이벤트를 받아 검증 산출물을 갱신한다. */
    override fun onFrame(snapshot: RuntimeBattleFrameSnapshot) {
        snapshot.traceView?.let { frames += RuntimeBattleTraceJson.frame(it) }
            ?: snapshot.payload.takeIf(String::isNotEmpty)?.let(frames::add)
    }

    /** onCompleted: 런타임 이벤트를 받아 검증 산출물을 갱신한다. */
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
