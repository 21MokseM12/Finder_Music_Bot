package com.layers.database.dao.interfaces;

import com.layers.exception.DaoException;

import java.util.Optional;

public interface Dao<I, E> {

    Optional<E> findById(I id) throws DaoException;

    E save(E entity) throws DaoException;

    boolean delete(I id) throws DaoException;

    boolean update(E entity) throws DaoException;
}
