package br.com.letscode;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Year;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import br.com.letscode.models.MetaScore;
import br.com.letscode.models.Movie;
import br.com.letscode.models.MoviesFilePropertiesEnum;
import br.com.letscode.models.UnsignedInteger;

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

    private static Object getMovieObjectDataFromList(List<String> movieData, MoviesFilePropertiesEnum type) {
        return type.getConversor().apply(movieData.get(type.getIndex()));
    }

    @SuppressWarnings("unchecked")
    private static Movie getMovieFromMovieData(List<String> movieData) {
        return new Movie.Builder()
                .withRank((UnsignedInteger) getMovieObjectDataFromList(movieData,
                        MoviesFilePropertiesEnum.RANK))
                .withTitle((String) getMovieObjectDataFromList(movieData,
                        MoviesFilePropertiesEnum.TITLE))
                .withGenre((Set<String>) getMovieObjectDataFromList(movieData,
                        MoviesFilePropertiesEnum.GENRE))
                .withDescription((String) getMovieObjectDataFromList(movieData,
                        MoviesFilePropertiesEnum.DESCRIPTION))
                .withDirectors((Set<String>) getMovieObjectDataFromList(movieData,
                        MoviesFilePropertiesEnum.DIRECTORS))
                .withActors((Set<String>) getMovieObjectDataFromList(movieData,
                        MoviesFilePropertiesEnum.ACTORS))
                .withYear((Year) getMovieObjectDataFromList(movieData,
                        MoviesFilePropertiesEnum.YEAR))
                .withRuntime((Duration) getMovieObjectDataFromList(movieData,
                        MoviesFilePropertiesEnum.RUNTIME))
                .withRating((Float) getMovieObjectDataFromList(movieData,
                        MoviesFilePropertiesEnum.RATING))
                .withVotes((UnsignedInteger) getMovieObjectDataFromList(movieData,
                        MoviesFilePropertiesEnum.VOTES))
                .withRevenue((BigDecimal) getMovieObjectDataFromList(movieData,
                        MoviesFilePropertiesEnum.REVENUE))
                .withMetaScore((MetaScore) getMovieObjectDataFromList(movieData,
                        MoviesFilePropertiesEnum.METASCORE))
                .build();
    }

    public void loadFilesInSet(Set<Movie> set, Path filePath, Boolean skipHeader) {
        try (Stream<String> fileReader = Files.lines(filePath)) {
            fileReader.skip(skipHeader ? 1 : 0)
                    .limit(2)
                    .forEach(line -> {
                        List<String> movieData = List.of(line.split(CSV_SEPARATOR_REGEX));
                        Movie movie = getMovieFromMovieData(movieData);
                        set.add(movie);
                    });
        } catch (IOException e) {
            throw new RuntimeException(String.format(FILE_OPENING_EXCEPTION_MSG, filePath.toString()), e);
        }
    }

    public void loadFilesInSet(Set<Movie> set, Path filePath) {
        loadFilesInSet(set, filePath, false);
    }
}
