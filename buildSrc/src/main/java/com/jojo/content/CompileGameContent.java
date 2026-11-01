package com.jojo.content;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;

/** Compiles reviewed YAML game tables to the binary resource format read by GameDataRepository. */
public abstract class CompileGameContent extends DefaultTask {
    @InputDirectory
    public abstract DirectoryProperty getSourceDirectory();

    @OutputDirectory
    public abstract DirectoryProperty getOutputDirectory();

    public CompileGameContent() {
        setGroup("content");
        setDescription("Compiles reviewed YAML game content into encrypted runtime tables.");
    }

    @TaskAction
    public void compile() {
        getProject().delete(getOutputDirectory().get().getAsFile());
        GameContentCompiler.compile(getSourceDirectory().get().getAsFile().toPath(),
                getOutputDirectory().get().getAsFile().toPath());
        GlobalVariableCompiler.compile(getSourceDirectory().get().getAsFile().toPath().resolve("variables/globals.yaml"),
                getOutputDirectory().get().getAsFile().toPath().resolve("global-variables.json"));
        GameRuleCompiler.compile(getSourceDirectory().get().getAsFile().toPath().resolve("rules/game-rules.yaml"),
                getOutputDirectory().get().getAsFile().toPath().resolve("game-rules.json"));
        GamePackageCompiler.compile(getSourceDirectory().get().getAsFile().toPath().resolve("game.yaml"),
                getOutputDirectory().get().getAsFile().toPath().resolve("game-package.json"));
    }
}
