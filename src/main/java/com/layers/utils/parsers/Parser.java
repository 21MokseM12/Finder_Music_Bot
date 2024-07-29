package com.layers.utils.parsers;

import com.layers.exception.TrackNotFoundException;
import com.layers.utils.validators.implementation.LinkValidator;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Parser {

    private final LinkValidator linkValidator;



    @Autowired
    public Parser(LinkValidator validator) {this.linkValidator = validator;}

    public String parseMP3Link(Document document) throws TrackNotFoundException{
        Elements body = document.select("a");
        for (var element : body)
            if (linkValidator.isValid(element.attr("href")))
                return element.attr("href");

        throw  new TrackNotFoundException();
    }

    public String parseArtistName(String data) {
        String[] splitData = data.split(" - ");
        return splitData[0];
    }

    public String parseTrackName(String data) {
        String[] splitData = data.split(" - ");
        return splitData[1];
    }
}
