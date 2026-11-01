package com.jojo.content;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import java.nio.file.*;
import java.io.IOException;
import java.util.*;

/** Structural validation for game rule declarations; installed handlers validate their own parameters at runtime. */
public final class GameRuleCompiler {
    private GameRuleCompiler() {}
    public static void compile(Path input, Path output) {
        try {
            LoaderOptions options = new LoaderOptions();
            options.setAllowDuplicateKeys(false);
            options.setMaxAliasesForCollections(0);
            Map<String,Object> root = map(new Yaml(new SafeConstructor(options)).load(Files.readString(input)));
            fields(root, Set.of("version", "resources", "rules"));
            if (integer(root.get("version")) != 1) throw new IllegalArgumentException("unsupported rule contract version");
            List<Object> resources = new ArrayList<>();
            for (var entry : map(root.get("resources")).entrySet()) {
                String id = entry.getKey(); namespaced(id);
                var r = map(entry.getValue()); fields(r, Set.of("default", "min", "max", "scope"));
                int value=integer(r.get("default")), min=integer(r.get("min")), max=integer(r.get("max"));
                if (value<min || value>max) throw new IllegalArgumentException("resource default outside bounds: " + id);
                String scope=text(r.get("scope"));
                if (!Set.of("global","character").contains(scope)) throw new IllegalArgumentException("invalid resource scope: " + id);
                String[] parts=id.split("\\.");
                resources.add(Map.of("namespace",parts[0],"key",parts[1],"defaultValue",value,"minValue",min,"maxValue",max,"perCharacter",scope.equals("character")));
            }
            if (!(root.get("rules") instanceof List<?> rules)) throw new IllegalArgumentException("rules must be a list");
            List<Object> resultRules = new ArrayList<>(); Set<String> ids=new HashSet<>();
            for (Object raw : rules) {
                var r=map(raw);fields(r,Set.of("id","event","handler","priority","parameters"));
                String id=text(r.get("id")), handler=text(r.get("handler")), event=text(r.get("event"));
                namespaced(id);namespaced(handler);
                if (!ids.add(id)) throw new IllegalArgumentException("duplicate rule id: " + id);
                if (!Set.of("TURN_START","TURN_END","ATTACK","HIT","DAMAGE_CALCULATION","ACTION_COMPLETED").contains(event)) throw new IllegalArgumentException("unknown rule event: " + event);
                var params=new TreeMap<String,String>();
                for (var param : map(r.getOrDefault("parameters",Map.of())).entrySet()) {
                    Object v=param.getValue();
                    if (!(v instanceof String) && !(v instanceof Integer)) throw new IllegalArgumentException("parameter must be string or 32-bit integer: " + param.getKey());
                    params.put(param.getKey(),v.toString());
                }
                resultRules.add(Map.of("id",id,"handlerId",handler,"event",event,"priority",integer(r.getOrDefault("priority",0)),"parameters",params,"version",1));
            }
            Files.createDirectories(output.getParent());
            Files.writeString(output,GameContentCompiler.json(Map.of("version",1,"resources",resources,"rules",resultRules))+"\n");
        } catch (IOException | RuntimeException ex) {
            throw new IllegalArgumentException(input + ": " + ex.getMessage(), ex);
        }
    }
    private static void fields(Map<String,Object> m,Set<String> allowed) {
        for(String key:m.keySet())if(!allowed.contains(key))throw new IllegalArgumentException("unknown field: "+key);
    }
    private static void namespaced(String s) {
        if(!s.matches("[A-Za-z][A-Za-z0-9_-]*\\.[A-Za-z][A-Za-z0-9_-]*"))throw new IllegalArgumentException("expected namespace.key: "+s);
    }
    private static String text(Object v) {
        if(!(v instanceof String s)||s.isBlank())throw new IllegalArgumentException("expected nonempty string");return s;
    }
    private static int integer(Object v) {
        if(!(v instanceof Integer n))throw new IllegalArgumentException("expected 32-bit integer");return n;
    }
    private static Map<String,Object> map(Object value) {
        if(!(value instanceof Map<?,?> m))throw new IllegalArgumentException("expected mapping");
        var result=new LinkedHashMap<String,Object>();
        m.forEach((k,v)->result.put(text(k),v));return result;
    }
}
