package database;

import com.telegram.telegrambot.dao.implementations.TrackNameDAO;
import com.telegram.telegrambot.dao.implementations.TrackPathDAO;
import com.telegram.telegrambot.exception.TrackNotFoundException;
import com.telegram.telegrambot.services.implementations.database.DataBase;
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
