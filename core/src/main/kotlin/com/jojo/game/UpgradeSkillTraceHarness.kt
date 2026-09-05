package com.jojo.game

import java.nio.file.Files
import java.nio.file.Path

/**
 * Direct, headless Kotlin games of ItemUpgradeLayer.js, StartItemLayer.js and
 * LearnUnitSkillLayer.js.  This belongs to the isolated comparison boundary:
 * no desktop input abstraction is allowed to alter the recovered listener
 * contract before its trace is compared with the original factory.
 */
object UpgradeSkillTraceHarness {
    private fun esc(v: String) = v.replace("\\", "\\\\").replace("\"", "\\\"")
    private fun events(block: String): List<String> = Regex("\\\"events\\\":\\[(.*?)]")
        .find(block)?.groupValues?.get(1)?.let { Regex("\\\"([^\\\"]*)\\\"").findAll(it).map { m -> m.groupValues[1] }.toList() } ?: emptyList()
    private fun field(block: String, name: String) = Regex("\\\"$name\\\":\\\"([^\\\"]*)\\\"").find(block)?.groupValues?.get(1)

    private class S(val kind: String) {
        var dead = false
        var active = true
        var callbacks = 0
        var skill: Int? = if (kind == "learn") 0 else null
        val layers = mutableListOf<String>()
        val events = mutableListOf<String>()
        val tf = linkedMapOf<String, Int>()
        val zs = linkedMapOf<String, Int>()
        val tz = linkedMapOf<String, Int>()
        var answer: ((Int) -> Unit)? = null
        var timer = false
        fun layer(name: String, txt: String? = null, flag: Int? = null, sel: Int? = null, size: Int? = null) {
            layers += "{\"layer\":\"$name\",\"args\":{\"txt\":${txt?.let { "\"${esc(it)}\"" } ?: "null"},\"flag\":${flag ?: "null"},\"sel\":${sel ?: "null"},\"listSize\":${size ?: "null"}}}"
        }
        fun map(m: Map<String, Int>) = m.entries.joinToString(",", "[", "]") { "[\"${it.key}\",${it.value}]" }
        fun snap(step: String) = "{\"step\":\"${esc(step)}\",\"dead\":$dead,\"active\":$active,\"layers\":[${layers.joinToString(",")}],\"events\":[${events.joinToString(",") { "\"${esc(it)}\"" }}],\"callbacks\":$callbacks,\"skill\":${skill ?: "null"},\"tf\":${map(tf)},\"zs\":${map(zs)},\"tz\":${map(tz)}}"
    }

    private fun modifyTf(s: S, attr: Int, value: Int) {
        // Recovered _modifyTF stores only values different from Model.skillAttr2.
        val original = if (attr == 4) 7 else if (attr == 3) 1 else attr
        if (original == value) s.tf.remove(attr.toString()) else s.tf[attr.toString()] = value
    }
    private fun save(s: S) {
        s.tf.forEach { (key, value) -> s.events += "tf:${s.skill ?: 0}:$key:$value" }
        s.zs.forEach { (key, value) -> s.events += "zs:$key:$value" }
        s.tz.forEach { (key, value) -> s.events += "tz:$key:$value" }
        s.tf.clear(); s.zs.clear(); s.tz.clear()
    }
    private fun selectSkill(s: S, id: Int) {
        if (s.skill == id) return
        s.skill = id
        if (s.tf.isNotEmpty() || s.zs.isNotEmpty() || s.tz.isNotEmpty()) {
            s.layer("MsgBox", "수정 사항이 감지되었습니다. 수정 사항을 저장하시겠습니까?", 3)
            // Important recovered ordering: maps are cleared immediately after
            // opening MsgBox, so its later yes callback cannot save them.
            s.answer = { if (it == 0) save(s) }
            s.tf.clear(); s.zs.clear(); s.tz.clear()
        }
    }
    private fun run(kind: String, es: List<String>): String {
        val s = S(kind)
        if (kind == "upgrade") s.timer = true
        val out = mutableListOf(s.snap("create"))
        for (event in es) {
            val p = event.split(':')
            when (kind) {
                "upgrade" -> when (p[0]) {
                    "cancel" -> if (p[1] == "2") { s.callbacks++; s.timer = false; s.dead = true }
                    "timer" -> if (s.timer) { s.callbacks++; s.timer = false; s.dead = true }
                }
                "start" -> when (p[0]) {
                    "choice" -> if (p[2] == "2") { s.events += "START_ITEM_CHOICE:${p[1]}"; s.active = false }
                    "cancel" -> if (p[1] == "2") s.active = false
                }
                "learn" -> when (p[0]) {
                    "touch" -> if (p[2] == "2") {
                        val index = p[1].toInt(); val sel = if (index == 0) 0 else 1
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

    @JvmStatic fun main(args: Array<String>) {
        require(args.size == 2) { "fixture output" }
        val raw = Files.readString(Path.of(args[0]))
        val matches = Regex("\\\"name\\\":\\\"([^\\\"]+)\\\",\\\"kind\\\":\\\"([^\\\"]+)\\\"").findAll(raw).toList()
        val result = matches.joinToString(",", "{", "}") { m ->
            val name = m.groupValues[1]; val kind = m.groupValues[2]
            "\"${esc(name)}\":${run(kind, events(raw.substring(m.range.first)))}"
        }
        val output = Path.of(args[1]); Files.createDirectories(output.parent); Files.writeString(output, result)
    }
}
