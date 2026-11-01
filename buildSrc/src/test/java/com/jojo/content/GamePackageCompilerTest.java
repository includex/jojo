package com.jojo.content;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class GamePackageCompilerTest {
    @TempDir Path temp;

    @Test void compilesManifestPairsAndRejectsUnknownStartScene() throws Exception {
        Path content = temp.resolve("content");
        Files.createDirectories(content.resolve("scenarios"));
        Files.createDirectories(content.resolve("characters"));
        Files.writeString(content.resolve("characters/0000.yaml"), "id: 0\nname: Test\n");
        Files.writeString(content.resolve("scenarios/manifest.json"), """
                {"R_00":{"functions":["scene0","scene1"]},"S_00":{},"R_01":{},"S_01":{}}
                """);
        Path yaml = content.resolve("game.yaml");
        Files.writeString(yaml, """
                id: jojo
                title: JoJo
                startModule: R_00
                startScene: scene0
                resumeScene: scene1
                protagonistId: 0
                saveNamespace: jojo-game
                defaultBattleModule: S_00
                battleModules:
                  R_00: S_00
                  R_01: S_01
                """);
        Path output = temp.resolve("out/game-package.json");
        GamePackageCompiler.compile(yaml, output);
        String json = Files.readString(output);
        assertTrue(json.contains("\"battleModules\":{\"R_00\":\"S_00\",\"R_01\":\"S_01\"}"), json);

        Files.writeString(yaml, Files.readString(yaml).replace("startScene: scene0", "startScene: missing"));
        assertTrue(assertThrows(IllegalArgumentException.class,
                () -> GamePackageCompiler.compile(yaml, output)).getMessage().contains("startScene missing"));
    }
}
