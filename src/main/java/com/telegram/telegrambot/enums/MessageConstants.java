package com.telegram.telegrambot.enums;

public enum MessageConstants {

    WELCOME_MESSAGE("Приветствую тебя, друг!"
            .concat("\nЯ могу отправить тебе MP3 файл песни, которую ты хочешь! Для начала работы посмотри")
            .concat("\nменю и выбери, что тебе нравится!")
            .concat("\nСпасибо заранее, что выбрал имеено меня!")),
    TRACK_NOT_FOUND("Я не смог найти такую песню :/"
            .concat("Попробуйте отправить еще раз, если допустили ошибку в имени и названии или вернитесь к выбору команд")),
    AFTER_REQUEST("Отправьте имя исполнителя и название трека в формате \"Имя_исполнителя - Название_трека\", если хотите найти еще какой-нибудь трек или нажмите кнопку"
            .concat("\"Вернуться в меню\", чтобы выйти в меню")),
    INVALID_QUERY_MESSAGE("Я немного не понимаю, о чем Вы говорите...");

    private final String message;

    MessageConstants(final String message) {this.message = message;}

    @Override
    public String toString() {return this.message;}
}
