package validators;

import com.layers.utils.validators.implementation.TrackNameValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TrackNameValidatorTest {

    private final TrackNameValidator validator = new TrackNameValidator();

    @Test
    public void first() {
        Assertions.assertTrue(validator.isValid("Drug Flash - Швы"));
    }

    @Test
    public void second() {
        Assertions.assertFalse(validator.isValid("Drug Flash  - Швы") &&
                               validator.isValid("") &&
                               validator.isValid("Drug Flash  Швы") &&
                               validator.isValid("Drug Flash-Швы"));
    }
}
