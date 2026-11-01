package com.jojo.content;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class MagicEffectRuleCompilerTest {
    @TempDir Path temp;

    @Test void remapsTypeAndConcreteSpellIndependently() throws Exception {
        Path source = temp.resolve("source");
        Files.createDirectories(source.resolve("rules"));
        Path rule = source.resolve("rules/magic-effects.yaml");
        Path output = temp.resolve("out");
        Files.writeString(rule, "typeTeleport: 37\nspellHeavyRain: 58\n");
        MagicEffectRuleCompiler.compile(source, output, 61);
        assertTrue(Files.readString(output.resolve("data/magicEffects.json")).contains("\"typeTeleport\": 37"));
        Files.writeString(rule, "typeTeleport: 38\nspellHeavyRain: 58\n");
        MagicEffectRuleCompiler.compile(source, output, 61);
        assertTrue(Files.readString(output.resolve("data/magicEffects.json")).contains("\"typeTeleport\": 38"));
    }

    @Test void rejectsOutOfRangeSpellId() throws Exception {
        Path source = temp.resolve("source");
        Files.createDirectories(source.resolve("rules"));
        Files.writeString(source.resolve("rules/magic-effects.yaml"), "typeTeleport: 37\nspellHeavyRain: 61\n");
        assertThrows(IllegalArgumentException.class, () -> MagicEffectRuleCompiler.compile(source, temp.resolve("out"), 61));
    }

    @Test void rejectsDuplicateKeys() throws Exception {
        Path source = temp.resolve("source");
        Files.createDirectories(source.resolve("rules"));
        Files.writeString(source.resolve("rules/magic-effects.yaml"), "typeTeleport: 37\ntypeTeleport: 38\n");
        assertThrows(IllegalArgumentException.class, () -> MagicEffectRuleCompiler.compile(source, temp.resolve("out"), 61));
    }
}
