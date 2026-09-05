package com.jojo.game

import java.nio.file.Files
import java.nio.file.Path

/** CLI game counterpart of the recovered-JS MenuLayer trace harness. */
object MenuLayerTraceHarness {
    @JvmStatic
    fun main(args: Array<String>) {
        val fixture = Files.readString(Path.of(args.single()))
        val cases =
            Regex("""\{"id":"([^"]+)","weather":"([^"]+)","round":(\d+),"maxRound":(\d+),"flag":(\d+),"edit":(true|false)(?:,"switchWeather":"([^"]+)")?,"events":\[([^]]*)],"samples":\[([^]]*)]}""")
                .findAll(fixture).map { it.groupValues }.toList()
        print(cases.joinToString(prefix = "[", postfix = "]") { trace(it) }); println()
    }

    private fun trace(v: List<String>): String {
        val weather = MenuLayer.Weather.valueOf(v[2])
        val max = v[4].toInt()
        val edit = v[6].toBoolean()
        val switchWeather = v[7].takeIf { it.isNotEmpty() }?.let(MenuLayer.Weather::valueOf)
        val events = Regex("\"([^\"]+)\"").findAll(v[8]).map { it.groupValues[1] }.toList()
        val samples = v[9].split(',').map(String::toFloat)
        val menu = MenuLayer()
        val view =
            menu.onCreate(MenuLayer.CreateData(weather, v[3].toInt(), max, "영천", edit, v[5].toInt(), switchWeather))
        val buttons = MenuLayer.Command.entries.joinToString(
            prefix = "[",
            postfix = "]"
        ) { c ->
            "{\"i\":${c.ordinal},\"active\":${c != MenuLayer.Command.BJ || edit},\"interactable\":${
                view.buttons.getValue(
                    c
                )
            }}"
        }
        val frames = samples.joinToString(",") { MenuLayer.weatherFrameAt(it).toString() }
        val progress = "%.6f".format(java.util.Locale.ROOT, view.progress).toFloat()
        val switchSheet = switchWeather?.let(MenuLayer::weatherSheet)?.toString() ?: "null"
        val initial =
            "{\"round\":${view.round},\"progress\":$progress,\"attached\":true,\"buttons\":$buttons,\"weatherSheet\":${
                MenuLayer.weatherSheet(weather)
            },\"switchWeatherSheet\":$switchSheet,\"frames\":[$frames]}"
        val lifecycle = mutableListOf<String>()
        var attached = true
        var loaded = 0
        val inputs = events.joinToString(prefix = "[", postfix = "]") { event ->
            if (event.startsWith("LOAD:")) {
                menu.switchWeatherLoadComplete(); loaded++; "{\"event\":\"$event\",\"attached\":$attached,\"events\":[${
                    lifecycle.joinToString(
                        ","
                    ) { "\"$it\"" }
                }],\"loaded\":$loaded}"
            } else if (event.startsWith("FADE:")) {
                if (event == "FADE:2") {
                    attached = false; lifecycle += "remove"; lifecycle += "callback"
                }; "{\"event\":\"$event\",\"attached\":$attached,\"events\":[${lifecycle.joinToString(",") { "\"$it\"" }}],\"loaded\":2}"
            } else if (event == "CANCEL") {
                menu.onCancel(MenuLayer.TOUCH_END); attached =
                    false; lifecycle += "remove"; "{\"event\":\"$event\",\"attached\":$attached,\"events\":[${
                    lifecycle.joinToString(
                        ","
                    ) { "\"$it\"" }
                }]}"
            } else {
                val (kind, index) = event.split(':')
                val selected = MenuLayer.Command.entries[index.toInt()]
                if (kind == "END" && (selected != MenuLayer.Command.BJ || edit)) menu.onCommand(
                    selected,
                    MenuLayer.TOUCH_END
                )?.let { command ->
                    attached = false; lifecycle += "remove"
                    val effect = when (command) {
                        MenuLayer.Command.JSYX -> "layer:MsgBox"; MenuLayer.Command.CD -> "layer:SaveLayer"
                        MenuLayer.Command.DD -> "layer:LoadGameLayer"; MenuLayer.Command.XTSZ -> "layer:SettingLayer"
                        MenuLayer.Command.WJYL -> "dispatch:SHOW_CHARACTER_LIST"; MenuLayer.Command.DJYL -> "layer:PropertyLayer"
                        MenuLayer.Command.DX -> "layer:TerrainLayer"; MenuLayer.Command.BW -> "layer:TreasureLayer"
                        MenuLayer.Command.HHJS -> "dispatch:END_ROUND"; MenuLayer.Command.SLTJ -> "dispatch:WIN_CONDITION"
                        MenuLayer.Command.XDT -> ""; MenuLayer.Command.JSWCZBD -> "dispatch:NOACTION_INDEX"
                        MenuLayer.Command.BJ -> if (edit) "layer:EditLayer2" else "command:BJ"
                        MenuLayer.Command.HELP -> "layer:HelperLayer"
                    }
                    if (effect.isNotEmpty()) lifecycle += effect
                }
                "{\"event\":\"$event\",\"attached\":$attached,\"events\":[${lifecycle.joinToString(",") { "\"$it\"" }}]}"
            }
        }
        return "{\"id\":\"${v[1]}\",\"initial\":$initial,\"inputs\":$inputs}"
    }
}
