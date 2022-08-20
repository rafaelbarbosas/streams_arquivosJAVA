package br.com.letscode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import br.com.letscode.models.Movie;

public class MovieReader {
    private static MovieReader instance;

    private static final String FILE_OPENING_EXCEPTION_MSG = "Ocorreu um erro ao tentar abrir o arquivo %s";

    private static final String CSV_SEPARATOR_REGEX = ",(?=([^\"]|\"[^\"]*\")*$)";

    private MovieReader() {
    }

    public static MovieReader getInstance() {
        if (instance == null) {
            instance = new MovieReader();
        }
        return instance;
    }

    public void loadFilesInSet(Set<Movie> set, Path filePath, Boolean skipHeader) {
        try (Stream<String> fileReader = Files.lines(filePath)) {
            fileReader.skip(skipHeader ? 1 : 0)
                    .forEach(line -> {
                        List<String> movieData = List.of(line.split(CSV_SEPARATOR_REGEX, -1));
                        try {
                            Movie movie = Movie.fromCsvFileEntry(movieData);
                            System.out.println(movie);
                            set.add(movie);
                        } catch (RuntimeException e) {
                            // skip entry
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException(String.format(FILE_OPENING_EXCEPTION_MSG, filePath.toString()), e);
        }
    }

    public void loadFilesInSet(Set<Movie> set, Path filePath) {
        loadFilesInSet(set, filePath, false);
    }
}
