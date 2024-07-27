package com.telegram.telegrambot.services.interfaces;

import org.jsoup.nodes.Document;

import java.io.IOException;

public interface JsoupRequests {
    Document get(String link) throws IOException;
}
