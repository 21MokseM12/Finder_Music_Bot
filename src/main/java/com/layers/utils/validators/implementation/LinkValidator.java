package com.layers.utils.validators.implementation;

import com.layers.utils.validators.interfaces.Validator;
import org.springframework.stereotype.Component;

@Component
public class LinkValidator implements Validator {

    @Override
    public boolean isValid(String data) {
        return data.contains(".mp3");
    }
}
