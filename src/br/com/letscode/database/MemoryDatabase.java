package br.com.letscode.database;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import br.com.letscode.exception.DatabaseException;

public class MemoryDatabase<E> implements Database<E> {
    private final Set<E> database = new HashSet<>();

    public MemoryDatabase() {
    }

    public MemoryDatabase(Set<E> initial) {
        this.database.addAll(initial);
    }

    @Override
    public void save(E entity) throws DatabaseException {
        this.database.add(entity);
    }

    @Override
    public void delete(E entity) throws DatabaseException {
        this.database.remove(entity);
    }

    @Override
    public boolean exists(E entity) throws DatabaseException {
        return this.database.contains(entity);
    }

    @Override
    public Set<E> listFilter(Predicate<E> filter) throws DatabaseException {
        return this.database.stream()
                .collect(Collectors.toSet());
    }

    @Override
    public Set<E> listAll() throws DatabaseException {
        return new HashSet<>(this.database);
    }
}
