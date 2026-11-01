package com.jojo.content;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;

/** Emits named active-magic rule IDs for the runtime. */
public final class MagicEffectRuleCompiler {
    private MagicEffectRuleCompiler() { }

    public static void compile(Path source, Path output, int activeCount) throws IOException {
        Path file = source.resolve("rules/magic-effects.yaml");
        if (!Files.isRegularFile(file)) throw new IllegalArgumentException(file + ": missing magic effect bindings");
        LoaderOptions options = new LoaderOptions();
        options.setAllowDuplicateKeys(false);
        options.setMaxAliasesForCollections(0);
        Object parsed;
        try {
            parsed = new Yaml(new SafeConstructor(options)).load(Files.readString(file));
        } catch (RuntimeException e) {
            throw new IllegalArgumentException(file + ": invalid YAML: " + e.getMessage(), e);
        }
        if (!(parsed instanceof Map<?, ?> bindings) || bindings.isEmpty())
            throw new IllegalArgumentException(file + ": expected nonempty magic effect mapping");
        TreeMap<String, Integer> sorted = new TreeMap<>();
        for (Map.Entry<?, ?> entry : bindings.entrySet()) {
            if (!(entry.getKey() instanceof String key) || !key.matches("(?:type|category|spell)[A-Z][A-Za-z0-9]*"))
                throw new IllegalArgumentException(file + ": invalid magic effect key " + entry.getKey());
            if (!(entry.getValue() instanceof Integer id) || id < 0 || (key.startsWith("spell") && id >= activeCount))
                throw new IllegalArgumentException(file + ": invalid magic effect ID for " + key + ": " + entry.getValue());
            sorted.put(key, id);
        }
        StringBuilder json = new StringBuilder("{\n");
        for (Map.Entry<String, Integer> entry : sorted.entrySet()) {
            if (json.length() > 2) json.append(",\n");
            json.append("  \"").append(entry.getKey()).append("\": ").append(entry.getValue());
        }
        json.append("\n}\n");
        Path destination = output.resolve("data/magicEffects.json");
        Files.createDirectories(destination.getParent());
        Files.writeString(destination, json, StandardCharsets.UTF_8);
    }
}
