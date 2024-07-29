package com.telegram.telegrambot.services;

import com.telegram.telegrambot.config.BotConfig;
import com.telegram.telegrambot.enums.Directories;
import com.telegram.telegrambot.enums.MessageConstants;
import com.telegram.telegrambot.exception.TrackNotFoundException;
import com.telegram.telegrambot.services.implementations.listeners.HitmoListener;
import com.telegram.telegrambot.services.implementations.handlers.LinkHandler;
import com.telegram.telegrambot.services.implementations.validators.TrackNameValidator;
import com.telegram.telegrambot.services.implementations.parsers.Parser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.*;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    final private BotConfig config;

    private boolean albumFlag = false;

    private boolean nameFlag = false;

    private final InlineKeyboardMarkup menu;

    private static InlineKeyboardMarkup backToMenu;

    private final HitmoListener hitmoListener;

    private final LinkHandler linkHandler;

    private final TrackNameValidator trackNameValidator;

    private final Parser parser;

    @Autowired
    public TelegramBot(BotConfig config, HitmoListener hitmoListener, LinkHandler linkHandler, TrackNameValidator trackNameValidator, Parser parser) {
        this.config = config;
        this.menu = new InlineKeyboardMarkup();
        this.hitmoListener = hitmoListener;
        this.linkHandler = linkHandler;
        this.trackNameValidator = trackNameValidator;
        this.parser = parser;
        loadMenuButtons();
    }

    private void loadMenuButtons() {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> firstRow = new ArrayList<>();
        InlineKeyboardButton song = new InlineKeyboardButton();
        song.setText("Скачать песню");
        song.setCallbackData("/download_song");
        firstRow.add(song);

        List<InlineKeyboardButton> secondRow = new ArrayList<>();
        InlineKeyboardButton help = new InlineKeyboardButton();
        help.setText("Помощь");
        help.setCallbackData("/help");
        InlineKeyboardButton pentagon = new InlineKeyboardButton();
        pentagon.setText("Взломать Пентагон!");
        pentagon.setCallbackData("/hack_pentagon");
        secondRow.add(pentagon);
        secondRow.add(help);

        rows.add(firstRow);
        rows.add(secondRow);

        menu.setKeyboard(rows);

        backToMenu = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> row = new ArrayList<>();

        List<InlineKeyboardButton> onceRow = new ArrayList<>();
        InlineKeyboardButton back = new InlineKeyboardButton();
        back.setText("Вернуться в меню");
        back.setCallbackData("/back_to_menu");
        onceRow.add(back);
        row.add(onceRow);
        backToMenu.setKeyboard(row);
    }

    @Override
    public String getBotUsername() {
        return this.config.getBotName();
    }

    @Override
    public String getBotToken() {
        return this.config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();
            long chatID = update.getMessage().getChatId();
            int messageID = update.getMessage().getMessageId();

            switch(messageText){
                case "/start@MyDemoFirstBot":
                case "/start":
                    deleteMessage(chatID, messageID);
                    sendMessage(chatID, MessageConstants.WELCOME_MESSAGE.toString(), menu);
                    break;
                default:
                    //Поиск трека по имени исполнителя и названию трека
                    if (nameFlag && !albumFlag) {
                        deleteMessage(chatID, messageID - 1);
                        try {
                            String trackName, artistName;
                            if (!trackNameValidator.isValid(messageText)) throw new TrackNotFoundException();
                            else {
                                trackName = parser.parseTrackName(messageText);
                                artistName = parser.parseArtistName(messageText);



                                String link = hitmoListener.requestToHitmo(messageText);

                                this.execute(new SendAudio(String.valueOf(chatID),linkHandler.downloadFileByLink(link,
                                        Directories.MUSIC_FILE_DIRECTORY_NAME.toString(),
                                        trackName.concat("-").concat(artistName))));



                                sendMessage(chatID, MessageConstants.AFTER_REQUEST_MESSAGE.toString(), backToMenu);
                            }
                        } catch (TrackNotFoundException e) {
                            sendMessage(chatID, MessageConstants.TRACK_NOT_FOUND_MESSAGE.toString(), backToMenu);
                        } catch (TelegramApiException e) {
                            log.error(e.getMessage());
                        }
                    }
                    //Поиск альбома по имени исполнителя и названию альбома
                    else if (!nameFlag && albumFlag) {

                    }
                    else sendMessage(chatID, MessageConstants.INVALID_QUERY_MESSAGE.toString());
                    break;
            }
        }
        else if (update.hasCallbackQuery()){
            String messageText = update.getCallbackQuery().getData();
            long chatID = update.getCallbackQuery().getMessage().getChatId();
            int messageID = update.getCallbackQuery().getMessage().getMessageId();

            switch(messageText){
                case "/download_song@MyDemoFirstBot":
                case "/download_song":
                    if (!nameFlag) {
                        nameFlag = true;
                        deleteMessage(chatID, messageID);
                        sendMessage(chatID, MessageConstants.DOWNLOAD_SONG_REQUEST_MESSAGE.toString(), backToMenu);
                    }
                    break;
                case "/help@MyDemoFirstBot":
                case "/help":
                    deleteMessage(chatID, messageID);
                    sendMessage(chatID,  MessageConstants.HELP_REQUEST_MESSAGE.toString(), backToMenu);
                    break;
                case "/back_to_menu@MyDemoFirstBot":
                case "/back_to_menu":
                    deleteMessage(chatID, messageID);
                    nameFlag = false;
                    sendMessage(chatID, MessageConstants.BACK_TO_MENU_MESSAGE.toString(), menu);
                    break;
                case "/hack_pentagon":
                    deleteMessage(chatID, messageID);
                    try {
                        sendMessage(chatID, MessageConstants.WTF_FACE_MESSAGE.toString());
                        this.execute(new SendVideo(String.valueOf(chatID), new InputFile(new File(Directories.VIDEO_FILE_DIRECTORY_NAME + "/watchMe.mp4"))));
                        sendMessage(chatID, MessageConstants.HACK_PENTAGON_MEME_MESSAGE.toString(), menu);
                    } catch (TelegramApiException e) {
                        log.error(e.getMessage());
                    }
                    break;
                case "/download_album@MyDemoFirstBot":
                case "/download_album":
                    deleteMessage(chatID, messageID);
                    sendMessage(chatID, MessageConstants.DOWNLOAD_ALBUM_REQUEST_MESSAGE.toString(), backToMenu);
                    break;
            }
        }
    }

    private void sendMessage(long chatID, String textToSend){
        SendMessage message = new SendMessage();

        message.setChatId(String.valueOf(chatID));
        message.setText(textToSend);

        try{
            execute(message);
        }
        catch(TelegramApiException e){
            log.error(e.getMessage());
        }
    }

    private void sendMessage(long chatID, String textToSend, InlineKeyboardMarkup keyboard){
        SendMessage message = new SendMessage();

        message.setChatId(String.valueOf(chatID));
        message.setText(textToSend);
        message.setReplyMarkup(keyboard);

        try{
            execute(message);
        }
        catch(TelegramApiException e){
            log.error(e.getMessage());
        }
    }

    private void deleteMessage(long chatID, int messageID){
        DeleteMessage deleteMessage = new DeleteMessage(Long.toString(chatID), messageID);

        try{
            execute(deleteMessage);
        } catch (TelegramApiException e){
            log.error(e.getMessage());
        }
    }
}