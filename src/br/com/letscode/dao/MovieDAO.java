package br.com.letscode.dao;

import java.util.Set;
import java.util.function.Predicate;

import br.com.letscode.database.Database;
import br.com.letscode.exception.DatabaseException;
import br.com.letscode.model.movie.Movie;

public abstract class MovieDAO {
    private static Database<Movie> database;

    public static void setDatabase(Database<Movie> newDatabase) {
        database = newDatabase;
    }

    public static void save(Movie entity) throws DatabaseException {
        database.save(entity);
    }

    public static void delete(Movie entity) throws DatabaseException {
        database.delete(entity);
    }

    public static boolean exists(Movie entity) throws DatabaseException {
        return database.exists(entity);
    }

    public static Set<Movie> listFilter(Predicate<Movie> filter) throws DatabaseException {
        return database.listFilter(filter);
    }

    public static Set<Movie> listAll() throws DatabaseException {
        return database.listAll();
    }
}
