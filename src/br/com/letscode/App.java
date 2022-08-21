package br.com.letscode;

import java.io.File;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
    private static final String PROCESSING_TIME_FILE_NAME = "processing_time.csv";

    private static final String FILES_CHAR_SET = "UTF-8";

    // if you want to use all available processors, use this value
    // Runtime.getRuntime().availableProcessors();
    private static final int THREAD_POOL_SIZE = 4;

    private static final String INTERRUPTED_EXCEPTION_MESSAGE = "Error while processing a file";

    private static final DateTimeFormatter PROCESSING_TIME_FORMATTER = DateTimeFormatter
            .ofPattern("dd/MM/yyyy HH:mm:ss.SSS").withZone(ZoneId.systemDefault());
    private static final String PROCESSING_TIME_FILE_MODEL = "Início processamento: %s" + System.lineSeparator()
            + "Fim processamento: %s" + System.lineSeparator()
            + "Tempo em milissegundos: %s milissegundos" + System.lineSeparator()
            + "Tempo em segundos: %s segundos";

    private static void waitForThreadsToFinish(ThreadPoolExecutor threadPoolExecutor) {
        threadPoolExecutor.shutdown();
        try {
            threadPoolExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(INTERRUPTED_EXCEPTION_MESSAGE, e);
        }
    }

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

        waitForThreadsToFinish(executor);

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
        final Instant startTime = Instant.now();

        Set<Movie> moviesSet = loadFilesInMemory();

        FileUtils.createFolderIfNotExists(OUTPUT_FOLDER_PATH);
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        // best horror movies
        executor.execute(() -> writeMovieStreamToFile(
                moviesSet.stream()
                        .filter(movie -> movie.getGenre().contains("Horror"))
                        .sorted()
                        .sorted((m1, m2) -> m2.getRating().compareTo(m1.getRating()))
                        .limit(20),
                FileUtils.getFullFilePath(OUTPUT_FOLDER_PATH, BEST_HORROR_FILE_NAME),
                true));

        // best movies of each year
        moviesSet.stream()
                .map(movie -> movie.getYear())
                .distinct()
                .forEach(year -> executor.execute(() -> writeMovieStreamToFile(
                        moviesSet.stream()
                                .filter(movie -> movie.getYear().equals(year))
                                .sorted()
                                .sorted((m1, m2) -> m2.getRating().compareTo(m1.getRating()))
                                .limit(50),
                        FileUtils.getFullFilePath(OUTPUT_FOLDER_PATH, String.format(BEST_OF_YEAR_FILE_NAME, year)),
                        true)));

        waitForThreadsToFinish(executor);

        // processing time
        final Instant finishTime = Instant.now();
        final Duration processingTime = Duration.between(startTime, finishTime);

        final String processingTimeFilePath = FileUtils.getFullFilePath(OUTPUT_FOLDER_PATH, PROCESSING_TIME_FILE_NAME);
        FileUtils.createFileIfNotExists(processingTimeFilePath);
        FileUtils.writeStreamToFile(
                String.format(PROCESSING_TIME_FILE_MODEL,
                        PROCESSING_TIME_FORMATTER.format(startTime),
                        PROCESSING_TIME_FORMATTER.format(finishTime),
                        processingTime.toMillis(),
                        processingTime.getSeconds()).lines(),
                processingTimeFilePath,
                FILES_CHAR_SET,
                false);

        System.out.println("tasks executed: " + executor.getTaskCount());
        System.out.println("cores used: " + executor.getCorePoolSize());
    }
}
