// Verification
package com.jojo.game.verification

import com.jojo.game.*
import com.jojo.game.application.battle.bootstrap.BattleSceneCoordinator

import java.nio.file.Files
import java.nio.file.Path

/** BattleBootstrapTraceHarness: [BattleSceneCoordinator] 이벤트를 어댑트하며 상수·레지스트리는 원본 목록으로만 취급한다. */
object BattleBootstrapTraceHarness {
    /** q: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun q(s: String) = "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"") + "\""
    /** main: 검증 실행 흐름을 시작하고 종료 상태를 반환한다. */
    @JvmStatic
    /**
     * `main`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun main(args: Array<String>) {
        val raw = Files.readString(Path.of(args[0]))
        val cases = Regex("\"name\":\"([^\"]+)\",\"events\":\\[(.*?)]").findAll(raw)
        val json = cases.joinToString(",", "{", "}") { caseMatch ->
            val name = caseMatch.groupValues[1]
            val events = Regex("\"([^\"]+)\"").findAll(caseMatch.groupValues[2]).map { it.groupValues[1] }.toList()
            val resources = mutableListOf<String>()
            val layers = mutableListOf<String>()
            val log = mutableListOf<String>()
            val saves = mutableListOf<String>()
            val callbacks = mutableListOf<String>()
            val battleLayer = object : BattleSceneCoordinator.BattleScreen {
                /** save: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
                override fun save(out: MutableMap<String, Any?>) {
                    out["battleSaved"] = true; log += "battle.save"
                }

                /** filterUnits: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
                override fun filterUnits(flag: Int): List<Any?> = if (flag == 1187) listOf("m1", "m2") else listOf("e1")
            }
            /**
             * `factory` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val factory = object : BattleSceneCoordinator.Factory {
                /** addBattleScreen: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
                override fun addBattleScreen(data: Any?): BattleSceneCoordinator.BattleScreen {
                    layers += "{\"id\":[1,\"Battle/scene/BattleScreen\"],\"args\":{\"ms\":null,\"es\":null,\"flag\":null}}"; return battleLayer
                }

                /** addForcesList: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
                override fun addForcesList(mine: List<Any?>, enemy: List<Any?>, flag: Int) {
                    layers += "{\"id\":\"ForcesListLayer\",\"args\":{\"ms\":[${mine.joinToString(",") { q(it.toString()) }}],\"es\":[${
                        enemy.joinToString(
                            ","
                        ) { q(it.toString()) }
                    }],\"flag\":$flag}}"
                }

                /** stringify: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
                override fun stringify(value: Map<String, Any?>) =
                    "{\"battleSaved\":true,\"model\":{\"modelSaved\":true}}"
            }
            /**
             * `scene` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val scene = BattleSceneCoordinator(
                factory,
                { out -> out["modelSaved"] = true; saves += "model.save" },
                { index, payload -> log += "manager.saveGame:$index:$payload" },
                "prefab:battle",
                "prefab:init",
                "prefab:mini",
                "prefab:notice"
            )
            scene.onCreate(mapOf("scenario" to 1))
            events.forEach { event ->
                /**
                 * `kind` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val kind = event.substringBefore(':')
                /**
                 * `value` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val value = event.substringAfter(':'); when (kind) {
                "resource" -> {
                    /**
                     * `layer` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                     */

                    val layer = when (value) {
                        "Battle/scene/BattleScreen" -> BattleSceneCoordinator.Layer.BATTLE_LAYER; "Battle/scene/BattleInitLayer" -> BattleSceneCoordinator.Layer.BATTLE_INIT_LAYER; "Battle/scene/MiniMapLayer" -> BattleSceneCoordinator.Layer.MINI_MAP_LAYER; "Battle/scene/NoticeInfoLayer" -> BattleSceneCoordinator.Layer.NOTICE_INFO_LAYER; else -> null
                    }
                    /**
                     * `resource` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                     */

                    val resource =
                        layer?.let(scene::getResource); resources += "[${q(value)},${resource?.let { q(it.toString()) } ?: "null"}]"
                }

                "save" -> scene.saveGame(BattleSceneCoordinator.SaveRequest(value.toInt()) { callbacks += "save.callback" })
                "forces" -> scene.showCharacterList()
            }
            }
            q(name) + ":{\"resources\":[${resources.joinToString(",")}],\"layers\":[${layers.joinToString(",")}],\"log\":[${
                log.joinToString(
                    ",",
                    transform = ::q
                )
            }],\"modelSaves\":[${saves.joinToString(",", transform = ::q)}],\"callbacks\":[${
                callbacks.joinToString(
                    ",",
                    transform = ::q
                )
            }]}"
        }
        val out = Path.of(args[1]); Files.createDirectories(out.parent); Files.writeString(out, json)
    }
}
