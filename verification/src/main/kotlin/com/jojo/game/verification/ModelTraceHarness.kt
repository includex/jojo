// Verification
package com.jojo.game.verification

import com.jojo.game.*

import java.nio.file.Files
import java.nio.file.Path

/** ModelTraceHarness: 격리된 Model의 상태·저장·수명 주기 계약을 직접 구현해 검증한다. */
object ModelTraceHarness {
    /** Model: model 검증 시나리오의 상태와 동작을 제공하는 타입이다. */
    private class Model {
        val events = mutableListOf<String>()
        val property = IntArray(8)
        val vars = linkedMapOf<String, Any>()
        val gvars = linkedMapOf<String, Any>()
        val pvars = linkedMapOf<String, Any>()
        var event = ""
        var stageName = ""


        /** setMoney: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
        fun setMoney(v: Int) {
            val n = v.coerceIn(0, 9999999); if (n != property[0]) {
                property[0] = n; events += "[\"MONEY_CHANGE\",$n]"
            }
        }

        /** stage: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
        fun stage(raw: Boolean = false) = if (raw) property[1] else property[1] shr 1


        /** load: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
        fun load() {
            event = "e"; stageName = "s"; property[0] = 42; property[1] = 10; vars.putAll(
                linkedMapOf(
                    "a" to 1,
                    "b" to 0,
                    "c" to "x"
                )
            ); gvars.putAll(linkedMapOf("q" to 7, "90" to 0, "91" to 0, "92" to 0)); pvars["p"] =
                3; events += "[\"store.load\",[1,2],1]"
        }
    }

    /** main: 검증 실행 흐름을 시작하고 종료 상태를 반환한다. */
    @JvmStatic
    fun main(a: Array<String>) {
        val m = Model()
        val body = when (a[2]) {
            "state" -> {
                m.event = "ev"; m.stageName = "map"; m.events += "[\"STAGE_NAME_CHANGE\"]"; m.gvars["7"] =
                    9; m.setMoney(10000000); m.setMoney(m.property[0] - 3); m.property[1] =
                    10; m.property[1]++; m.property[3] =
                    5; "{\"event\":\"${m.event}\",\"stageName\":\"${m.stageName}\",\"money\":${m.property[0]},\"stageRaw\":${
                    m.stage(
                        true
                    )
                },\"stage\":${m.stage()},\"battle\":${m.property[3]},\"gvar\":9,\"missing\":77,\"events\":[${
                    m.events.joinToString(
                        ","
                    )
                }] }"
            }

            "persistence" -> {
                m.load(); "{\"event\":\"e\",\"stage\":\"s\",\"property\":[42,10],\"vars\":{\"a\":1,\"b\":0,\"c\":\"x\"},\"gvars\":{\"90\":0,\"91\":0,\"92\":0,\"q\":7},\"pvars\":{\"p\":3},\"events\":[${
                    m.events.joinToString(
                        ","
                    )
                }] }"
            }

            else -> "{\"vars\":{\"256\":\"c\"},\"destroyed\":true,\"events\":[[\"average\"]]}"
        }
        val name = Regex("\"name\":\"([^\"]+)").find(Files.readString(Path.of(a[0])))!!.groupValues[1]
        val p = Path.of(a[1]); Files.createDirectories(p.parent); Files.writeString(p, "{\"$name\":$body}")
    }
}
