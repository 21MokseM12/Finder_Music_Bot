package com.moksem.layers.enums;

public enum MessageConstants {

    WELCOME_MESSAGE("Приветствую тебя, друг!"
            .concat("\nЯ могу отправить тебе MP3 файл песни, которую ты хочешь! Для начала работы посмотри")
            .concat("\nменю и выбери, что тебе нравится!")
            .concat("\nСпасибо заранее, что выбрал имеено меня!")),

    TRACK_NOT_FOUND_MESSAGE("Я не смог найти такую песню :/"
            .concat("Попробуйте отправить еще раз, если допустили ошибку в имени и названии или вернитесь к выбору команд")),

    AFTER_REQUEST_MESSAGE("Отправьте имя исполнителя и название трека в формате \"Имя_исполнителя - Название_трека\", если хотите найти еще какой-нибудь трек.\n"
            .concat("Нажмите кнопку \"Вернуться в меню\", чтобы выйти в меню")),

    INVALID_QUERY_MESSAGE("Я немного не понимаю, о чем Вы говорите..."),

    DOWNLOAD_SONG_REQUEST_MESSAGE("Отправьте название трека в формате \"Имя_исполнителя - Название_трека\"!"),

    HELP_REQUEST_MESSAGE("Если бот выдал Вам не тот результат, который Вы ожидали, напишите, пожалуйста, его разработчику в телеграме.\n"
            .concat("Опишите, что не так с ботом, и он постарается это исправить!\n")
            .concat("Разработчик: @mmksm0_o")),

    BACK_TO_MENU_MESSAGE("Выберите команду из меню!"),

    WTF_FACE_MESSAGE("0_____0"),

    HACK_PENTAGON_MEME_MESSAGE("Я ничего не видел ---____---"),

    DOWNLOAD_ALBUM_REQUEST_MESSAGE("В разработке :)");

    private final String message;

    MessageConstants(final String message) {this.message = message;}

    @Override
    public String toString() {return this.message;}
}
