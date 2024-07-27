package com.telegram.telegrambot.services.implementations.validators;

import com.telegram.telegrambot.services.interfaces.Validator;
import org.springframework.stereotype.Component;

@Component
public class LinkValidator implements Validator {

    @Override
    public boolean isValid(String data) {
        return data.contains(".mp3");
    }
}
