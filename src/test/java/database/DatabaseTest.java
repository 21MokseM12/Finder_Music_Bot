package database;

import com.layers.database.dao.implementations.TrackNameDAO;
import com.layers.database.dao.implementations.TrackPathDAO;
import com.layers.exception.TrackNotFoundException;
import com.layers.database.service.DataBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DatabaseTest {

    private final DataBase database = new DataBase(new TrackNameDAO(), new TrackPathDAO());

    @Test
    public void save() throws TrackNotFoundException {
        database.saveTrack("Патрон", "Мияги", "moksem/miyagi/track.mp3");
    }

    @Test
    public void contains() throws TrackNotFoundException {
        Assertions.assertTrue(database.containsTrack("Патрон", "Мияги"));
    }

    @Test
    public void getPath() throws TrackNotFoundException {
        System.out.println(database.getTrackPath("Патрон", "Мияги"));
    }
}
