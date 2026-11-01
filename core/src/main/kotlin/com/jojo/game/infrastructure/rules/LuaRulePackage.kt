package com.jojo.game.infrastructure.rules

import com.jojo.game.domain.rules.*
import org.yaml.snakeyaml.LoaderOptions
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.SafeConstructor
import java.io.InputStream
import java.io.File
import java.io.ByteArrayOutputStream

/** A complete, freshly loaded script registry. Loading never uses libGDX or network access. */
data class LuaRulePackage(val rules: List<RuleDefinition>, val module: RuleModule) {
    companion object {
        const val DIRECTORY_PROPERTY = "jojo.rules.dir"
        private const val MAX_CONFIG_BYTES = 256 * 1024
        private const val MAX_HANDLERS = 128
        private const val MAX_RULES = 128

        fun load(): LuaRulePackage {
            val external = System.getProperty(DIRECTORY_PROPERTY) ?: System.getenv("JOJO_RULES_DIR")
            return if (external != null) {
                require(external.isNotBlank()) { "$DIRECTORY_PROPERTY must name a directory" }
                fromDirectory(File(external))
            } else {
                val loader = LuaRulePackage::class.java.classLoader
                parse { name, limit ->
                    val stream = requireNotNull(loader.getResourceAsStream("rules/$name")) { "Missing packaged Lua resource: $name" }
                    stream.use { boundedText(it, limit) }
                }
            }
        }

        fun fromDirectory(directory: File): LuaRulePackage {
            require(directory.isDirectory) { "Lua rules directory missing: $directory" }
            // Canonicalize the explicitly selected root so OS aliases such as /var work.
            val root = directory.canonicalFile
            return parse { name, limit ->
                safePath(name)
                val file = File(root, name).absoluteFile
                require(file.path.startsWith(root.path.trimEnd(File.separatorChar) + File.separator)) {
                    "Lua path escapes package: $name"
                }
                rejectSymlinks(root, file)
                require(file.isFile) { "Lua resource missing: $name" }
                file.inputStream().use { boundedText(it, limit) }
            }
        }

        private fun parse(read: (String, Int) -> String): LuaRulePackage {
            val options = LoaderOptions().apply {
                isAllowDuplicateKeys = false
                maxAliasesForCollections = 0
                codePointLimit = MAX_CONFIG_BYTES
                nestingDepthLimit = 20
            }
            val yaml = Yaml(SafeConstructor(options))
            val source = read("lua-rules.yaml", MAX_CONFIG_BYTES)
            require(yaml.parse(source.reader()).none { it is org.yaml.snakeyaml.events.AliasEvent }) { "YAML aliases are forbidden" }
            val config = mapping(yaml.load<Any?>(source), "package")
            keys(config, setOf("version", "scripts", "rules"), "package")
            require(config["version"] == 1) { "Unsupported Lua package version" }
            val scripts = mapping(config["scripts"], "scripts")
            require(scripts.size <= MAX_HANDLERS) { "Lua handler budget exceeded" }
            val handlers = scripts.map { (id, value) ->
                identifier(id)
                val path = value as? String ?: error("Script path must be a string: $id")
                safePath(path)
                require(path.endsWith(".lua")) { "Script must be a .lua file: $path" }
                LuaRuleHandler(id, read(path, LuaRuleHandler.MAX_SOURCE_BYTES), path)
            }
            val rows = config["rules"] as? List<*> ?: error("rules must be a list")
            require(rows.size <= MAX_RULES) { "Lua rule budget exceeded" }
            val rules = rows.map { row ->
                val rule = mapping(row, "rule")
                keys(rule, setOf("id", "event", "handler", "priority", "parameters", "binding"), "rule")
                val id = string(rule["id"], "rule id").also(::identifier)
                val handler = string(rule["handler"], "handler")
                require(handler in scripts) { "Unknown Lua handler: $handler" }
                val event = RuleEvent.valueOf(string(rule["event"], "event"))
                val params = rule["parameters"]?.let { mapping(it, "parameters") }?.mapValues { (key, value) ->
                    require(key.isNotBlank() && key.length <= 256) { "Invalid parameter key" }
                    require(value is String || value is Number || value is Boolean) { "Parameter must be scalar: $key" }
                    value.toString().also { require(it.length <= 4096) { "Parameter too long" } }
                } ?: emptyMap()
                val binding = rule["binding"]?.let {
                    val b = mapping(it, "binding")
                    keys(b, setOf("scope", "id"), "binding")
                    val scope = when (b["scope"]) {
                        "global" -> RuleBindingScope.GLOBAL
                        "character" -> RuleBindingScope.CHARACTER
                        "equipment" -> RuleBindingScope.EQUIPMENT
                        else -> error("Unknown binding scope")
                    }
                    val targetId = b["id"]?.let { number -> integer(number, "binding id").also { require(it >= 0) } }
                    require((scope == RuleBindingScope.GLOBAL) == (targetId == null)) { "Global bindings omit id; character/equipment bindings require id" }
                    RuleBinding(scope, targetId)
                } ?: RuleBinding()
                RuleDefinition(id, event, handler, rule["priority"]?.let { integer(it, "priority") } ?: 0, params, binding = binding)
            }
            require(rules.map { it.id }.distinct().size == rules.size) { "Duplicate Lua rule id" }
            val module = object : RuleModule { override val handlers = handlers }
            return LuaRulePackage(rules, module)
        }

        private fun mapping(value: Any?, name: String): Map<String, Any?> {
            require(value is Map<*, *> && value.keys.all { it is String }) { "$name must be a string-keyed mapping" }
            return value.entries.associate { it.key as String to it.value }
        }
        private fun keys(map: Map<String, Any?>, allowed: Set<String>, name: String) =
            require(map.keys.all { it in allowed }) { "Unknown $name key" }
        private fun string(value: Any?, name: String): String = value as? String ?: error("$name must be a string")
        private fun integer(value: Any?, name: String): Int = (value as? Int) ?: error("$name must be a 32-bit integer")
        private fun identifier(id: String) = require(id.matches(Regex("[A-Za-z][A-Za-z0-9_.-]{0,127}"))) { "Invalid Lua identifier: $id" }
        private fun safePath(name: String) {
            require(name.isNotBlank() && !name.contains('\\') && !name.contains(':') && !name.contains('\u0000')) {
                "Invalid Lua resource path"
            }
            require(!File(name).isAbsolute && name.split('/').none { it == ".." || it == "." || it.isEmpty() }) {
                "Invalid Lua resource path: $name"
            }
        }
        private fun rejectSymlinks(root: File, file: File) {
            var current = file
            while (current != root) {
                require(current.canonicalFile == current.absoluteFile) {
                    "Symlinks are forbidden in Lua package paths: $current"
                }
                current = requireNotNull(current.parentFile) { "Lua path escapes package" }
            }
        }
        private fun boundedText(stream: InputStream, limit: Int): String {
            val output = ByteArrayOutputStream()
            val buffer = ByteArray(8192)
            while (true) {
                val count = stream.read(buffer, 0, minOf(buffer.size, limit + 1 - output.size()))
                if (count < 0) break
                if (count == 0) {
                    val next = stream.read()
                    if (next < 0) break
                    output.write(next)
                } else output.write(buffer, 0, count)
                require(output.size() <= limit) { "Lua resource byte budget exceeded" }
            }
            val bytes = output.toByteArray()
            require(bytes.size <= limit) { "Lua resource byte budget exceeded" }
            return Charsets.UTF_8.newDecoder().decode(java.nio.ByteBuffer.wrap(bytes)).toString()
        }
    }
}
