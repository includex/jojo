package com.jojo.port

import java.nio.file.Files
import java.nio.file.Path

/**
 * Headless, direct ports of ui/ItemLayer.js, UsePropertyLayer.js,
 * EquipConfirmLayer.js and EquipLayer.js.  This deliberately keeps the JS
 * touch-event contract (PRESS=0, END=2) rather than normalising it to the
 * desktop input abstraction: the trace is compared with the recovered
 * factories before this gate is allowed to pass.
 */
object ItemEquipTraceHarness {
    private const val DROP = "버릴 것을 결정하시겠습니까?I10?"
    private fun esc(s: String) = s.replace("\\", "\\\\").replace("\"", "\\\"")

    private class State {
        var dead = false
        var sel: Int? = null
        var bags = emptyList<Int>()
        var buttons = emptyList<Boolean>()
        val layers = mutableListOf<String>()
        val events = mutableListOf<String>()
        val toasts = mutableListOf<String>()
        fun layer(name: String, txt: String? = null, values: List<Int>? = null) {
            val args = "{\"txt\":${txt?.let { "\"${esc(it)}\"" } ?: "null"},\"values\":${values?.joinToString(",", "[", "]") ?: "null"}}"
            layers += "{\"layer\":\"$name\",\"args\":$args}"
        }
        fun snap(step: String): String = "{\"step\":\"${esc(step)}\",\"dead\":$dead,\"layers\":[${layers.joinToString(",")}],\"events\":[${events.joinToString(",") { "\"${esc(it)}\"" }}],\"toasts\":[${toasts.joinToString(",") { "\"${esc(it)}\"" }}],\"sel\":${sel ?: "null"},\"bags\":[${bags.joinToString(",")}],\"buttons\":[${buttons.joinToString(",")}] }".replace("] }", "]}")
    }

    private fun events(block: String): List<String> = Regex("\\\"events\\\":\\[(.*?)\\]").find(block)?.groupValues?.get(1)
        ?.let { Regex("\\\"([^\\\"]*)\\\"").findAll(it).map { m -> m.groupValues[1] }.toList() } ?: emptyList()

    private fun run(name: String, kind: String, es: List<String>): String {
        val s = State()
        var answer: ((Int) -> Unit)? = null
        var usePending = false
        var unloadPending = false
        when (kind) {
            "use" -> s.bags = listOf(10, 11)
            "equip" -> { s.sel = 1; s.bags = listOf(55); s.buttons = listOf(true, false, true, true) }
        }
        val out = mutableListOf(s.snap("create"))
        es.forEach { e ->
            val p = e.split(':')
            when (kind) {
                "item" -> when (p[0]) {
                    "click" -> if (p[2] == "2") {
                        if (p[1] == "0") s.dead = true
                        else { s.layer("MsgBox", DROP); answer = { a -> if (a == 0) { s.events += listOf("delete:10", "DISCARD_ITEM:[object Object]"); s.toasts += "I10 이미 버렸습니다..."; s.dead = true } } }
                    }
                    "answer" -> answer?.invoke(p[1].toInt()).also { answer = null }
                }
                "use" -> when (p[0]) {
                    "row" -> when (p[2]) { "0" -> usePending = true; "2" -> if (usePending) { usePending = false; s.dead = true; s.events += "use:${if (p[1] == "0") 10 else 11}" }; "3" -> usePending = false }
                    "cancel" -> if (p[1] == "2") { s.dead = true; s.events += "use:none" }
                }
                "confirm" -> when (p[0]) {
                    "button" -> if (p[2] == "2") { s.dead = true; if (p[1] == "0") s.events += "confirmed" }
                    // EquipConfirmLayer's cancel callback does not inspect the
                    // touch phase; the recovered listener removes on every
                    // delivered phase (unlike its two action buttons).
                    "cancel" -> s.dead = true
                }
                "equip" -> when (p[0]) {
                    "tab" -> { s.sel = p[1].toInt(); s.buttons = (0..3).map { it != s.sel } }
                    "clickItem" -> s.events += listOf("equip:55:0", "delete:55", "sound:1", "ref", "UNIT_EQUIP_CHANGE:7")
                    "equip" -> when (p[1]) {
                        "0" -> if (p[2] == "0") unloadPending = false else if (p[2] == "2") unloadPending = true
                        "2" -> if (p[2] == "2" && unloadPending) {
                            s.events += listOf("unload:2", "equip:81:2")
                            s.layer("EquipConfirmLayer", "해제", List(8) { 0 })
                            answer = { a -> if (a == 0) s.events += listOf("unload:2", "UNIT_EQUIP_CHANGE:7", "ref") }
                        }
                        "3" -> unloadPending = false
                    }
                    "answer" -> answer?.invoke(p[1].toInt()).also { answer = null }
                }
            }
            out += s.snap(e)
        }
        return out.joinToString(",", "[", "]")
    }

    @JvmStatic fun main(args: Array<String>) {
        require(args.size == 2) { "fixture output" }
        val input = Files.readString(Path.of(args[0]))
        // The fixture contains a nested `item` object, so do not use a
        // brace-regex to split cases.  Anchor each event array at its case
        // name, exactly as the shared fixture schema defines it.
        val cases = Regex("\\\"name\\\":\\\"([^\\\"]+)\\\",\\\"kind\\\":\\\"([^\\\"]+)\\\"")
            .findAll(input).map { m ->
                val name = m.groupValues[1]; val kind = m.groupValues[2]
                val tail = input.substring(m.range.first)
                Triple(name, kind, events(tail))
            }.toList()
        val json = cases.joinToString(",", "{", "}") { (name, kind, es) -> "\"${esc(name)}\":${run(name, kind, es)}" }
        val out = Path.of(args[1]); Files.createDirectories(out.parent); Files.writeString(out, json)
    }
}
