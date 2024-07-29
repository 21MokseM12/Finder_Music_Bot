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

    @Override
    public Optional<TrackName> findById(Long id) throws DaoException {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
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
        try (Connection connection = ConnectionManager.get();
            PreparedStatement statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
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
        try (Connection connection = ConnectionManager.get();
            PreparedStatement statement = connection.prepareStatement(DELETE_SQL)) {
            statement.setLong(1, id);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean update(TrackName entity) throws DaoException {
        try (Connection connection = ConnectionManager.get();
            PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {
            statement.setString(1, entity.getTrackTitle());
            statement.setString(2, entity.getArtistName());
            statement.setLong(3, entity.getId());

            return statement.executeUpdate() > 0;
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
