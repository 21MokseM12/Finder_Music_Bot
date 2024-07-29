package com.moksem.layers.database.entities;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TrackPath {

    private Long id;

    private String path;

    private TrackName trackName;

    public TrackPath(Long id, String path, TrackName trackName) {
        this.id = id;
        this.path = path;
        this.trackName = trackName;
    }

    public TrackPath(String path, TrackName trackName) {
        this.path = path;
        this.trackName = trackName;
    }

    @Override
    public String toString() {
        return "TrackPath{" +
               "id=" + id +
               ", path='" + path + '\'' +
               ", trackName=" + trackName +
               '}';
    }
}
