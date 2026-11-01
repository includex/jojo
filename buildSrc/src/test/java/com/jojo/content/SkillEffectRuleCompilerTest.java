package com.jojo.content;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class SkillEffectRuleCompilerTest {
    @TempDir Path temp;

    @Test void remapsAnEffectWithoutChangingItsSemanticName() throws Exception {
        Path source = temp.resolve("source");
        Files.createDirectories(source.resolve("rules"));
        Path rule = source.resolve("rules/skill-effects.yaml");
        Path output = temp.resolve("out");
        Files.writeString(rule, "lifeSteal: 2\ncriticalFollowUp: 1\n");
        SkillEffectRuleCompiler.compile(source, output, 3);
        String original = Files.readString(output.resolve("data/skillEffects.json"));
        assertTrue(original.contains("\"lifeSteal\": 2"));
        Files.writeString(rule, "lifeSteal: 1\ncriticalFollowUp: 2\n");
        SkillEffectRuleCompiler.compile(source, output, 3);
        String remapped = Files.readString(output.resolve("data/skillEffects.json"));
        assertTrue(remapped.contains("\"lifeSteal\": 1"));
        assertNotEquals(original, remapped);
    }

    @Test void rejectsOutOfRangeAndDuplicateBindings() throws Exception {
        Path source = temp.resolve("source");
        Files.createDirectories(source.resolve("rules"));
        Path rule = source.resolve("rules/skill-effects.yaml");
        Files.writeString(rule, "lifeSteal: 3\n");
        assertThrows(IllegalArgumentException.class, () -> SkillEffectRuleCompiler.compile(source, temp.resolve("out"), 3));
        Files.writeString(rule, "lifeSteal: 1\nlifeSteal: 2\n");
        assertThrows(IllegalArgumentException.class, () -> SkillEffectRuleCompiler.compile(source, temp.resolve("out"), 3));
    }
}
