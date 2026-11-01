package com.jojo.content;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

/** Strict, deterministic translator from semantic YAML rows to legacy encrypted JSON tables. */
public final class GameContentCompiler {
    private static final String KEY = "ccz65Sha08GeZ1Fu";
    private static final Map<String, String> TABLES = Map.of(
            "characters", "unit.bin", "equipment", "item.bin", "skills/active", "magic.bin",
            "skills/passive", "defineSkill.bin", "skills/assignments", "unitPostsSkill.bin",
            "skills/equipment", "itemSkills.bin");

    private GameContentCompiler() { }

    public static void compile(Path source, Path output) {
        try {
            validateSourceTree(source);
            Map<String, Object> schema = mapping(read(source.resolve("schema.json")), source.resolve("schema.json"));
            Map<String, List<Object>> rows = new LinkedHashMap<>();
            Map<String, List<Path>> rowFiles = new LinkedHashMap<>();
            Map<String, List<Integer>> equipmentEffects = new TreeMap<>(Comparator.comparingInt(Integer::parseInt));
            Map<String, Path> equipmentEffectFiles = new LinkedHashMap<>();
            for (String category : List.of("characters", "equipment", "skills/active", "skills/passive",
                    "skills/assignments", "skills/equipment")) {
                Object spec = schema.get(category);
                if (spec == null) throw problem(source.resolve("schema.json"), "missing category " + category);
                Map<String, Object> categorySpec = mapping(spec, source.resolve("schema.json"));
                String table = String.valueOf(categorySpec.getOrDefault("table", TABLES.get(category)));
                if (!TABLES.get(category).equals(table)) throw problem(source.resolve("schema.json"), "incorrect table for " + category);
                Map<String, Object> fields = mapping(categorySpec.get("fields"), source.resolve("schema.json"));
                TreeMap<Integer, Object> sorted = new TreeMap<>();
                TreeMap<Integer, Path> sortedFiles = new TreeMap<>();
                Path directory = source.resolve(category);
                if (!Files.isDirectory(directory)) throw problem(directory, "missing category directory");
                List<Path> files;
                try (Stream<Path> paths = Files.walk(directory)) {
                    files = paths.filter(Files::isRegularFile).filter(p -> p.toString().endsWith(".yaml") || p.toString().endsWith(".yml"))
                            .sorted().toList();
                }
                if (files.isEmpty()) throw problem(directory, "no YAML records");
                for (Path file : files) {
                    Map<String, Object> row = mapping(read(file), file);
                    int id = checkedId(row.remove("id"), file);
                    if (sorted.containsKey(id)) throw problem(file, "duplicate id " + id + " in " + category);
                    LinkedHashMap<String, Object> legacy = new LinkedHashMap<>();
                    for (Map.Entry<String, Object> field : row.entrySet()) {
                        String name = field.getKey();
                        if (category.equals("equipment") && name.equals("skillEffects")) {
                            equipmentEffects.put(String.valueOf(id), intList(field.getValue(), file, name));
                            equipmentEffectFiles.put(String.valueOf(id), file);
                            continue;
                        }
                        if (!fields.containsKey(name)) throw problem(file, "unknown field " + name);
                        Field rule = fieldRule(fields.get(name), file);
                        Object value = validate(field.getValue(), rule.type, file, name);
                        if (legacy.put(rule.key, value) != null) throw problem(file, "duplicate legacy key " + rule.key);
                    }
                    for (Map.Entry<String, Object> field : fields.entrySet()) {
                        Field rule = fieldRule(field.getValue(), source.resolve("schema.json"));
                        if (rule.required && !row.containsKey(field.getKey())) throw problem(file, "missing required field " + field.getKey());
                    }
                    sorted.put(id, legacy);
                    sortedFiles.put(id, file);
                }
                ArrayList<Object> ordered = new ArrayList<>(sorted.size());
                ArrayList<Path> orderedFiles = new ArrayList<>(sorted.size());
                for (int id = 0; id < sorted.size(); id++) {
                    if (!sorted.containsKey(id)) throw problem(directory, "missing id " + id + " (indexed table cannot be renumbered)");
                    ordered.add(sorted.get(id));
                    orderedFiles.add(sortedFiles.get(id));
                }
                rows.put(category, ordered);
                rowFiles.put(category, orderedFiles);
            }
            int passiveCount = rows.get("skills/passive").size();
            validateSkillRefs(rows.get("skills/equipment"), rowFiles.get("skills/equipment"), "skillId", passiveCount);
            validateSkillRefs(rows.get("skills/equipment"), rowFiles.get("skills/equipment"), "upgrade", passiveCount);
            validateSkillRefs(rows.get("skills/assignments"), rowFiles.get("skills/assignments"), "3", passiveCount);
            int effectCount = rows.get("skills/equipment").size();
            for (Map.Entry<String, List<Integer>> entry : equipmentEffects.entrySet()) {
                for (int index : entry.getValue()) {
                    if (index >= effectCount) throw problem(equipmentEffectFiles.get(entry.getKey()),
                            "skillEffects index " + index + " for equipment " + entry.getKey() + " exceeds " + (effectCount - 1));
                }
            }
            Files.createDirectories(output.resolve("data"));
            for (Map.Entry<String, List<Object>> entry : rows.entrySet()) {
                Object table = entry.getValue();
                if (entry.getKey().equals("skills/equipment")) {
                    Map<String, Object> itemSkills = new LinkedHashMap<>();
                    itemSkills.put("define", table);
                    itemSkills.put("index", equipmentEffects);
                    table = itemSkills;
                }
                Files.write(output.resolve("data").resolve(TABLES.get(entry.getKey())), encode(json(table)));
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Cannot compile game content: " + e.getMessage(), e);
        }
    }

    private static void validateSourceTree(Path source) throws IOException {
        try (Stream<Path> paths = Files.walk(source)) {
            for (Path file : paths.filter(Files::isRegularFile).toList()) {
                String relative = source.relativize(file).toString().replace('\\', '/');
                // Scenario sources belong to the separate Python AST resource pipeline.
                if (relative.startsWith("scenarios/")) continue;
                if (relative.equals("schema.json") || relative.equals("README.md")) continue;
                if (!relative.endsWith(".yaml") && !relative.endsWith(".yml")) throw problem(file, "unexpected content file");
                boolean known = TABLES.keySet().stream().anyMatch(category -> relative.startsWith(category + "/"));
                if (!known) throw problem(file, "YAML file outside a declared category");
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static void validateSkillRefs(List<Object> rows, List<Path> files, String key, int count) {
        for (int i = 0; i < rows.size(); i++) {
            Object value = ((Map<String, Object>) rows.get(i)).get(key);
            if (value == null) continue;
            if (!(value instanceof Integer id) || id < 0 || id >= count)
                throw problem(files.get(i), "row " + i + " references invalid passive skill " + value + " in " + key);
        }
    }

    private static Object read(Path file) throws IOException {
        LoaderOptions options = new LoaderOptions();
        options.setAllowDuplicateKeys(false);
        options.setMaxAliasesForCollections(0);
        options.setCodePointLimit(10_000_000);
        try {
            return new Yaml(new SafeConstructor(options)).load(Files.readString(file));
        } catch (RuntimeException e) {
            throw problem(file, "invalid YAML: " + e.getMessage());
        }
    }

    private static Map<String, Object> mapping(Object value, Path file) {
        if (!(value instanceof Map<?, ?> raw)) throw problem(file, "expected a mapping");
        Map<String, Object> result = new LinkedHashMap<>();
        for (Map.Entry<?, ?> entry : raw.entrySet()) {
            if (!(entry.getKey() instanceof String key)) throw problem(file, "mapping keys must be strings");
            result.put(key, entry.getValue());
        }
        return result;
    }

    private static int checkedId(Object value, Path file) {
        if (!(value instanceof Integer id) || id < 0) throw problem(file, "id must be a nonnegative integer");
        return id;
    }

    private record Field(String key, String type, boolean required) { }

    private static Field fieldRule(Object value, Path file) {
        if (value instanceof String key) return new Field(key, "any", false);
        Map<String, Object> spec = mapping(value, file);
        Object key = spec.getOrDefault("key", spec.get("legacy"));
        if (!(key instanceof String name)) throw problem(file, "field rule needs a string key");
        String type = String.valueOf(spec.getOrDefault("type", "any"));
        Object required = spec.getOrDefault("required", false);
        if (!(required instanceof Boolean flag)) throw problem(file, "required must be boolean");
        return new Field(name, type, flag);
    }

    private static Object validate(Object value, String type, Path file, String field) {
        boolean valid = switch (type) {
            case "any" -> value != null;
            case "string", "text" -> value instanceof String;
            case "int", "integer" -> value instanceof Integer;
            case "number" -> value instanceof Number;
            case "boolean", "bool" -> value instanceof Boolean;
            case "string-list" -> value instanceof List<?> list && list.stream().allMatch(v -> v instanceof String);
            case "object", "map" -> value instanceof Map<?, ?>;
            case "intMap", "integerMap", "integer-map" -> value instanceof Map<?, ?> map && map.entrySet().stream().allMatch(e ->
                    String.valueOf(e.getKey()).matches("[0-9]+") && e.getValue() instanceof Integer);
            case "intList", "integerList", "integer-list" -> value instanceof List<?> list && list.stream().allMatch(v -> v instanceof Integer && (Integer) v >= 0);
            default -> throw problem(file, "unsupported schema type " + type);
        };
        if (!valid) throw problem(file, "field " + field + " must be " + type);
        if (value instanceof Map<?, ?> raw) {
            Map<String, Object> copy = mapping(raw, file);
            for (Object nested : copy.values()) validateJsonValue(nested, file);
            return copy;
        }
        validateJsonValue(value, file);
        return value;
    }

    private static List<Integer> intList(Object value, Path file, String field) {
        if (!(value instanceof List<?> list)) throw problem(file, "field " + field + " must be an integer list");
        List<Integer> result = new ArrayList<>();
        for (Object element : list) {
            if (!(element instanceof Integer id) || id < 0) throw problem(file, "field " + field + " must be a nonnegative integer list");
            result.add(id);
        }
        return result;
    }

    private static void validateJsonValue(Object value, Path file) {
        if (value instanceof String || value instanceof Boolean || value instanceof Integer || value instanceof Long) return;
        if (value instanceof Number number && Double.isFinite(number.doubleValue())) return;
        if (value instanceof List<?> list) { list.forEach(v -> validateJsonValue(v, file)); return; }
        if (value instanceof Map<?, ?> map) {
            for (Map.Entry<?, ?> e : map.entrySet()) {
                if (!(e.getKey() instanceof String) && !(e.getKey() instanceof Integer)) throw problem(file, "object key must be string or integer");
                validateJsonValue(e.getValue(), file);
            }
            return;
        }
        throw problem(file, "unsupported JSON value " + (value == null ? "null" : value.getClass().getSimpleName()));
    }

    private static IllegalArgumentException problem(Path file, String message) {
        return new IllegalArgumentException(file + ": " + message);
    }

    public static byte[] encode(String json) {
        byte[] input = (md5(KEY + json) + json).getBytes(StandardCharsets.UTF_8);
        byte[] output = new byte[input.length];
        for (int i = 0; i < input.length; i++) {
            int shift = KEY.charAt(i % KEY.length()) % 8;
            int value = input[i] & 255;
            output[i] = (byte) (((value << shift) & 255) | (value >>> (8 - shift)));
        }
        return output;
    }

    private static String md5(String value) {
        try {
            byte[] hash = MessageDigest.getInstance("MD5").digest(value.getBytes(StandardCharsets.UTF_8));
            StringBuilder output = new StringBuilder(32);
            for (byte b : hash) output.append(String.format("%02x", b & 255));
            return output.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    private static String jsonString(String input) {
        StringBuilder escaped = new StringBuilder(input.length() + 2).append('"');
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            switch (c) {
                case '"' -> escaped.append("\\\"");
                case '\\' -> escaped.append("\\\\");
                case '\b' -> escaped.append("\\b");
                case '\f' -> escaped.append("\\f");
                case '\n' -> escaped.append("\\n");
                case '\r' -> escaped.append("\\r");
                case '\t' -> escaped.append("\\t");
                default -> {
                    if (c < 0x20) escaped.append(String.format("\\u%04x", (int) c));
                    else escaped.append(c);
                }
            }
        }
        return escaped.append('"').toString();
    }

    static String json(Object value) {
        if (value == null) return "null";
        if (value instanceof String s) return jsonString(s);
        if (value instanceof Number || value instanceof Boolean) return value.toString();
        if (value instanceof List<?> list) {
            List<String> values = new ArrayList<>();
            for (Object element : list) values.add(json(element));
            return "[" + String.join(",", values) + "]";
        }
        if (value instanceof Map<?, ?> map) {
            List<String> entries = new ArrayList<>();
            for (Map.Entry<?, ?> entry : map.entrySet()) entries.add(json(String.valueOf(entry.getKey())) + ":" + json(entry.getValue()));
            return "{" + String.join(",", entries) + "}";
        }
        throw new IllegalArgumentException("Unsupported JSON type " + value.getClass());
    }
}
