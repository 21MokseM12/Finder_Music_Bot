package com.telegram.telegrambot.enums;

public enum Directories {

    MUSIC_FILE_DIRECTORY_NAME("D:/Projects/IdeaProjects/TelegrammBot/src/main/resources/music");

    private String directory;

    Directories(String directory) {this.directory = directory;}

    @Override
    public String toString() {return directory;}
}
