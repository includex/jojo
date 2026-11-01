package com.jojo.content;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;
class GameRuleCompilerTest {
    @TempDir Path temp;
    private Path compile(String yaml) throws Exception {
        Path source=temp.resolve("rules.yaml"),out=temp.resolve("game-rules.json");
        Files.writeString(source,yaml);GameRuleCompiler.compile(source,out);return out;
    }
    @Test void compilesNamespacedResourcesAndTypedParameters() throws Exception {
        var result=compile("""
            version: 1
            resources:
              yitian.innerEnergy: {default: 0, min: 0, max: 100, scope: character}
            rules:
              - id: yitian.regeneration
                event: TURN_START
                handler: core.resourceRegeneration
                parameters: {resource: yitian.innerEnergy, amount: 5}
            """);
        String json=Files.readString(result);
        assertTrue(json.contains("\"amount\":\"5\""));
        assertTrue(json.contains("\"perCharacter\":true"));
    }
    @Test void rejectsInvalidVersionBoundsEventAndUnknownField() {
        for(String yaml:new String[]{
            "version: 2\nresources: {}\nrules: []",
            "version: 1\nresources: {game.energy: {default: 8, min: 0, max: 5, scope: character}}\nrules: []",
            "version: 1\nresources: {}\nrules: [{id: game.rule, handler: core.recovery, event: TYPO}]",
            "version: 1\nresources: {}\nrules: []\ntypo: 1"
        }) assertThrows(IllegalArgumentException.class,()->compile(yaml));
    }
    @Test void rejectsDuplicateRulesAndInvalidParameters() {
        assertThrows(IllegalArgumentException.class,()->compile("""
            version: 1
            resources: {}
            rules:
              - {id: game.rule, handler: core.recovery, event: TURN_START}
              - {id: game.rule, handler: core.recovery, event: TURN_START}
            """));
        assertThrows(IllegalArgumentException.class,()->compile("""
            version: 1
            resources: {}
            rules: [{id: game.rule, handler: core.recovery, event: TURN_START, parameters: {hpAmount: 1.5}}]
            """));
    }
}
