package br.com.letscode;

import java.io.File;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import br.com.letscode.models.Movie;
import br.com.letscode.utils.FileUtils;

public class App {

    private static final String MOVIES_FOLDER_PATH = FileUtils.getFullFilePath("data");
    private static final List<String> FILES_WITH_HEADER = List.of("movies1.csv");

    private static final String OUTPUT_FOLDER_PATH = FileUtils.getFullFilePath("output");
    private static final String BEST_HORROR_FILE_NAME = "best_horror.csv";
    private static final String BEST_OF_YEAR_FILE_NAME = "best_of_year_%s.csv";

    private static final String FILES_CHAR_SET = "UTF-8";

    // if you want to use all available processors, use this value
    // Runtime.getRuntime().availableProcessors();
    private static final int THREAD_POOL_SIZE = 4;

    private static final String INTERRUPTED_EXCEPTION_MESSAGE = "Error while processing a file";

    private static Set<Movie> loadFilesInMemory() {
        Set<Movie> moviesSet = Collections.synchronizedSet(new HashSet<>());
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        // load files into set
        File directory = new File(MOVIES_FOLDER_PATH);
        FileUtils.listFilesInDirectory(directory).stream()
                .forEach(fileName -> {
                    String filePath = FileUtils.getFullFilePath(MOVIES_FOLDER_PATH, fileName);
                    executor.execute(() -> MovieReader.getInstance().loadFilesInSet(moviesSet,
                            Paths.get(filePath),
                            FILES_WITH_HEADER.contains(fileName)));
                });

        // wait for all threads to finish
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(INTERRUPTED_EXCEPTION_MESSAGE, e);
        }

        return moviesSet;
    }

    private static void writeMovieStreamToFile(Stream<?> stream, String filePath, boolean includeHeader) {
        if (includeHeader) {
            stream = Stream.concat(Stream.of(Movie.getCsvHeader()), stream);
        }
        FileUtils.createFileIfNotExists(filePath);
        FileUtils.writeStreamToFile(
                stream,
                filePath,
                FILES_CHAR_SET,
                false);
    }

    public static void main(String[] args) {
        Set<Movie> moviesSet = loadFilesInMemory();

        FileUtils.createFolderIfNotExists(OUTPUT_FOLDER_PATH);

        writeMovieStreamToFile(
                moviesSet.stream()
                        .filter(movie -> movie.getGenre().contains("Horror"))
                        .sorted()
                        .sorted((m1, m2) -> m2.getRating().compareTo(m1.getRating()))
                        .limit(20),
                FileUtils.getFullFilePath(OUTPUT_FOLDER_PATH, BEST_HORROR_FILE_NAME),
                true);

        // TODO: create a file for each year with the best 50 movies of each year

        // TODO: create a file with the processing time
    }
}
