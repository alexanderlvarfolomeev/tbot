package com.aorise.bot;

import java.nio.file.Path;
import java.nio.file.Paths;

public class BotConst {
    public static final Path RESOURCES_DIR = Paths.get("src", "main", "resources");
    public static final Path MAPPING = RESOURCES_DIR.resolve("mapping.txt");
    public static final Path EXC_MAPPING = RESOURCES_DIR.resolve("exc_mapping.txt");
    public static final Path CONTEXTS = RESOURCES_DIR.resolve("contexts");
}
