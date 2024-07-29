package com.moksem.layers.database.entities;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TrackName {

    private Long id;

    private String trackTitle;

    private String artistName;

    public TrackName(Long id, String trackTitle, String artistName) {
        this.id = id;
        this.trackTitle = trackTitle;
        this.artistName = artistName;
    }

    public TrackName(String trackTitle, String artistName) {
        this.trackTitle = trackTitle;
        this.artistName = artistName;
    }

    @Override
    public String toString() {
        return "TrackName{" +
               "id=" + id +
               ", trackName='" + trackTitle + '\'' +
               ", artistName='" + artistName + '\'' +
               '}';
    }
}
