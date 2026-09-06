// Runtime
package com.jojo.game.application.runtime

import com.jojo.game.*

/** RuntimeRenderEventLogProvider: 현재 화면의 렌더링 이벤트 기록을 검증 도구에 제공하는 계약이다. */
interface RuntimeRenderEventLogProvider {
    /**
     * `runtimeRenderEventLog`: 화면 표시 상태를 렌더링한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun runtimeRenderEventLog(): String
}

/** RuntimeCompositionTraceProvider: 화면 합성 순서와 레이어 정보를 텍스트 추적으로 제공하는 계약이다. */
interface RuntimeCompositionTraceProvider {
    /**
     * `runtimeCompositionTrace`: 해당 흐름을 실행하거나 다음 단계로 전달한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun runtimeCompositionTrace(): String
}
