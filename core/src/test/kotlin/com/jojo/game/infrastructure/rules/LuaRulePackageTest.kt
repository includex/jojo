package com.jojo.game.infrastructure.rules

import com.jojo.game.domain.rules.RuleBindingScope
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.*

class LuaRulePackageTest {
    private fun withPackage(body: (Path) -> Unit) {
        val root = Files.createTempDirectory("jojo-lua-").toRealPath()
        try { body(root) } finally { root.toFile().deleteRecursively() }
    }
    private fun write(root: Path, yaml: String) {
        Files.writeString(root.resolve("lua-rules.yaml"), yaml)
        Files.writeString(root.resolve("rule.lua"), "return { evaluate = function(ctx, params) return {} end }")
    }
    private val valid = """
        version: 1
        scripts: {example.test: rule.lua}
        rules:
          - id: example.rule
            event: TURN_START
            handler: example.test
            parameters: {amount: 4}
            binding: {scope: equipment, id: 2}
    """.trimIndent()

    @Test fun loadsBindingsAndReloadsSourcesWithoutRebuild() = withPackage { root ->
        write(root, valid)
        val first = LuaRulePackage.fromDirectory(root.toFile())
        assertEquals("4", first.rules.single().parameters["amount"])
        assertEquals(RuleBindingScope.EQUIPMENT, first.rules.single().binding.scope)
        Files.writeString(root.resolve("rule.lua"), "invalid lua !")
        assertFails { LuaRulePackage.fromDirectory(root.toFile()) }
        assertEquals(1, first.module.handlers.size)
    }

    @Test fun emptyExternalPackageIsSupported() = withPackage { root ->
        write(root, "version: 1\nscripts: {}\nrules: []\n")
        assertTrue(LuaRulePackage.fromDirectory(root.toFile()).rules.isEmpty())
    }

    @Test fun rejectsUnsafeAndInvalidPackages() = withPackage { root ->
        val invalid = listOf(
            valid.replace("rule.lua", "../rule.lua"),
            valid.replace("rule.lua", "/tmp/rule.lua"),
            valid.replace("handler: example.test", "handler: unknown"),
            valid.replace("scope: equipment, id: 2", "scope: global, id: 2"),
            valid.replace("scope: equipment, id: 2", "scope: character"),
            valid.replace("version: 1", "version: 2"),
            valid.replace("version: 1", "version: 1\nversion: 1"),
            valid.replace("amount: 4", "amount: [4]"),
            valid.replace("version: 1", "version: &v 1").replace("amount: 4", "amount: *v"),
            valid + "\nunknown: true\n",
        )
        invalid.forEach { yaml ->
            write(root, yaml)
            assertFails(yaml) { LuaRulePackage.fromDirectory(root.toFile()) }
        }
    }

    @Test fun rejectsSymlinkScriptAndOversizedConfig() = withPackage { root ->
        write(root, valid)
        val original = root.resolve("original.lua")
        Files.move(root.resolve("rule.lua"), original)
        Files.createSymbolicLink(root.resolve("rule.lua"), original)
        assertFails { LuaRulePackage.fromDirectory(root.toFile()) }
        Files.writeString(root.resolve("lua-rules.yaml"), "#" + "x".repeat(256 * 1024))
        assertFails { LuaRulePackage.fromDirectory(root.toFile()) }
    }
}
