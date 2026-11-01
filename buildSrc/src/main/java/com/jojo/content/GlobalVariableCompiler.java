package com.jojo.content;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/** Compiles the authored, typed global variable registry into its runtime resource. */
public final class GlobalVariableCompiler {
    private GlobalVariableCompiler() { }

    public static void compile(Path source, Path output) {
        try {
            LoaderOptions options = new LoaderOptions();
            options.setAllowDuplicateKeys(false);
            options.setMaxAliasesForCollections(0);
            Map<String, Object> root = mapping(new Yaml(new SafeConstructor(options)).load(Files.readString(source)), source);
            fields(root, Set.of("version", "variables"), source);
            if (!(root.get("version") instanceof Integer version) || version != 1)
                throw invalid(source, "version must be 1");
            Map<String, Object> variables = mapping(root.get("variables"), source);
            TreeMap<String, Object> compiled = new TreeMap<>();
            Set<Integer> aliases = new HashSet<>();
            for (var entry : variables.entrySet()) {
                String name = entry.getKey();
                if (!name.matches("[a-z][a-z0-9_]*(\\.[a-z][a-z0-9_]*)+"))
                    throw invalid(source, "invalid variable name " + name);
                Map<String, Object> definition = mapping(entry.getValue(), source);
                fields(definition, Set.of("type", "default", "description", "legacyId"), source);
                Object type = definition.get("type");
                if (!"int".equals(type) && !"bool".equals(type))
                    throw invalid(source, name + ": type must be int or bool");
                Object value = definition.get("default");
                if ("int".equals(type) ? !(value instanceof Integer) : !(value instanceof Boolean))
                    throw invalid(source, name + ": default must match type " + type);
                if (!(definition.get("description") instanceof String description) || description.isBlank())
                    throw invalid(source, name + ": description must be a nonempty string");
                LinkedHashMap<String, Object> record = new LinkedHashMap<>();
                record.put("type", type);
                record.put("default", value);
                record.put("description", description);
                if (definition.containsKey("legacyId")) {
                    if (!(definition.get("legacyId") instanceof Integer id) || id < 0)
                        throw invalid(source, name + ": legacyId must be a nonnegative integer");
                    if (!aliases.add(id)) throw invalid(source, "duplicate legacyId " + id);
                    record.put("legacyId", id);
                }
                compiled.put(name, record);
            }
            LinkedHashMap<String, Object> result = new LinkedHashMap<>();
            result.put("version", 1);
            result.put("variables", compiled);
            Files.createDirectories(output.toAbsolutePath().getParent());
            Files.writeString(output, GameContentCompiler.json(result) + "\n", StandardCharsets.UTF_8);
        } catch (IOException | RuntimeException exception) {
            throw invalid(source, exception.getMessage());
        }
    }

    private static Map<String, Object> mapping(Object value, Path source) {
        if (!(value instanceof Map<?, ?> map)) throw invalid(source, "expected mapping");
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        for (var entry : map.entrySet()) {
            if (!(entry.getKey() instanceof String key)) throw invalid(source, "mapping keys must be strings");
            result.put(key, entry.getValue());
        }
        return result;
    }

    private static void fields(Map<String, Object> map, Set<String> allowed, Path source) {
        for (String key : map.keySet()) if (!allowed.contains(key)) throw invalid(source, "unknown field " + key);
    }

    private static IllegalArgumentException invalid(Path source, String message) {
        return new IllegalArgumentException(source + ": " + message);
    }
}
