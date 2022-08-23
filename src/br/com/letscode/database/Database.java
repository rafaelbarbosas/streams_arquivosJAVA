package br.com.letscode.database;

import java.util.Set;
import java.util.function.Predicate;

import br.com.letscode.exception.DatabaseException;

public interface Database<E> {
    public void save(E entity) throws DatabaseException;

    public void delete(E entity) throws DatabaseException;

    public boolean exists(E entity) throws DatabaseException;

    public Set<E> listFilter(Predicate<E> filter) throws DatabaseException;

    public Set<E> listAll() throws DatabaseException;
}
