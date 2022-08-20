package br.com.letscode;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.letscode.models.Movie;
import br.com.letscode.utils.FileUtils;

public class App {

    private static final String MOVIES_FOLDER_PATH = FileUtils.getFullFilePath("data");
    private static final String FIRST_FILE_NAME = "movies1.csv";

    private static final String THREAD_INTERRUPTED_EXCEPTION = "Ocorreu um erro na execução do programa. Por favor, tente novamente.";

    public static void main(String[] args) throws Exception {
        Set<Movie> moviesSet = Collections.synchronizedSet(new HashSet<>());

        // load files into set
        File directory = new File(MOVIES_FOLDER_PATH);
        List<Thread> tasks = new ArrayList<>();
        FileUtils.listFilesInDirectory(directory).stream()
                .forEach(fileName -> {
                    String filePath = MOVIES_FOLDER_PATH + "/" + fileName;
                    Thread thread = new Thread(() -> MovieReader.getInstance().loadFilesInSet(moviesSet,
                            Paths.get(filePath),
                            fileName.equals(FIRST_FILE_NAME)));
                    tasks.add(thread);
                    thread.start();
                });

        // wait for all threads to finish
        tasks.stream().forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(THREAD_INTERRUPTED_EXCEPTION, e);
            }
        });

        System.out.println(moviesSet.stream().count());
    }
}
