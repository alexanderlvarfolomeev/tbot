package com.aorise;

import java.nio.file.Path;
import java.nio.file.Paths;

public class RunnerConst {
    public static final Path LOGS_DIR = Paths.get("logs");
    public static final Path ERROR_LOG = LOGS_DIR.resolve("err.log");
    public static final Path RESOURCES_DIR = Paths.get("src", "main", "resources");
    public static final Path PROPERTIES_DIR = RESOURCES_DIR.resolve("props");
    public static final Path BOT_PROPERTIES = PROPERTIES_DIR.resolve("bot.properties");
    public static final Path LOGGING_PROPERTIES = PROPERTIES_DIR.resolve("logging.properties");
}
