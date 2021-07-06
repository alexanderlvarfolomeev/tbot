package com.aorise.bot;

import java.nio.file.Path;

import static com.aorise.RunnerConst.RESOURCES_DIR;

public class BotConst {
    public static final Path MAPPING = RESOURCES_DIR.resolve("mapping.txt");
    public static final Path EXC_MAPPING = RESOURCES_DIR.resolve("exc_mapping.txt");
    public static final Path ACC_MAPPING = RESOURCES_DIR.resolve("acc_mapping.txt");
    public static final Path CONTEXTS = RESOURCES_DIR.resolve("contexts");
}
