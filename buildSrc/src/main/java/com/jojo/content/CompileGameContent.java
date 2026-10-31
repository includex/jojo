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
        GameContentCompiler.compile(getSourceDirectory().get().getAsFile().toPath(),
                getOutputDirectory().get().getAsFile().toPath());
    }
}
