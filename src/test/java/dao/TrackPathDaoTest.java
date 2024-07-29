package dao;

import com.telegram.telegrambot.dao.implementations.TrackNameDAO;
import com.telegram.telegrambot.dao.implementations.TrackPathDAO;
import com.telegram.telegrambot.entities.TrackPath;
import com.telegram.telegrambot.exception.DaoException;
import org.junit.jupiter.api.Test;

public class TrackPathDaoTest {

    private final TrackPathDAO dao = new TrackPathDAO();

    private final TrackNameDAO nameDao = new TrackNameDAO();

    @Test
    public void create() throws DaoException {
        System.out.println(dao.save(new TrackPath("path", nameDao.findById(3L).orElse(null))));
    }

    @Test
    public void read() throws DaoException {
        System.out.println(dao.findById(1L));
    }

    @Test
    public void update() throws DaoException {
        System.out.println(dao.update(new TrackPath(1L, "new path", nameDao.findById(4L).orElse(null))));
    }

    @Test
    public void delete() throws DaoException {
        System.out.println(dao.delete(1L));
    }

    @Test
    public void findByTrackId() throws DaoException {
        System.out.println(dao.findByTrackId(6L));
    }
}
