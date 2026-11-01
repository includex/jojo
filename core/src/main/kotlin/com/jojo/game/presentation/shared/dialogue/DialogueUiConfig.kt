package com.jojo.game.presentation.shared.dialogue

import org.yaml.snakeyaml.LoaderOptions
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.SafeConstructor
import java.io.File

/** Optional values inherit the screen's recovered rendering defaults. Asset paths are internal paths. */
data class DialogueFontConfig(
    val path: String? = null, val size: Int? = null, val bold: Boolean? = null,
    val faceIndex: Int? = null, val color: String? = null, val borderWidth: Float? = null,
    val borderColor: String? = null, val scaleX: Float? = null, val scaleY: Float? = null,
)
data class DialogueNinePatchConfig(val enabled: Boolean = false, val left: Int = 0, val right: Int = 0, val top: Int = 0, val bottom: Int = 0)
data class DialogueBackgroundConfig(
    val leftImage: String? = null, val rightImage: String? = null,
    val tint: String? = null, val fillColor: String? = null,
    val ninePatch: DialogueNinePatchConfig = DialogueNinePatchConfig(),
)
data class DialogueUiProfile(
    val layout: Map<String, Double> = emptyMap(),
    val bodyFont: DialogueFontConfig = DialogueFontConfig(),
    val speakerFont: DialogueFontConfig = DialogueFontConfig(),
    val background: DialogueBackgroundConfig = DialogueBackgroundConfig(),
    val legacyRaster: Boolean = true,
    val followSpeaker: Boolean = true,
) {
    fun applyLayout(base: DialogueRenderLayout): DialogueRenderLayout = base.copy(
        width = layout["width"]?.toFloat() ?: base.width,
        height = layout["height"]?.toFloat() ?: base.height,
        panelLeftX = layout["panelLeftX"]?.toFloat() ?: base.panelLeftX,
        panelRightX = layout["panelRightX"]?.toFloat() ?: base.panelRightX,
        panelY = layout["panelY"]?.toFloat() ?: base.panelY,
        topOffsetY = layout["topOffsetY"]?.toFloat() ?: base.topOffsetY,
        panelWidth = layout["panelWidth"]?.toFloat() ?: base.panelWidth,
        panelHeight = layout["panelHeight"]?.toFloat() ?: base.panelHeight,
        portraitOffsetY = layout["portraitOffsetY"]?.toFloat() ?: base.portraitOffsetY,
        portraitWidth = layout["portraitWidth"]?.toFloat() ?: base.portraitWidth,
        portraitHeight = layout["portraitHeight"]?.toFloat() ?: base.portraitHeight,
        portraitLeftX = layout["portraitLeftX"]?.toFloat() ?: base.portraitLeftX,
        portraitRightX = layout["portraitRightX"]?.toFloat() ?: base.portraitRightX,
        speakerLeftX = layout["speakerLeftX"]?.toFloat() ?: base.speakerLeftX,
        speakerRightX = layout["speakerRightX"]?.toFloat() ?: base.speakerRightX,
        speakerOffsetY = layout["speakerOffsetY"]?.toFloat() ?: base.speakerOffsetY,
        speakerLabelLeftX = layout["speakerLabelLeftX"]?.toFloat() ?: base.speakerLabelLeftX,
        speakerLabelRightX = layout["speakerLabelRightX"]?.toFloat() ?: base.speakerLabelRightX,
        speakerLabelLeftOffsetY = layout["speakerLabelLeftOffsetY"]?.toFloat() ?: base.speakerLabelLeftOffsetY,
        speakerLabelRightOffsetY = layout["speakerLabelRightOffsetY"]?.toFloat() ?: base.speakerLabelRightOffsetY,
        speakerLabelScale = layout["speakerLabelScale"]?.toFloat() ?: base.speakerLabelScale,
        bodyLabelLeftSourceX = layout["bodyLabelLeftSourceX"]?.toDouble() ?: base.bodyLabelLeftSourceX,
        bodyLabelRightSourceX = layout["bodyLabelRightSourceX"]?.toDouble() ?: base.bodyLabelRightSourceX,
        bodyLabelSourceTopOffsetY = layout["bodyLabelSourceTopOffsetY"]?.toDouble() ?: base.bodyLabelSourceTopOffsetY,
        bodyLabelScale = layout["bodyLabelScale"]?.toDouble() ?: base.bodyLabelScale,
        textLeftX = layout["textLeftX"]?.toFloat() ?: base.textLeftX,
        textRightX = layout["textRightX"]?.toFloat() ?: base.textRightX,
        textOffsetY = layout["textOffsetY"]?.toFloat() ?: base.textOffsetY,
        textWidth = layout["textWidth"]?.toFloat() ?: base.textWidth,
        choicePanelX = layout["choicePanelX"]?.toFloat() ?: base.choicePanelX,
        choicePanelY = layout["choicePanelY"]?.toFloat() ?: base.choicePanelY,
        choicePanelWidth = layout["choicePanelWidth"]?.toFloat() ?: base.choicePanelWidth,
        choicePanelHeight = layout["choicePanelHeight"]?.toFloat() ?: base.choicePanelHeight,
        choicePortraitX = layout["choicePortraitX"]?.toFloat() ?: base.choicePortraitX,
        choicePortraitY = layout["choicePortraitY"]?.toFloat() ?: base.choicePortraitY,
        choiceRowX = layout["choiceRowX"]?.toFloat() ?: base.choiceRowX,
        choiceRowWidth = layout["choiceRowWidth"]?.toFloat() ?: base.choiceRowWidth,
        choiceRowHeight = layout["choiceRowHeight"]?.toFloat() ?: base.choiceRowHeight,
        choiceRowTopInset = layout["choiceRowTopInset"]?.toFloat() ?: base.choiceRowTopInset,
        choiceRowSpacing = layout["choiceRowSpacing"]?.toFloat() ?: base.choiceRowSpacing,
        choiceTextX = layout["choiceTextX"]?.toFloat() ?: base.choiceTextX,
        choiceTextOffsetY = layout["choiceTextOffsetY"]?.toFloat() ?: base.choiceTextOffsetY,
        choiceVisibleRows = layout["choiceVisibleRows"]?.toInt() ?: base.choiceVisibleRows
    )
}

