package br.com.letscode.models;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Year;
import java.util.List;

public class Movie {
    private UnsignedInteger rank;
    private String title;
    private List<String> genre;
    private String description;
    private List<String> director;
    private List<String> actors;
    private Year year;
    private Duration runtime;
    private Float rating;
    private UnsignedInteger votes;
    private BigDecimal revenue;
    private MetaScore metascore;

    // Getters
    public UnsignedInteger getRank() {
        return rank;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getGenre() {
        return genre;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getDirector() {
        return director;
    }

    public List<String> getActors() {
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

        public Builder withGenre(List<String> genre) {
            movie.genre = genre;
            return this;
        }

        public Builder withDescription(String description) {
            movie.description = description;
            return this;
        }

        public Builder withDirector(List<String> director) {
            movie.director = director;
            return this;
        }

        public Builder withActors(List<String> actors) {
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
}
