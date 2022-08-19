package br.com.letscode;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import br.com.letscode.models.Movie;
import br.com.letscode.utils.FileUtils;

public class App {

    private static final String MOVIES_FOLDER_PATH = FileUtils.getFullFilePath("data");
    private static final String FIRST_FILE_NAME = "movies1.csv";

    public static void main(String[] args) throws Exception {
        Set<Movie> moviesSet = new HashSet<>();

        File directory = new File(MOVIES_FOLDER_PATH);
        FileUtils.listFilesInDirectory(directory).stream()
                .forEach(fileName -> {
                    String filePath = MOVIES_FOLDER_PATH + "/" + fileName;
                    MovieReader.getInstance().loadFilesInSet(moviesSet,
                            Paths.get(filePath),
                            fileName.equals(FIRST_FILE_NAME));
                });

        System.out.println(moviesSet.stream().count());
    }
}
