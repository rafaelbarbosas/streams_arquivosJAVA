package br.com.letscode.model.movie;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Year;
import java.util.Comparator;
import java.util.Set;
import java.util.function.Function;

import br.com.letscode.model.number.UnsignedInteger;
import br.com.letscode.model.system.Conditional;
import br.com.letscode.util.FunctionUtils;

public enum MoviesFilePropertiesEnum {
    RANK(0,
            text -> new UnsignedInteger(Integer.parseInt(text))),
    TITLE(1,
            Function.identity()),
    GENRE(2,
            FunctionUtils.SET_OF_STRINGS_CONVERSOR),
    DESCRIPTION(3,
            Function.identity()),
    DIRECTOR(4,
            FunctionUtils.SET_OF_STRINGS_CONVERSOR),
    ACTORS(5,
            FunctionUtils.SET_OF_STRINGS_CONVERSOR),
    YEAR(6,
            text -> Year.of(Integer.parseInt(text))),
    RUNTIME(7,
            text -> Duration.ofMinutes(Integer.parseInt(text))),
    RATING(8,
            text -> Float.parseFloat(text)),
    VOTES(9,
            text -> new UnsignedInteger(Integer.parseInt(text))),
    REVENUE(10,
            text -> BigDecimal.valueOf(Double.parseDouble(text)).multiply(Movie.REVENUE_MULTIPLIER)),
    METASCORE(11,
            text -> new MetaScore(Integer.parseInt(text)));

    private int index;
    private Function<String, ? extends Object> conversor;

    public int getIndex() {
        return index;
    }

    public Object convert(String text) {
        if (text == null || text.strip().length() == 0) {
            return null;
        }
        if (text.charAt(0) == '"') {
            text = text.substring(1);
        }
        if (text.charAt(text.length() - 1) == '"') {
            text = text.substring(0, text.length() - 1);
        }
        return conversor.apply(text);
    }

    public Comparator<Movie> getPropertyComparator() {
        switch (this) {
            case RANK:
                return (m1, m2) -> m1.getRank().compareTo(m2.getRank());
            case TITLE:
                return (m1, m2) -> m1.getTitle().toUpperCase().compareTo(m2.getTitle().toUpperCase());
            case GENRE:
                return (m1, m2) -> m1.getGenre().stream().sorted()
                        .reduce("", (partial, full) -> partial.toUpperCase().concat(full))
                        .compareTo(m2.getGenre().stream().sorted().reduce("",
                                (partial, full) -> partial.toUpperCase().concat(full)));
            case DESCRIPTION:
                return (m1, m2) -> m1.getDescription().toUpperCase().compareTo(m2.getDescription().toUpperCase());
            case DIRECTOR:
                return (m1, m2) -> m1.getDirectors().stream().sorted()
                        .reduce("", (partial, full) -> partial.toUpperCase().concat(full))
                        .compareTo(m2.getDirectors().stream().sorted().reduce("",
                                (partial, full) -> partial.toUpperCase().concat(full)));
            case ACTORS:
                return (m1, m2) -> m1.getActors().stream().sorted()
                        .reduce("", (partial, full) -> partial.toUpperCase().concat(full))
                        .compareTo(
                                m2.getActors().stream().sorted().reduce("",
                                        (partial, full) -> partial.toUpperCase().concat(full)));
            case YEAR:
                return (m1, m2) -> m1.getYear().compareTo(m2.getYear());
            case RUNTIME:
                return (m1, m2) -> m1.getRuntime().compareTo(m2.getRuntime());
            case RATING:
                return (m1, m2) -> m1.getRating().compareTo(m2.getRating());
            case VOTES:
                return (m1, m2) -> m1.getVotes().compareTo(m2.getVotes());
            case REVENUE:
                return (m1, m2) -> m1.getRevenue().compareTo(m2.getRevenue());
            case METASCORE:
                return (m1, m2) -> Integer.compare(m1.getMetascore() != null ? m1.getMetascore().getValue() : -1,
                        m2.getMetascore() != null ? m2.getMetascore().getValue() : -1);
        }

        return (m1, m2) -> m1.compareTo(m2);
    }

    @SuppressWarnings("unchecked")
    public boolean testFilter(Movie movie, Conditional conditional, String condition) {
        Movie.Builder mockMovieBuilder = new Movie.Builder();
        mockMovieBuilder.withTitle("");
        mockMovieBuilder.withYear(Year.of(0));

        switch (this) {
            case RANK:
                mockMovieBuilder.withRank((UnsignedInteger) this.convert(condition));
                break;
            case TITLE:
                mockMovieBuilder.withTitle((String) this.convert(condition));
                break;
            case GENRE:
                mockMovieBuilder.withGenre((Set<String>) this.convert(condition));
                break;
            case DESCRIPTION:
                mockMovieBuilder.withDescription((String) this.convert(condition));
                break;
            case DIRECTOR:
                mockMovieBuilder.withDirectors((Set<String>) this.convert(condition));
                break;
            case ACTORS:
                mockMovieBuilder.withActors((Set<String>) this.convert(condition));
                break;
            case YEAR:
                mockMovieBuilder.withYear((Year) this.convert(condition));
                break;
            case RUNTIME:
                mockMovieBuilder.withRuntime((Duration) this.convert(condition));
                break;
            case RATING:
                mockMovieBuilder.withRating((Float) this.convert(condition));
                break;
            case VOTES:
                mockMovieBuilder.withVotes((UnsignedInteger) this.convert(condition));
                break;
            case REVENUE:
                mockMovieBuilder.withRevenue((BigDecimal) this.convert(condition));
                break;
            case METASCORE:
                mockMovieBuilder.withMetaScore((MetaScore) this.convert(condition));
                break;
        }

        return conditional.test(this.getPropertyComparator(), movie, mockMovieBuilder.build());
    }

    private MoviesFilePropertiesEnum(int index, Function<String, ? extends Object> conversor) {
        this.index = index;
        this.conversor = conversor;
    }
}
