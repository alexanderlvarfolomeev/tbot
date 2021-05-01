package com.aorise.util;

import com.aorise.BotLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Loader {
    public static Map<String, String> loadMapper(String path) throws IOException {
        Map<String, String> mapper = new HashMap<>();
        try (BufferedReader mapperReader = Files.newBufferedReader(Path.of(path), StandardCharsets.UTF_8)) {
            Properties mapperProperties = new Properties();
            mapperProperties.load(mapperReader);
            for (final String name : mapperProperties.stringPropertyNames()) {
                mapper.put(name, mapperProperties.getProperty(name));
            }
            return mapper;
        }
    }
}
