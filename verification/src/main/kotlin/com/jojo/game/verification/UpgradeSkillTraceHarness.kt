// Verification
package com.jojo.game.verification

import com.jojo.game.*

import java.nio.file.Files
import java.nio.file.Path


/** UpgradeSkillTraceHarness: ItemUpgradeLayer.js·StartItemLayer.js·LearnUnitSkillLayer.js를 헤드리스로 직접 실행한다. 격리 비교 경계에서 원본 팩토리와 추적하기 전 데스크톱 입력 추상화가 리스너 계약을 바꾸지 않도록 한다. */
object UpgradeSkillTraceHarness {
    /** esc: JSON 특수 문자를 이스케이프해 안전한 문자열을 만든다. */
    private fun esc(v: String) = v.replace("\\", "\\\\").replace("\"", "\\\"")
    /** events: 입력 데이터에서 검증 이벤트 목록을 추출한다. */
    private fun events(block: String): List<String> = Regex("\\\"events\\\":\\[(.*?)]")
        .find(block)?.groupValues?.get(1)
        ?.let { Regex("\\\"([^\\\"]*)\\\"").findAll(it).map { m -> m.groupValues[1] }.toList() } ?: emptyList()

    /** field: 입력 데이터에서 지정한 블록을 추출한다. */
    private fun field(block: String, name: String) =
        Regex("\\\"$name\\\":\\\"([^\\\"]*)\\\"").find(block)?.groupValues?.get(1)

