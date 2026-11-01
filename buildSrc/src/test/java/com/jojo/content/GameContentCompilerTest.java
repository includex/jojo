package com.jojo.content;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameContentCompilerTest {
    @TempDir Path temp;

    @Test void scenarioSourcesAreOwnedByTheSeparateAstPipeline() throws Exception {
        Path content = fixture();
        Files.createDirectories(content.resolve("scenarios"));
        Files.writeString(content.resolve("scenarios/R_00.py"), "def scene0(): pass\n");
        Files.writeString(content.resolve("scenarios/dialogue-text.json"), "{}");
        GameContentCompiler.compile(content, temp.resolve("compiled"));
        assertTrue(Files.exists(temp.resolve("compiled/data/unit.bin")));
        Files.writeString(content.resolve("unexpected.py"), "pass");
        assertThrows(IllegalArgumentException.class,
                () -> GameContentCompiler.compile(content, temp.resolve("bad")));
    }

    @Test void encodingMatchesRuntimeRotationAndDigest() throws Exception {
        String json = "[{\"0\":\"조조\\n\\\"test\\\"\"}]";
        byte[] encoded = GameContentCompiler.encode(json);
        String decoded = decode(encoded);
        assertEquals(json, decoded.substring(32));
        byte[] digest = MessageDigest.getInstance("MD5").digest(("ccz65Sha08GeZ1Fu" + json).getBytes(StandardCharsets.UTF_8));
        assertEquals(toHex(digest), decoded.substring(0, 32));
    }

    @Test void compilesCurrentContentDeterministically() throws Exception {
        Path content = Path.of("../content").toAbsolutePath().normalize();
        Path first = temp.resolve("first"), second = temp.resolve("second");
        GameContentCompiler.compile(content, first);
        GameContentCompiler.compile(content, second);
        for (String table : List.of("unit", "item", "magic", "defineSkill", "unitPostsSkill", "itemSkills")) {
            byte[] output = Files.readAllBytes(first.resolve("data/" + table + ".bin"));
            assertArrayEquals(output, Files.readAllBytes(second.resolve("data/" + table + ".bin")));
            assertTrue(decode(output).substring(32).length() > 2);
        }
    }

    @Test void invalidRecordsFailWithPathAndReason() throws Exception {
        Path content = fixture();
        Path first = content.resolve("characters/0000.yaml");
        String original = Files.readString(first);
        Files.writeString(first, original + "unknownThing: 1\n");
        assertTrue(assertThrows(IllegalArgumentException.class,
                () -> GameContentCompiler.compile(content, temp.resolve("badUnknown"))).getMessage().contains("unknown field unknownThing"));
        Files.writeString(first, original.replace("id: 0", "id: -1"));
        assertTrue(assertThrows(IllegalArgumentException.class,
                () -> GameContentCompiler.compile(content, temp.resolve("badId"))).getMessage().contains("id must be a nonnegative integer"));
        Files.writeString(first, original.replace("id: 0", "id: 1"));
        assertTrue(assertThrows(IllegalArgumentException.class,
                () -> GameContentCompiler.compile(content, temp.resolve("gap"))).getMessage().contains("missing id 0"));
        Files.writeString(first, original.replace("attack: 41", "attack: invalid"));
        assertTrue(assertThrows(IllegalArgumentException.class,
                () -> GameContentCompiler.compile(content, temp.resolve("badType"))).getMessage().contains("field attack must be integer"));
        Files.writeString(first, original + "name: duplicate\n");
        assertTrue(assertThrows(IllegalArgumentException.class,
                () -> GameContentCompiler.compile(content, temp.resolve("duplicateKey"))).getMessage().contains("invalid YAML"));
        Files.writeString(first, original);
        Files.writeString(content.resolve("characters/0001.yaml"), original.replace("id: 0", "id: 0"));
        assertTrue(assertThrows(IllegalArgumentException.class,
                () -> GameContentCompiler.compile(content, temp.resolve("duplicateId"))).getMessage().contains("duplicate id 0"));
    }

    @Test void changingTextChangesBinaryAndEscapesCorrectly() throws Exception {
        Path content = fixture();
        Path file = content.resolve("characters/0000.yaml");
        String original = Files.readString(file);
        GameContentCompiler.compile(content, temp.resolve("before"));
        Files.writeString(file, original.replace("name: \"테스트\"", "name: \"새\\n이름\""));
        GameContentCompiler.compile(content, temp.resolve("after"));
        byte[] before = Files.readAllBytes(temp.resolve("before/data/unit.bin"));
        byte[] after = Files.readAllBytes(temp.resolve("after/data/unit.bin"));
        assertFalse(Arrays.equals(before, after));
        assertTrue(decode(after).contains("새\\n이름"));
    }

    @Test void jsonEscapesEveryControlCharacter() {
        String source = "\u0000\b\f\n\r\t\u001f";
        assertEquals("\"\\u0000\\b\\f\\n\\r\\t\\u001f\"", GameContentCompiler.json(source));
    }

    @Test void invalidEffectReferenceNamesSourceFile() throws Exception {
        Path content = fixture();
        Path equipment = content.resolve("equipment/0000.yaml");
        Files.writeString(equipment, Files.readString(equipment).replace("skillEffects: [0]", "skillEffects: [999999]"));
        String invalid = assertThrows(IllegalArgumentException.class,
                () -> GameContentCompiler.compile(content, temp.resolve("reference"))).getMessage();
        assertTrue(invalid.contains("equipment/0000.yaml"), invalid);
        assertTrue(invalid.contains("skillEffects index 999999"), invalid);
    }

    private Path fixture() throws IOException {
        Path content = temp.resolve("content");
        Files.createDirectories(content);
        Files.copy(Path.of("../content/schema.json"), content.resolve("schema.json"));
        write(content, "characters/0000.yaml", "id: 0\nname: \"테스트\"\nattack: 41\n");
        write(content, "equipment/0000.yaml", "id: 0\nname: \"아이템\"\nskillEffects: [0]\n");
        write(content, "skills/active/0000.yaml", "id: 0\nname: \"마법\"\n");
        write(content, "skills/passive/0000.yaml", "id: 0\nname: \"기술\"\n");
        write(content, "skills/assignments/0000.yaml", "id: 0\nname: \"배정\"\nskillId: 0\n");
        write(content, "skills/equipment/0000.yaml", "id: 0\nskillId: 0\n");
        return content;
    }

    private static void write(Path content, String path, String data) throws IOException {
        Path file = content.resolve(path);
        Files.createDirectories(file.getParent());
        Files.writeString(file, data);
    }

    private static String decode(byte[] bytes) {
        String key = "ccz65Sha08GeZ1Fu";
        byte[] decoded = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            int shift = key.charAt(i % key.length()) % 8;
            int value = bytes[i] & 255;
            decoded[i] = (byte) ((value >>> shift) | ((value << (8 - shift)) & 255));
        }
        return new String(decoded, StandardCharsets.UTF_8);
    }

    private static String toHex(byte[] bytes) {
        StringBuilder value = new StringBuilder();
        for (byte b : bytes) value.append(String.format("%02x", b & 255));
        return value.toString();
    }
}
