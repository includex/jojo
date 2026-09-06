// Verification
package com.jojo.game.verification.trace

import com.jojo.game.application.runtime.BattleTraceRecorder

/** FullBattleTraceFighter: 원본 프리팹 순서의 FightLayer 슬롯을 불변 값으로 투영한다. */
internal data class FullBattleTraceFighter(
    /** characterId: 검증 대상 무장 정보를 담는다. */
    val characterId: Int?,
    /** created: 생성 여부 여부를 나타낸다. */
    val created: Boolean,
    /** action: 검증 입력 정보를 담는다. */
    val action: Int?,
    /** actionElapsedSeconds: 검증 입력 정보를 담는다. */
    val actionElapsedSeconds: Float,
    /** parentX: 부모 X 좌표 값을 보관한다. */
    val parentX: Float,
    /** parentScaleX: 부모 X 배율 값을 보관한다. */
    val parentScaleX: Float,
    /** childX: 자식 X 좌표 값을 보관한다. */
    val childX: Float,
    /** childY: 자식 Y 좌표 값을 보관한다. */
    val childY: Float,
    /** childScaleX: 자식 X 배율 값을 보관한다. */
    val childScaleX: Float,
    /** opacity: 투명도 값을 보관한다. */
    val opacity: Float,
    /** zIndex: 깊이 순서 값을 보관한다. */
    val zIndex: Int,
    /** dead: 사망 여부 여부를 나타낸다. */
    val dead: Boolean,
)

/** FullBattleTraceSpeech: 전투 연출 대사의 활성 여부와 현재 표시 문구를 기록한다. */
internal data class FullBattleTraceSpeech(val active: Boolean, val renderedText: String)

/** FullBattleTraceFightSnapshot: 전투 연출의 배경 전환·양쪽 유닛 슬롯·대사 상태를 한 프레임으로 기록한다. */
internal data class FullBattleTraceFightSnapshot(
    /** mineIndex: 광산 인덱스 값을 보관한다. */
    val mineIndex: Int,
    /** enemyIndex: 적 인덱스 값을 보관한다. */
    val enemyIndex: Int,
    /** introBackgroundActive: 인트로 배경 표시 여부 여부를 나타낸다. */
    val introBackgroundActive: Boolean,
    /** duelBackgroundActive: 결투 배경 표시 여부 여부를 나타낸다. */
    val duelBackgroundActive: Boolean,
    /** startCrossFade: 시작 크로스페이드 여부 상태를 검증 흐름에 전달한다. */
    val startCrossFade: Float,
    /** slot0: 첫 번째 슬롯 상태를 검증 흐름에 전달한다. */
    val slot0: FullBattleTraceFighter,
    /** slot1: 두 번째 슬롯 상태를 검증 흐름에 전달한다. */
    val slot1: FullBattleTraceFighter,
    /** slot0Speech: 첫 번째 슬롯 대사 상태를 검증 흐름에 전달한다. */
    val slot0Speech: FullBattleTraceSpeech,
    /** slot1Speech: 두 번째 슬롯 대사 상태를 검증 흐름에 전달한다. */
    val slot1Speech: FullBattleTraceSpeech,
)

/** FullBattleTraceFightEvidence: 추적 JSON이 가변 렌더러에 의존하지 않도록 BattleScreen 밖에 둔 직렬화기이다. */
internal object FullBattleTraceFightEvidence {
    /** json: 검증 상태를 JSON으로 직렬화한다. */
    fun json(snapshot: FullBattleTraceFightSnapshot?): String {
        if (snapshot == null) return "null"
        val introOpacity = if (snapshot.introBackgroundActive) 1f - snapshot.startCrossFade else 0f
        val duelOpacity = if (snapshot.duelBackgroundActive) snapshot.startCrossFade else 0f
        return "{\"mineIndex\":${snapshot.mineIndex},\"enemyIndex\":${snapshot.enemyIndex}," +
                "\"backgrounds\":[[${snapshot.introBackgroundActive},${number(introOpacity)}]," +
                "[${snapshot.duelBackgroundActive},${number(duelOpacity)}]]," +
                "\"units\":[${fighter(snapshot.slot0)},${fighter(snapshot.slot1)}]," +
                "\"speeches\":[${speech(snapshot.slot0Speech)},${speech(snapshot.slot1Speech)}]}"
    }

    /** fighter: 전투 무장 식별 정보를 변환한다. */
    private fun fighter(value: FullBattleTraceFighter): String =
        "[${value.characterId ?: "null"},${value.created},${value.action ?: "null"},${number(value.actionElapsedSeconds)}," +
                "${number(value.parentX)},${number(value.parentScaleX)},${number(value.childX)},${number(value.childY)}," +
                "${number(value.childScaleX)},${number(value.opacity)},${value.zIndex},${value.dead}]"

    /** speech: 대사 렌더 이벤트를 구성한다. */
    private fun speech(value: FullBattleTraceSpeech): String =
        "[${value.active},\"${BattleTraceRecorder.escape(value.renderedText)}\"]"

    /** number: 문자열에서 수치 값을 읽는다. */
    private fun number(value: Float): String = BattleTraceRecorder.number(value)
}
