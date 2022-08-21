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

import br.com.letscode.models.Movie;
import br.com.letscode.utils.FileUtils;

public class App {

    private static final String MOVIES_FOLDER_PATH = FileUtils.getFullFilePath("data");
    private static final List<String> FILES_WITH_HEADER = List.of("movies1.csv");

    // if you want to use all available processors, use this value
    // Runtime.getRuntime().availableProcessors();
    private static final int THREAD_POOL_SIZE = 4;

    public static void main(String[] args) throws Exception {
        Set<Movie> moviesSet = Collections.synchronizedSet(new HashSet<>());
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        // load files into set
        File directory = new File(MOVIES_FOLDER_PATH);
        FileUtils.listFilesInDirectory(directory).stream()
                .forEach(fileName -> {
                    String filePath = MOVIES_FOLDER_PATH + "/" + fileName;
                    executor.execute(() -> MovieReader.getInstance().loadFilesInSet(moviesSet,
                            Paths.get(filePath),
                            FILES_WITH_HEADER.contains(fileName)));
                });

        // wait for all threads to finish
        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);

        System.out.println(moviesSet.stream().count());
    }
}
