package br.com.letscode.dao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import br.com.letscode.models.Movie;
import br.com.letscode.utils.FileUtils;

public class MovieManager {
    private static MovieManager instance;

    private static final String FILE_OPENING_EXCEPTION_MSG = "Ocorreu um erro ao tentar abrir o arquivo %s";

    private static final String CSV_SEPARATOR_REGEX = ",(?=([^\"]|\"[^\"]*\")*$)";

    private MovieManager() {
    }

    public static MovieManager getInstance() {
        if (instance == null) {
            instance = new MovieManager();
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

    public static void writeMovieStreamToFile(Stream<?> stream, String filePath, String fileCharSet,
            boolean includeHeader) {
        if (includeHeader) {
            stream = Stream.concat(Stream.of(Movie.getCsvHeader()), stream);
        }
        FileUtils.createFileIfNotExists(filePath);
        FileUtils.writeStreamToFile(
                stream,
                filePath,
                fileCharSet,
                false);
    }
}
