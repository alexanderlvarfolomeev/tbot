package com.aorise.test;

import com.aorise.bot.FileBot;
import com.aorise.bot.handlers.MimeHandler;

import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Test {
    public static void main(String... args) {
        try (Stream<Path> pathStream = Files.walk(Path.of("data"))) {
//            System.out.println(
//                    pathStream
//                            .filter(p -> !Files.isDirectory(p))
//                            .map(p -> URLConnection.guessContentTypeFromName(p.getFileName().toString()))
//                    .collect(Collectors.toMap(Function.identity(), x -> 1, Integer::sum))
//            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
