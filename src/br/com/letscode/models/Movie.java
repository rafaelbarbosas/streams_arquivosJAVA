package br.com.letscode.models;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Year;
import java.util.List;
import java.util.Set;

import br.com.letscode.utils.StringUtils;

public class Movie implements Comparable<Movie> {
    private UnsignedInteger rank;
    private String title;
    private Set<String> genre;
    private String description;
    private Set<String> directors;
    private Set<String> actors;
    private Year year;
    private Duration runtime;
    private Float rating;
    private UnsignedInteger votes;
    private BigDecimal revenue;
    private MetaScore metascore;

    public static final BigDecimal REVENUE_MULTIPLIER = BigDecimal.valueOf(1000000);

    public UnsignedInteger getRank() {
        return rank;
    }

    public String getTitle() {
        return title;
    }

    public Set<String> getGenre() {
        return genre;
    }

    public String getDescription() {
        return description;
    }

    public Set<String> getDirectors() {
        return directors;
    }

    public Set<String> getActors() {
        return actors;
    }

    public Year getYear() {
        return year;
    }

    public Duration getRuntime() {
        return runtime;
    }

    public Float getRating() {
        return rating;
    }

    public UnsignedInteger getVotes() {
        return votes;
    }

    public BigDecimal getRevenue() {
        return revenue;
    }

