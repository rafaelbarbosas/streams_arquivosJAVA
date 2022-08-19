package br.com.letscode.utils;

import java.io.File;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class FileUtils {
    public static final String CURRENT_DIRECTORY_PATH = Paths.get("").toAbsolutePath().toString();

    public static String getFullFilePath(String fileName) {
        return CURRENT_DIRECTORY_PATH + "/" + fileName;
    }

    public static Set<String> listFilesInDirectory(File directory) {
        return Stream.of(directory.listFiles())
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toSet());
    }
}
