// Verification
package com.jojo.game.verification

import com.badlogic.gdx.Gdx
import com.jojo.game.application.runtime.BattleTraceRuntimeConfig
import com.jojo.game.application.runtime.RuntimeBattleCompletion
import com.jojo.game.application.runtime.RuntimeBattleFrameSnapshot
import com.jojo.game.application.runtime.RuntimeBattleObserver
import com.jojo.game.domain.battle.BattleOutcome
import com.jojo.game.verification.trace.FullBattleTraceConfig
import com.jojo.game.verification.trace.FullBattleTraceEvidenceSession
import com.jojo.game.verification.trace.FullBattleTraceFinishSnapshot
import com.jojo.game.verification.trace.FullBattleTraceRecorder
import com.jojo.game.verification.trace.SourceRandomStreams

/**
 * VerificationBattleObserver: 중립 전투 프레임 스냅샷을 받아 전체 전투 추적 봉투로 기록하는 검증 전용 수집기이다.
 *
 * 프레임 누적과 봉투 직렬화는 [FullBattleTraceEvidenceSession]과 [FullBattleTraceRecorder]가 담당한다.
 * 관찰기는 중립 경계에서 받은 값만 그쪽으로 넘긴다.
 */
internal class VerificationBattleObserver(
    /** outputPath: 검증 산출물을 저장할 경로를 담는다. */
    private val outputPath: String,
    /** config: 검증 실행 설정을 담는다. */
    private val config: BattleTraceRuntimeConfig,
) : RuntimeBattleObserver {
    /** traceConfig: 중립 실행 설정을 추적 기록기의 설정으로 옮긴 값이다. */
    private val traceConfig = FullBattleTraceConfig(
        outputPath = outputPath,
        scenario = config.scenario,
        toolSeed = config.toolSeed,
        mathSeed = config.mathSeed,
        timeScale = config.timeScale,
        maxSimulationSeconds = config.maxSimulationSeconds,
        driverIntervalSeconds = config.driverIntervalSeconds,
        exitOnFinish = config.exitOnFinish,
    )

    // 난수는 전투 런타임이 core 쪽 스트림으로 굴린다. 여기의 스트림은 기록기가 요구하는 자리를
    // 채울 뿐이라 rng 배열은 비어 있다. 봉투의 rng는 검증기가 확인하지 않는다.
    private val recorder = FullBattleTraceRecorder(
        traceConfig,
        SourceRandomStreams(config.toolSeed, config.mathSeed),
    )

    /** session: 프레임 기록과 봉투 종료를 담당하는 증거 경계이다. */
    private val session = FullBattleTraceEvidenceSession(traceConfig, recorder)

    /** onFrame: 런타임 이벤트를 받아 검증 산출물을 갱신한다. */
    override fun onFrame(snapshot: RuntimeBattleFrameSnapshot) {
        snapshot.traceView?.let(session::record)
            ?: snapshot.payload.takeIf(String::isNotEmpty)?.let(recorder::addFrame)
    }

    /** onCompleted: 런타임 이벤트를 받아 검증 산출물을 갱신한다. */
    override fun onCompleted(completion: RuntimeBattleCompletion) {
        completion.inputs.forEach(session::recordInput)
        val evidence = completion.finish
        session.finish(
            completion.reason,
            FullBattleTraceFinishSnapshot(
                scenario = evidence?.scenario ?: config.scenario,
                requestedScenario = evidence?.requestedScenario ?: config.scenario,
                round = evidence?.round ?: 0,
                camp = evidence?.camp ?: -1,
                ended = evidence?.ended ?: false,
                outcome = evidence?.outcome?.let(BattleOutcome::valueOf),
                seededUnitIds = evidence?.seededUnitIds ?: emptyList(),
                loadedMapIndex = evidence?.loadedMapIndex ?: 0,
                mapName = evidence?.mapName ?: "",
                mapWidth = evidence?.mapWidth ?: 0,
                mapHeight = evidence?.mapHeight ?: 0,
            ),
        )
        if (completion.exitRequested) Gdx.app.exit()
    }
}