    /** S: 검증 시나리오의 상태와 동작을 제공하는 타입이다. */
    private class S(val kind: String) {
        /**
         * `dead` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var dead = false
        /**
         * `active` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var active = true
        /**
         * `callbacks` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var callbacks = 0
        /**
         * `skill` (Int?): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var skill: Int? = if (kind == "learn") 0 else null
        /**
         * `layers` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val layers = mutableListOf<String>()
        /**
         * `events` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val events = mutableListOf<String>()
        /**
         * `tf` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val tf = linkedMapOf<String, Int>()
        /**
         * `zs` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val zs = linkedMapOf<String, Int>()
        /**
         * `tz` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val tz = linkedMapOf<String, Int>()
        /**
         * `answer` (((Int) -> Unit)?): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var answer: ((Int) -> Unit)? = null
        /**
         * `timer` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var timer = false


        /** layer: 검증 입력을 처리하고 관련 상태를 갱신한다. */
        fun layer(name: String, txt: String? = null, flag: Int? = null, sel: Int? = null, size: Int? = null) {
            layers += "{\"layer\":\"$name\",\"args\":{\"txt\":${txt?.let { "\"${esc(it)}\"" } ?: "null"},\"flag\":${flag ?: "null"},\"sel\":${sel ?: "null"},\"listSize\":${size ?: "null"}}}"
        }


        /** map: 검증 입력을 처리하고 관련 상태를 갱신한다. */
        fun map(m: Map<String, Int>) = m.entries.joinToString(",", "[", "]") { "[\"${it.key}\",${it.value}]" }


        /** snap: 현재 추적 상태를 스냅샷으로 만든다. */
        fun snap(step: String) =
            "{\"step\":\"${esc(step)}\",\"dead\":$dead,\"active\":$active,\"layers\":[${layers.joinToString(",")}],\"events\":[${
                events.joinToString(",") { "\"${esc(it)}\"" }
            }],\"callbacks\":$callbacks,\"skill\":${skill ?: "null"},\"tf\":${map(tf)},\"zs\":${map(zs)},\"tz\":${map(tz)}}"
    }

    /** modifyTf: 검증 입력을 처리하고 관련 상태를 갱신한다. */
    private fun modifyTf(s: S, attr: Int, value: Int) {
        // 복원한 _modifyTF는 Model.skillAttr2와 다른 값만 저장한다.
        val original = if (attr == 4) 7 else if (attr == 3) 1 else attr
        if (original == value) s.tf.remove(attr.toString()) else s.tf[attr.toString()] = value
    }

    /** save: 검증 산출물을 지정한 경로에 기록한다. */
    private fun save(s: S) {
        s.tf.forEach { (key, value) -> s.events += "tf:${s.skill ?: 0}:$key:$value" }
        s.zs.forEach { (key, value) -> s.events += "zs:$key:$value" }
        s.tz.forEach { (key, value) -> s.events += "tz:$key:$value" }
        s.tf.clear(); s.zs.clear(); s.tz.clear()
    }

    /** selectSkill: 검증 입력 선택을 적용해 다음 상태로 진행한다. */
    private fun selectSkill(s: S, id: Int) {
        if (s.skill == id) return
        s.skill = id
        if (s.tf.isNotEmpty() || s.zs.isNotEmpty() || s.tz.isNotEmpty()) {
            s.layer("MsgBox", "수정 사항이 감지되었습니다. 수정 사항을 저장하시겠습니까?", 3)
            // 복원한 순서상 MsgBox를 연 직후 맵을 비우므로, 나중의 예 콜백은 해당 값을 저장할 수 없다.
            s.answer = { if (it == 0) save(s) }
            s.tf.clear(); s.zs.clear(); s.tz.clear()
        }
    }

    /** run: 검증 시나리오 입력을 적용하고 추적 결과를 반환한다. */
    private fun run(kind: String, es: List<String>): String {
        val s = S(kind)
        if (kind == "upgrade") s.timer = true
        val out = mutableListOf(s.snap("create"))
        for (event in es) {
            val p = event.split(':')
            when (kind) {
                "upgrade" -> when (p[0]) {
                    "cancel" -> if (p[1] == "2") {
                        s.callbacks++; s.timer = false; s.dead = true
                    }

                    "timer" -> if (s.timer) {
                        s.callbacks++; s.timer = false; s.dead = true
                    }
                }

                "start" -> when (p[0]) {
                    "choice" -> if (p[2] == "2") {
                        s.events += "START_ITEM_CHOICE:${p[1]}"; s.active = false
                    }

                    "cancel" -> if (p[1] == "2") s.active = false
                }

                "learn" -> when (p[0]) {
                    "touch" -> if (p[2] == "2") {
                        val index = p[1].toInt()
                        val sel = if (index == 0) 0 else 1
                        s.layer("SelectListLayer", sel = sel, size = 4)
                        s.answer = { choice -> if (choice >= 0) modifyTf(s, index, choice) }
                    }

                    "select" -> s.answer?.also { s.answer = null }?.invoke(p[1].toInt())
                    "skill" -> selectSkill(s, p[1].toInt())
                    "answer" -> s.answer?.also { s.answer = null }?.invoke(p[1].toInt())
                    "save" -> if (p[1] == "2") save(s)
                    "close" -> if (p[1] == "2") s.dead = true
                    "edit" -> modifyTf(s, 4, p[1].toInt().let { if (it < 0 || it >= 255) 255 else it })
                }
            }
            out += s.snap(event)
        }
        return out.joinToString(",", "[", "]")
    }

    /** main: 검증 실행 흐름을 시작하고 종료 상태를 반환한다. */
    @JvmStatic
    /**
     * `main`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun main(args: Array<String>) {
        require(args.size == 2) { "fixture output" }
        val raw = Files.readString(Path.of(args[0]))
        val matches = Regex("\\\"name\\\":\\\"([^\\\"]+)\\\",\\\"kind\\\":\\\"([^\\\"]+)\\\"").findAll(raw).toList()
        val result = matches.joinToString(",", "{", "}") { m ->
            val name = m.groupValues[1]
            val kind = m.groupValues[2]
            "\"${esc(name)}\":${run(kind, events(raw.substring(m.range.first)))}"
        }
        val output = Path.of(args[1]); Files.createDirectories(output.parent); Files.writeString(output, result)
    }
}
