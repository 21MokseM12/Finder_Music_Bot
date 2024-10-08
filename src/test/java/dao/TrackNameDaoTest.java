package dao;

import com.layers.database.dao.implementations.TrackNameDAO;
import com.layers.database.entities.TrackName;
import com.layers.exception.DaoException;
import org.junit.jupiter.api.Test;

public class TrackNameDaoTest {

    private final TrackNameDAO dao = new TrackNameDAO();

    @Test
    public void find() throws DaoException {
        System.out.println(dao.findById(3L));
    }

    @Test
    public void save() throws DaoException {
        System.out.println(dao.save(new TrackName("Maksim", "Buynov")));
    }

    @Test
    public void delete() throws DaoException {
        System.out.println(dao.delete(1L));
    }

    @Test
    public void update() throws DaoException {
        System.out.println(dao.update(new TrackName(3L, "Dima", "Egorov")));
    }

    @Test
    public void contains() throws DaoException {
        System.out.println(dao.contains(new TrackName("Dim", "Egorov")));
    }

    @Test
    public void getId() throws DaoException {
        System.out.println(dao.getId(new TrackName("Dim", "Egorov")));
    }
}
