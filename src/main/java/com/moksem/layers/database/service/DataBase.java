package com.moksem.layers.database.service;

import com.moksem.layers.database.dao.implementations.TrackNameDAO;
import com.moksem.layers.database.dao.implementations.TrackPathDAO;
import com.moksem.layers.database.entities.TrackName;
import com.moksem.layers.database.entities.TrackPath;
import com.moksem.layers.exception.DaoException;
import com.moksem.layers.exception.TrackNotFoundException;
import com.moksem.layers.database.config.ConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;

@Service
public class DataBase {

    private final TrackNameDAO trackNameDAO;

    private final TrackPathDAO trackPathDAO;

    @Autowired
    public DataBase(TrackNameDAO trackNameDAO, TrackPathDAO trackPathDAO) {
        this.trackNameDAO = trackNameDAO;
        this.trackPathDAO = trackPathDAO;
    }

    public void saveTrack(String trackName, String artistName, String trackPath) throws TrackNotFoundException {
        try {
            Connection connection = ConnectionManager.get();
            try {
                connection.setAutoCommit(false);

                TrackName track = new TrackName(trackName, artistName);
                TrackPath path = new TrackPath(trackPath, track);

                trackNameDAO.save(track, connection);
                trackPathDAO.save(path, connection);

                connection.commit();
            } catch (DaoException e) {
                connection.rollback();
                throw new DaoException(e);
            } finally {
                connection.close();
            }
        } catch (SQLException | DaoException e) {
            throw new TrackNotFoundException(e);
        }
    }

    public boolean containsTrack(String trackName, String artistName) throws TrackNotFoundException {
        try {
            return trackNameDAO.contains(new TrackName(trackName, artistName));
        } catch (DaoException e) {
            throw new TrackNotFoundException(e);
        }
    }

    public String getTrackPath(String trackName, String artistName) throws TrackNotFoundException {
         try {
             TrackName track = new TrackName(trackName, artistName);
             Long trackId = trackNameDAO.getId(track).orElse(0L);

             TrackPath trackPath = trackPathDAO.findByTrackId(trackId).orElse(null);

             if (trackPath != null) return trackPath.getPath();
             else throw new DaoException();
        } catch (DaoException e) {
            throw new TrackNotFoundException(e);
        }
    }
}
