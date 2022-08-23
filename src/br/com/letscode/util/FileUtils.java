package br.com.letscode.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class FileUtils {
    public static final String CURRENT_DIRECTORY_PATH = Paths.get("").toAbsolutePath().toString();

    public static final String WRITING_TO_FILE_EXCEPTION_MESSAGE = "Error while writing to a file";
    public static final String CREATING_FILE_EXCEPTION_MESSAGE = "Error while creating a file";

    public static String getFullFilePath(String fileName) {
        return CURRENT_DIRECTORY_PATH + "/" + fileName;
    }

    public static String getFullFilePath(String path, String fileName) {
        return path + "/" + fileName;
    }

    public static Set<String> listFilesInDirectory(File directory) {
        return Stream.of(directory.listFiles())
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toSet());
    }

    public static void createFolderIfNotExists(String folderPath) {
        File directory = new File(folderPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public static void createFileIfNotExists(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(CREATING_FILE_EXCEPTION_MESSAGE, e);
            }
        }
    }

    public static void writeStreamToFile(Stream<?> stream, String filePath, String charSet, boolean append) {
        final File outputFile = new File(filePath);
        try (PrintWriter fileWriter = new PrintWriter(
                new BufferedWriter(
                        new OutputStreamWriter(
                                new FileOutputStream(outputFile), charSet)))) {
            stream.forEach(fileWriter::println);
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            throw new RuntimeException(WRITING_TO_FILE_EXCEPTION_MESSAGE, e);
        }
    }
}
