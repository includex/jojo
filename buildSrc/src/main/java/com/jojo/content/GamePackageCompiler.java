package com.jojo.content;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/** Validates the package declaration and turns it into a runtime JSON resource. */
public final class GamePackageCompiler {
    private GamePackageCompiler() { }

    public static void compile(Path yamlFile, Path outputJson) {
        try {
            LoaderOptions options = new LoaderOptions();
            options.setAllowDuplicateKeys(false);
            options.setMaxAliasesForCollections(0);
            Object parsed = new Yaml(new SafeConstructor(options)).load(Files.readString(yamlFile));
            Map<String, Object> input = mapping(parsed, yamlFile);
            List<String> fields = List.of("id", "title", "startModule", "startScene", "resumeScene",
                    "protagonistId", "saveNamespace", "defaultBattleModule", "battleModules");
            for (String field : input.keySet()) {
                if (!fields.contains(field)) throw invalid(yamlFile, "unknown field " + field);
            }
            LinkedHashMap<String, Object> result = new LinkedHashMap<>();
            for (String field : fields) {
                if (field.equals("battleModules")) continue;
                Object value = input.get(field);
                if (field.equals("protagonistId")) {
                    if (!(value instanceof Integer number) || number < 0)
                        throw invalid(yamlFile, field + " must be a nonnegative integer");
                } else if (!(value instanceof String text) || text.isBlank()) {
                    throw invalid(yamlFile, field + " must be a nonempty string");
                }
                result.put(field, value);
            }
            String namespace = (String) result.get("saveNamespace");
            if (!namespace.matches("[A-Za-z0-9][A-Za-z0-9._-]*"))
                throw invalid(yamlFile, "saveNamespace must use letters, digits, dot, underscore or hyphen");
            Map<String, Object> manifest = mapping(
                    new Yaml(new SafeConstructor(options)).load(Files.readString(yamlFile.getParent().resolve("scenarios/manifest.json"))),
                    yamlFile.getParent().resolve("scenarios/manifest.json"));
            String start = (String) result.get("startModule");
            String battle = (String) result.get("defaultBattleModule");
            if (!manifest.containsKey(start)) throw invalid(yamlFile, "startModule missing from scenario manifest: " + start);
            if (!manifest.containsKey(battle)) throw invalid(yamlFile, "defaultBattleModule missing from scenario manifest: " + battle);
            int protagonist = (Integer) result.get("protagonistId");
            if (!Files.isRegularFile(yamlFile.getParent().resolve("characters/%04d.yaml".formatted(protagonist))))
                throw invalid(yamlFile, "protagonistId missing from characters: " + protagonist);
            Map<String, Object> startEntry = mapping(manifest.get(start), yamlFile);
            List<?> functions = (List<?>) startEntry.get("functions");
            for (String field : List.of("startScene", "resumeScene")) {
                if (functions == null || !functions.contains(result.get(field)))
                    throw invalid(yamlFile, field + " missing from " + start + ": " + result.get(field));
            }
            Map<String, Object> authoredRoutes = mapping(input.get("battleModules"), yamlFile);
            if (authoredRoutes.isEmpty()) throw invalid(yamlFile, "battleModules must not be empty");
            TreeMap<String, String> battleModules = new TreeMap<>();
            for (Map.Entry<String, Object> route : authoredRoutes.entrySet()) {
                String module = route.getKey();
                Object target = route.getValue();
                if (!manifest.containsKey(module)) throw invalid(yamlFile, "battleModules source missing from manifest: " + module);
                if (!(target instanceof String destination) || !manifest.containsKey(destination))
                    throw invalid(yamlFile, "battleModules target missing from manifest: " + target);
                if (battleModules.put(module, destination) != null)
                    throw invalid(yamlFile, "duplicate battleModules source: " + module);
            }
            if (battleModules.values().stream().distinct().count() != battleModules.size())
                throw invalid(yamlFile, "battleModules targets must be unique for return routing");
            if (!battleModules.containsKey(start))
                throw invalid(yamlFile, "battleModules must include startModule " + start);
            if (!battleModules.containsValue(battle))
                throw invalid(yamlFile, "battleModules must include defaultBattleModule " + battle);
            result.put("battleModules", battleModules);
            Files.createDirectories(outputJson.getParent());
            Files.writeString(outputJson, GameContentCompiler.json(result) + "\n", StandardCharsets.UTF_8);
        } catch (IOException | RuntimeException exception) {
            if (exception instanceof IllegalArgumentException invalid) throw invalid;
            throw new IllegalArgumentException("Cannot compile " + yamlFile + ": " + exception.getMessage(), exception);
        }
    }

    private static Map<String, Object> mapping(Object value, Path source) {
        if (!(value instanceof Map<?, ?> raw)) throw invalid(source, "expected a mapping");
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        for (Map.Entry<?, ?> entry : raw.entrySet()) {
            if (!(entry.getKey() instanceof String key)) throw invalid(source, "mapping keys must be strings");
            result.put(key, entry.getValue());
        }
        return result;
    }

    private static IllegalArgumentException invalid(Path file, String reason) {
        return new IllegalArgumentException(file + ": " + reason);
    }
}
