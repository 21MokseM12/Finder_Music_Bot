package com.telegram.telegrambot.dao.implementations;

import com.telegram.telegrambot.dao.interfaces.Dao;
import com.telegram.telegrambot.entities.TrackName;
import com.telegram.telegrambot.exception.DaoException;
import com.telegram.telegrambot.utils.ConnectionManager;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.Optional;

@Component
@Scope("singleton")
public class TrackNameDAO implements Dao<Long, TrackName> {

    private static final String FIND_BY_ID_SQL = """
            SELECT id,
               track_title,
               artist_name
            FROM track_name
            WHERE id = ?
            """;

    private static final String SAVE_SQL = """
            INSERT INTO track_name
            (track_title, artist_name)
            VALUES
            (?, ?)
            """;

    private static final String DELETE_SQL = """
            DELETE FROM track_name
            WHERE id = ?
            """;

    private static final String UPDATE_SQL = """
            UPDATE track_name
            SET
                track_title = ?,
                artist_name = ?
            WHERE id = ?
            """;

    private static final String CONTAINS_SQL = """
            SELECT COUNT (track_title)
            FROM track_name
            WHERE
            (track_title = ? AND artist_name = ?)
            """;

    private static final String GET_ID_SQL = """
            SELECT id
            FROM track_name
            WHERE
            (track_title = ? AND artist_name = ?)
            """;

    @Override
    public Optional<TrackName> findById(Long id) throws DaoException {
        try {
            return findById(id, ConnectionManager.get());
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public Optional<TrackName> findById(Long id, Connection connection) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            statement.setLong(1, id);

            ResultSet response = statement.executeQuery();

            TrackName result = null;
            if (response.next()) result = buildTrackName(response);

            return Optional.ofNullable(result);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public TrackName save(TrackName entity) throws DaoException {
        try {
            return save(entity, ConnectionManager.get());
        } catch (SQLException e) {
            throw new DaoException(e);
        }

    }

    public TrackName save(TrackName entity, Connection connection) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getTrackTitle());
            statement.setString(2, entity.getArtistName());

            statement.executeUpdate();
            ResultSet result = statement.getGeneratedKeys();

            if (result.next()) entity.setId(result.getLong("id"));

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
    public boolean update(TrackName entity) throws DaoException {
        try {
            return update(entity, ConnectionManager.get());
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public boolean update(TrackName entity, Connection connection) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {
            statement.setString(1, entity.getTrackTitle());
            statement.setString(2, entity.getArtistName());
            statement.setLong(3, entity.getId());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public boolean contains(TrackName trackName) throws DaoException {
        try {
            return contains(trackName, ConnectionManager.get());
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public boolean contains(TrackName trackName, Connection connection) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(CONTAINS_SQL)) {
            statement.setString(1, trackName.getTrackTitle());
            statement.setString(2, trackName.getArtistName());

            ResultSet response = statement.executeQuery();
            int result = 0;
            if (response.next()) result = response.getInt("count");
            return result > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public Optional<Long> getId(TrackName trackName) throws DaoException {
        try {
            return getId(trackName, ConnectionManager.get());
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public Optional<Long> getId(TrackName trackName, Connection connection) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(GET_ID_SQL)) {
            statement.setString(1, trackName.getTrackTitle());
            statement.setString(2, trackName.getArtistName());

            Long id = null;
            ResultSet response = statement.executeQuery();
            if (response.next()) id = response.getLong("id");

            return Optional.ofNullable(id);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private TrackName buildTrackName(ResultSet response) throws SQLException {
        return new TrackName(
                response.getLong("id"),
                response.getString("track_title"),
                response.getString("artist_name")
        );
    }
}
