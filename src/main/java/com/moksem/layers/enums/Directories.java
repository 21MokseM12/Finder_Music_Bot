package com.moksem.layers.enums;

public enum Directories {

    MUSIC_FILE_DIRECTORY_NAME("./media/music"),

    VIDEO_FILE_DIRECTORY_NAME("./media/video");

    private final String directory;

    Directories(String directory) {this.directory = directory;}

    @Override
    public String toString() {return directory;}
}
