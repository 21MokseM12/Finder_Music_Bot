package com.moksem.layers.utils.validators.implementation;

import com.moksem.layers.utils.validators.interfaces.Validator;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class TrackNameValidator implements Validator {

    @Override
    public boolean isValid(String data) {
        List<String> trackDefinition = Arrays.stream(data.trim().split(" - ")).toList();
        trackDefinition = trackDefinition.stream().map(String::trim).toList();

        return trackDefinition.size() == 2;
    }
}
