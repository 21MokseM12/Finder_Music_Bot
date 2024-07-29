package com.telegram.telegrambot.dao.implementations;

import com.telegram.telegrambot.dao.interfaces.Dao;
import com.telegram.telegrambot.entities.TrackPath;
import com.telegram.telegrambot.exception.DaoException;
import com.telegram.telegrambot.utils.ConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.Optional;

@Component
@Scope("singleton")
public class TrackPathDAO implements Dao<Long, TrackPath> {

    private final TrackNameDAO trackNameDao;

    private static final String FIND_BY_ID_SQL = """
        SELECT
            id,
            path,
            track_id
        FROM track_path
        WHERE id = ?
        """;

    private static final String SAVE_SQL = """
        INSERT INTO track_path
        (path, track_id)
        VALUES
        (?, ?)
        """;

    private static final String DELETE_SQL = """
        DELETE
        FROM track_path
        WHERE id = ?
        """;

    private static final String UPDATE_SQL = """
        UPDATE track_path
        SET
        path = ?,
        track_id = ?
        WHERE id = ?
        """;

    @Autowired
    private TrackPathDAO(TrackNameDAO trackNameDao) {
        this.trackNameDao = trackNameDao;
    }

    public TrackPathDAO() {
        trackNameDao = new TrackNameDAO();
    }

    @Override
    public Optional<TrackPath> findById(Long id) throws DaoException {
        try {
            return findById(id, ConnectionManager.get());
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public Optional<TrackPath> findById(Long id, Connection connection) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            statement.setLong(1, id);

            TrackPath result = null;
            ResultSet response = statement.executeQuery();
            if (response.next()) result = buildTrackPath(response);

            return Optional.ofNullable(result);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public TrackPath save(TrackPath entity) throws DaoException {
        try {
            return save(entity, ConnectionManager.get());
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public TrackPath save(TrackPath entity, Connection connection) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getPath());
            statement.setLong(2, entity.getTrackName().getId());

            statement.executeUpdate();

            ResultSet response = statement.getGeneratedKeys();
            if (response.next()) entity.setId(response.getLong("id"));

            return entity;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean delete(Long id) throws DaoException {
        try {
            return delete(id, ConnectionManager.get());
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public boolean delete(Long id, Connection connection) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_SQL)) {
            statement.setLong(1, id);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean update(TrackPath entity) throws DaoException {
        try {
            return update(entity, ConnectionManager.get());
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public boolean update(TrackPath entity, Connection connection) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {
            statement.setString(1, entity.getPath());
            statement.setLong(2, entity.getTrackName().getId());
            statement.setLong(3, entity.getId());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private TrackPath buildTrackPath(ResultSet response) throws SQLException {
        try {
            return new TrackPath(
                    response.getLong("id"),
                    response.getString("path"),
                    trackNameDao.findById(response.getLong("track_id")).orElse(null)
            );
        } catch (DaoException e) {
            throw new SQLException(e);
        }
    }
}
