package com.layers.http.service.implementation;

import com.layers.http.service.interfaces.JsoupRequests;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JsoupHandler implements JsoupRequests {

    @Override
    public Document get(String link) throws IOException {
        return Jsoup.connect(link).get();
    }
}
