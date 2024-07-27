package validators;

import com.telegram.telegrambot.services.implementations.validators.LinkValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LinkValidatorTest {

    private final LinkValidator validator = new LinkValidator();

    @Test
    public void first() {
        Assertions.assertTrue(validator.isValid("https://www.google.com/music/track.mp3"));
    }

    @Test
    public void second() {
        Assertions.assertFalse(validator.isValid("") && validator.isValid("mp4") && validator.isValid("mp3"));
    }
}
