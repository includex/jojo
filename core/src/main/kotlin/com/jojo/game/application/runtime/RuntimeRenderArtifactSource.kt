// Runtime
package com.jojo.game.application.runtime

import com.jojo.game.*

/** RuntimeRenderEventLogProvider: 현재 화면의 렌더링 이벤트 기록을 검증 도구에 제공하는 계약이다. */
interface RuntimeRenderEventLogProvider {
    fun runtimeRenderEventLog(): String
}

/** RuntimeCompositionTraceProvider: 화면 합성 순서와 레이어 정보를 텍스트 추적으로 제공하는 계약이다. */
interface RuntimeCompositionTraceProvider {
    fun runtimeCompositionTrace(): String
}
