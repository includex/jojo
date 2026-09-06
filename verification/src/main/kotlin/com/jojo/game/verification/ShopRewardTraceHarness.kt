// Verification
package com.jojo.game.verification

import java.nio.file.Files
import java.nio.file.Path

/** ShopRewardTraceHarness: 검증 전용 상점·판매·보상 추적의 진입점이다. */
object ShopRewardTraceHarness {
    /** main: 검증 실행 흐름을 시작하고 종료 상태를 반환한다. */
    @JvmStatic
    /**
     * `main`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun main(args: Array<String>) {
        val result = linkedMapOf<String, String>()
        ShopRewardFixtureParser.parse(Files.readString(Path.of(args[0]))).forEach { fixture ->
            result[fixture.name] = ShopRewardTraceScenario.run(fixture)
        }
        val output = ShopRewardJson.objectValue(result.entries.map { it.key to it.value })
        val destination = Path.of(args[1])
        Files.createDirectories(destination.parent)
        Files.writeString(destination, output)
        println(output)
    }
}
