package com.telegram.telegrambot.exception;

public class TrackNotFoundException extends Throwable{
    public TrackNotFoundException() {super();}
    public TrackNotFoundException(String message) {super(message);}
    public TrackNotFoundException(Throwable cause) {super(cause);}
    public TrackNotFoundException(String message, Throwable cause) {super(message, cause);}
}