/** Strict YAML contract shared by scenario and battle dialogue. */
data class DialogueUiConfig(val profiles: Map<String, DialogueUiProfile>) {
    fun profile(name: String): DialogueUiProfile = requireNotNull(profiles[name]) { "Unknown dialogue profile: $name" }
    fun validateAssets(exists: (String) -> Boolean) {
        profiles.forEach { (name, profile) ->
            listOfNotNull(profile.bodyFont.path, profile.speakerFont.path, profile.background.leftImage, profile.background.rightImage).forEach {
                require(exists(it)) { "Missing dialogue UI asset for $name: $it" }
            }
        }
    }
    companion object {
        const val PATH_PROPERTY = "jojo.ui.dialogue.path"
        fun load(): DialogueUiConfig {
            val external = System.getProperty(PATH_PROPERTY) ?: System.getenv("JOJO_UI_DIALOGUE_PATH")
            val source = if (external != null) {
                require(external.isNotBlank()) { "$PATH_PROPERTY must name a YAML file" }
                File(external).inputStream().use { String(it.readNBytes(262145), Charsets.UTF_8) }
            } else {
                requireNotNull(DialogueUiConfig::class.java.classLoader.getResourceAsStream("ui/dialogue.yaml")) { "Missing ui/dialogue.yaml" }
                    .use { String(it.readNBytes(262145), Charsets.UTF_8) }
            }
            return parse(source)
        }
        fun parse(source: String): DialogueUiConfig {
            require(source.toByteArray(Charsets.UTF_8).size <= 262144) { "Dialogue YAML exceeds size limit" }
            val yaml = Yaml(SafeConstructor(LoaderOptions().apply {
                isAllowDuplicateKeys = false; maxAliasesForCollections = 0; nestingDepthLimit = 12; codePointLimit = 262144
            }))
            require(yaml.parse(source.reader()).none { it is org.yaml.snakeyaml.events.AliasEvent }) { "YAML aliases are forbidden" }
            val root = mapping(yaml.load<Any?>(source), "dialogue", setOf("version", "profiles"))
            require(root["version"] == 1) { "Unsupported dialogue UI version" }
            val profiles = mapping(root["profiles"], "profiles", setOf("scenario", "battle"))
            require(profiles.keys == setOf("scenario", "battle")) { "Both scenario and battle profiles are required" }
            return DialogueUiConfig(profiles.mapValues { (name, value) ->
                val p = mapping(value, name, setOf("layout", "bodyFont", "speakerFont", "background", "legacyRaster", "followSpeaker"))
                val layout = p["layout"]?.let { mapping(it, "$name.layout", layoutKeys) } ?: emptyMap()
                val numbers = layout.mapValues { (key, value) ->
                    number(value, key).also {
                        if (key in positiveLayoutKeys) require(it > 0) { "$key must be positive" }
                        if (key == "choiceVisibleRows") require(it % 1.0 == 0.0 && it <= 100) { "Invalid choiceVisibleRows" }
                    }
                }
                val body = p["bodyFont"]?.let { font(it, "$name.bodyFont") } ?: DialogueFontConfig()
                val speaker = p["speakerFont"]?.let { font(it, "$name.speakerFont") } ?: DialogueFontConfig()
                DialogueUiProfile(numbers, body, speaker, p["background"]?.let(::background) ?: DialogueBackgroundConfig(),
                    (p["legacyRaster"]?.let { boolean(it, "legacyRaster") } ?: true) && numbers.isEmpty() && body == DialogueFontConfig() && speaker == DialogueFontConfig(),
                    p["followSpeaker"]?.let { boolean(it, "followSpeaker") } ?: true)
            })
        }
        private val layoutKeys = setOf("panelLeftX", "panelRightX", "panelY", "topOffsetY", "panelWidth", "panelHeight", "portraitOffsetY", "portraitWidth", "portraitHeight", "portraitLeftX", "portraitRightX", "speakerLeftX", "speakerRightX", "speakerOffsetY", "textLeftX", "textRightX", "textOffsetY", "textWidth", "choicePanelX", "choicePanelY", "choicePanelWidth", "choicePanelHeight", "choicePortraitX", "choicePortraitY", "choiceRowX", "choiceRowWidth", "choiceRowHeight", "choiceRowTopInset", "choiceRowSpacing", "choiceTextX", "choiceTextOffsetY", "choiceVisibleRows")
        private val positiveLayoutKeys = layoutKeys.filter { it.endsWith("Width") || it.endsWith("Height") || it.endsWith("Scale") || it == "width" || it == "height" || it == "choiceVisibleRows" || it == "choiceRowSpacing" }.toSet()
        private fun mapping(value: Any?, name: String, allowed: Set<String>): Map<String, Any?> {
            require(value is Map<*, *>) { "$name must be a mapping" }
            require(value.keys.all { it is String && it in allowed }) { "Unknown $name keys: ${value.keys.filter { it !in allowed }}" }
            require(value.values.none { it == null }) { "$name does not accept null values; omit inherited keys" }
            return value.entries.associate { it.key as String to it.value }
        }
        private fun number(value: Any?, name: String): Double {
            require(value is Number) { "$name must be a number" }
            return value.toDouble().also { require(it.isFinite() && kotlin.math.abs(it) <= 1000000) { "Invalid $name" } }
        }
        private fun integer(value: Any?, name: String, minimum: Int): Int = number(value, name).let {
            require(it % 1.0 == 0.0 && it >= minimum) { "Invalid $name" }; it.toInt()
        }
        private fun boolean(value: Any?, name: String): Boolean = (value as? Boolean) ?: error("$name must be boolean")
        private fun color(value: Any?): String {
            require(value is String && Regex("#[0-9a-fA-F]{6}([0-9a-fA-F]{2})?").matches(value)) { "Color must be #RRGGBB or #RRGGBBAA" }; return value
        }
        private fun path(value: Any?): String {
            require(value is String && value.isNotBlank() && !value.startsWith("/") && !value.contains('\\') && value.split('/').none { it == ".." || it == "." || it.isEmpty() }) { "Asset path must be relative and internal" }; return value
        }
        private fun font(value: Any?, name: String): DialogueFontConfig {
            val f = mapping(value, name, setOf("path", "size", "bold", "faceIndex", "color", "borderWidth", "borderColor", "scaleX", "scaleY"))
            fun scale(key: String): Float? = f[key]?.let { number(it, key).also { n -> require(n > 0 && n <= 100) { "Invalid $key" } }.toFloat() }
            return DialogueFontConfig(f["path"]?.let(::path), f["size"]?.let { integer(it, "size", 1).also { n -> require(n <= 512) } },
                f["bold"]?.let { boolean(it, "bold") }, f["faceIndex"]?.let { integer(it, "faceIndex", 0) },
                f["color"]?.let(::color), f["borderWidth"]?.let { number(it, "borderWidth").also { n -> require(n >= 0 && n <= 64) }.toFloat() },
                f["borderColor"]?.let(::color), scale("scaleX"), scale("scaleY"))
        }
        private fun background(value: Any?): DialogueBackgroundConfig {
            val b = mapping(value, "background", setOf("leftImage", "rightImage", "tint", "fillColor", "ninePatch"))
            val patch = b["ninePatch"]?.let {
                val n = mapping(it, "ninePatch", setOf("enabled", "left", "right", "top", "bottom"))
                DialogueNinePatchConfig(n["enabled"]?.let { v -> boolean(v, "enabled") } ?: false,
                    n["left"]?.let { v -> integer(v, "left", 0) } ?: 0, n["right"]?.let { v -> integer(v, "right", 0) } ?: 0,
                    n["top"]?.let { v -> integer(v, "top", 0) } ?: 0, n["bottom"]?.let { v -> integer(v, "bottom", 0) } ?: 0)
            } ?: DialogueNinePatchConfig()
            return DialogueBackgroundConfig(b["leftImage"]?.let(::path), b["rightImage"]?.let(::path), b["tint"]?.let(::color), b["fillColor"]?.let(::color), patch)
        }
    }
}
