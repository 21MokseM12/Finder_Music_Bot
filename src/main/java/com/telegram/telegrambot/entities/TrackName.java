package com.telegram.telegrambot.entities;

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

    public TrackName() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTrackTitle() {
        return trackTitle;
    }

    public void setTrackTitle(String trackTitle) {
        this.trackTitle = trackTitle;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
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
