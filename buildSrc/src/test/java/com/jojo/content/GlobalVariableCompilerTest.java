package com.jojo.content;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class GlobalVariableCompilerTest {
    @TempDir Path directory;

    private String compile(String yaml) throws Exception {
        Path input = directory.resolve("globals.yaml");
        Path output = directory.resolve("global-variables.json");
        Files.writeString(input, yaml);
        GlobalVariableCompiler.compile(input, output);
        return Files.readString(output);
    }

    @Test void compilesTypedDefaultsAndOptionalLegacyAlias() throws Exception {
        assertEquals("{\"version\":1,\"variables\":{\"campaign.score\":{\"type\":\"int\",\"default\":-2,\"description\":\"A score\",\"legacyId\":4074},\"ui.flag\":{\"type\":\"bool\",\"default\":false,\"description\":\"A flag\"}}}\n",
                compile("version: 1\nvariables:\n  campaign.score: {type: int, default: -2, description: A score, legacyId: 4074}\n  ui.flag: {type: bool, default: false, description: A flag}\n"));
    }

    @Test void rejectsMalformedRegistries() {
        String[] invalid = {
                "version: 2\nvariables: {}",
                "version: 1\nextra: true\nvariables: {}",
                "version: 1\nvariables:\n  bad-name: {type: int, default: 0, description: bad}",
                "version: 1\nvariables:\n  test.a: {type: int, default: false, description: bad}",
                "version: 1\nvariables:\n  test.a: {type: bool, default: 0, description: bad}",
                "version: 1\nvariables:\n  test.a: {type: int, default: 0, description: bad, typo: 1}",
                "version: 1\nvariables:\n  test.a: {type: int, default: 0}",
                "version: 1\nvariables:\n  test.a: {type: int, default: 0, description: a, legacyId: -1}",
                "version: 1\nvariables:\n  test.a: {type: int, default: 0, description: a, legacyId: 1}\n  test.b: {type: int, default: 0, description: b, legacyId: 1}",
                "version: 1\nvariables:\n  test.a: {type: int, default: 0, description: a}\n  test.a: {type: int, default: 1, description: b}",
                "version: 1\nvariables:\n  test.a: {type: int, default: 0, default: 1, description: a}",
                "version: 1\nvariables:\n  test.a: &a {type: int, default: 0, description: a}\n  test.b: *a"
        };
        for (String yaml : invalid) assertThrows(IllegalArgumentException.class, () -> compile(yaml), yaml);
    }
}
