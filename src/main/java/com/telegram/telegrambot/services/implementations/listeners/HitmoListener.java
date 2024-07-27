package com.telegram.telegrambot.services.implementations.listeners;

import com.telegram.telegrambot.exception.TrackNotFoundException;
import com.telegram.telegrambot.services.implementations.handlers.JsoupHandler;
import com.telegram.telegrambot.services.implementations.parsers.Parser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class HitmoListener {

    private static final String HITMO_ADDRESS = "https://rus.hitmotop.com/search?q=";

    private final JsoupHandler jsoupHandler;

    private final Parser parser;

    @Autowired
    public HitmoListener(JsoupHandler jsoupHandler, Parser parser) {
        this.jsoupHandler = jsoupHandler;
        this.parser = parser;
    }

    public String requestToHitmo(String name) throws TrackNotFoundException {
        try {
            return parser.parseMP3Link(jsoupHandler.get(HITMO_ADDRESS + name));
        } catch (IOException | TrackNotFoundException e) {
            log.error(e.getMessage());
            throw new TrackNotFoundException(e);
        }
    }
}