    public MetaScore getMetascore() {
        return metascore;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((actors == null) ? 0 : actors.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((directors == null) ? 0 : directors.hashCode());
        result = prime * result + ((genre == null) ? 0 : genre.hashCode());
        result = prime * result + ((metascore == null) ? 0 : metascore.hashCode());
        result = prime * result + ((rank == null) ? 0 : rank.hashCode());
        result = prime * result + ((rating == null) ? 0 : rating.hashCode());
        result = prime * result + ((revenue == null) ? 0 : revenue.hashCode());
        result = prime * result + ((runtime == null) ? 0 : runtime.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        result = prime * result + ((votes == null) ? 0 : votes.hashCode());
        result = prime * result + ((year == null) ? 0 : year.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Movie other = (Movie) obj;
        if (actors == null) {
            if (other.actors != null)
                return false;
        } else if (!actors.equals(other.actors))
            return false;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (directors == null) {
            if (other.directors != null)
                return false;
        } else if (!directors.equals(other.directors))
            return false;
        if (genre == null) {
            if (other.genre != null)
                return false;
        } else if (!genre.equals(other.genre))
            return false;
        if (metascore == null) {
            if (other.metascore != null)
                return false;
        } else if (!metascore.equals(other.metascore))
            return false;
        if (rank == null) {
            if (other.rank != null)
                return false;
        } else if (!rank.equals(other.rank))
            return false;
        if (rating == null) {
            if (other.rating != null)
                return false;
        } else if (!rating.equals(other.rating))
            return false;
        if (revenue == null) {
            if (other.revenue != null)
                return false;
        } else if (!revenue.equals(other.revenue))
            return false;
        if (runtime == null) {
            if (other.runtime != null)
                return false;
        } else if (!runtime.equals(other.runtime))
            return false;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        if (votes == null) {
            if (other.votes != null)
                return false;
        } else if (!votes.equals(other.votes))
            return false;
        if (year == null) {
            if (other.year != null)
                return false;
        } else if (!year.equals(other.year))
            return false;
        return true;
    }

    @Override
    public int compareTo(Movie o) {
        return Integer.valueOf(rank.getValue()).compareTo(o.getRank().getValue());
    }

    public static String getCsvHeader() {
        return "Rank,Title,Genre,Description,Director,Actors,Year,Runtime (Minutes),Rating,Votes,Revenue (Millions),Metascore";
    }

    @Override
    public String toString() {
        return rank
                + ","
                + StringUtils.surroundIfContains(title, "\"", ",")
                + ","
                + StringUtils.getCsvRepresentation(genre, true)
                + ","
                + StringUtils.surroundIfContains(description, "\"", ",")
                + ","
                + StringUtils.getCsvRepresentation(directors)
                + ","
                + StringUtils.getCsvRepresentation(actors, true)
                + ","
                + year
                + ","
                + runtime.toMinutes()
                + ","
                + rating
                + ","
                + votes
                + ","
                + (revenue != null ? revenue.divide(REVENUE_MULTIPLIER) : "")
                + ","
                + (metascore != null ? metascore : "");
    }

    public static class Builder {
        private Movie movie;

        public Builder() {
            movie = new Movie();
        }

        public Builder withRank(UnsignedInteger rank) {
            movie.rank = rank;
            return this;
        }

        public Builder withTitle(String title) {
            movie.title = title;
            return this;
        }

        public Builder withGenre(Set<String> genre) {
            movie.genre = genre;
            return this;
        }

        public Builder withDescription(String description) {
            movie.description = description;
            return this;
        }

        public Builder withDirectors(Set<String> directors) {
            movie.directors = directors;
            return this;
        }

        public Builder withActors(Set<String> actors) {
            movie.actors = actors;
            return this;
        }

        public Builder withYear(Year year) {
            movie.year = year;
            return this;
        }

        public Builder withRuntime(Duration runtime) {
            movie.runtime = runtime;
            return this;
        }

        public Builder withRating(Float rating) {
            movie.rating = rating;
            return this;
        }

        public Builder withVotes(UnsignedInteger votes) {
            movie.votes = votes;
            return this;
        }

        public Builder withRevenue(BigDecimal revenue) {
            movie.revenue = revenue;
            return this;
        }

        public Builder withMetaScore(MetaScore metascore) {
            movie.metascore = metascore;
            return this;
        }

        private void validate() {
            if (movie.title == null) {
                throw new IllegalArgumentException("title is required");
            }
            if (movie.year == null) {
                throw new IllegalArgumentException("year is required");
            }
        }

        public Movie build() {
            this.validate();
            return movie;
        }
    }

    @SuppressWarnings("unchecked")
    public static Movie fromCsvFileEntry(List<String> movieData) {
        MoviesFilePropertiesEnum type;
        Builder builder = new Builder();

        type = MoviesFilePropertiesEnum.RANK;
        builder.withRank((UnsignedInteger) type.convert(movieData.get(type.getIndex())));

        type = MoviesFilePropertiesEnum.TITLE;
        builder.withTitle((String) type.convert(movieData.get(type.getIndex())));

        type = MoviesFilePropertiesEnum.GENRE;
        builder.withGenre((Set<String>) type.convert(movieData.get(type.getIndex())));

        type = MoviesFilePropertiesEnum.DESCRIPTION;
        builder.withDescription((String) type.convert(movieData.get(type.getIndex())));

        type = MoviesFilePropertiesEnum.DIRECTORS;
        builder.withDirectors((Set<String>) type.convert(movieData.get(type.getIndex())));

        type = MoviesFilePropertiesEnum.ACTORS;
        builder.withActors((Set<String>) type.convert(movieData.get(type.getIndex())));

        type = MoviesFilePropertiesEnum.YEAR;
        builder.withYear((Year) type.convert(movieData.get(type.getIndex())));

        type = MoviesFilePropertiesEnum.RUNTIME;
        builder.withRuntime((Duration) type.convert(movieData.get(type.getIndex())));

        type = MoviesFilePropertiesEnum.RATING;
        builder.withRating((Float) type.convert(movieData.get(type.getIndex())));

        type = MoviesFilePropertiesEnum.VOTES;
        builder.withVotes((UnsignedInteger) type.convert(movieData.get(type.getIndex())));

        type = MoviesFilePropertiesEnum.REVENUE;
        builder.withRevenue((BigDecimal) type.convert(movieData.get(type.getIndex())));

        type = MoviesFilePropertiesEnum.METASCORE;
        builder.withMetaScore((MetaScore) type.convert(movieData.get(type.getIndex())));

        return builder.build();
    }
}
