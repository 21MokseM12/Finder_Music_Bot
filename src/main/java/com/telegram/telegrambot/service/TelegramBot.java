package com.telegram.telegrambot.service;

import com.telegram.telegrambot.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {
    final private BotConfig config;
    private boolean albumFlag = false;
    private boolean nameFlag = false;
    private final InlineKeyboardMarkup menu;
    private final InlineKeyboardMarkup backToMenu;

    public TelegramBot(BotConfig config) {
        this.config = config;
        this.menu = new InlineKeyboardMarkup();
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

        this.backToMenu = new InlineKeyboardMarkup();
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
    synchronized public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();
            long chatID = update.getMessage().getChatId();
            int messageID = update.getMessage().getMessageId();

            switch(messageText){
                case "/start@MyDemoFirstBot":
                case "/start":
                    deleteLastMessage(chatID, messageID);
                    welcomeMessage(chatID, menu);
                    break;
                default:
                    //Поиск трека по имени исполнителя и названию трека
                    if (nameFlag && !albumFlag) {
                        deleteLastMessage(chatID, messageID-1);
                        String link = requestToHitmo(messageText);
                        if (Objects.equals("", link)) sendMessage(chatID, "Я не смог найти такую песню :/\n" +
                                "Попробуйте отправить еще раз, если допустили ошибку в имени и названии или вернитесь к выбору команд", backToMenu);
                        else {
                            try {
                                this.execute(new SendAudio(String.valueOf(chatID), downloadByLink(link)));
                            } catch (Exception e) {
                                log.error("Sending audio was failed: " + e.getMessage());
                            }
                            sendMessage(chatID, "Отправьте название трека и имя исполнителя, если хотите найти еще какой-нибудь трек или нажмите кнопку \"Вернуться в меню\", чтобы выйти в меню", backToMenu);
                        }
                    }
                    //Поиск альбома по имени исполнителя и названию альбома
                    else if (!nameFlag && albumFlag) {

                    }
                    else sendMessage(chatID, "Я немного не понимаю, о чем Вы говорите...");
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
                        deleteLastMessage(chatID, messageID);
                        sendMessage(chatID, "Отправьте название трека и имя исполнителя!", backToMenu);
                    }
                    break;
                case "/help@MyDemoFirstBot":
                case "/help":
                    deleteLastMessage(chatID, messageID);
                    sendMessage(chatID, "Если бот выдал Вам не тот результат, который Вы ожидали, напишите, пожалуйста, его разработчику в телеграме, опишите, что не так с ботом, и он постарается это исправить!\n" +
                           "Разработчик: @mmksm0_o" , backToMenu);
                    break;
                case "/back_to_menu@MyDemoFirstBot":
                case "/back_to_menu":
                    deleteLastMessage(chatID, messageID);
                    nameFlag = false;
                    sendMessage(chatID, "Выберите команду из меню!", menu);
                    break;
                case "/hack_pentagon":
                    deleteLastMessage(chatID, messageID);
                    try {
                        sendMessage(chatID, "0_____0");
                        this.execute(new SendVideo(String.valueOf(chatID), new InputFile(new File("/home/moksem/JavaBots/media/watchMe.mp4"))));
                        sendMessage(chatID, "Я ничего не видел ---____---", menu);
                    } catch (TelegramApiException e) {
                        log.error("hacking Pentagon was failed: " + e.getMessage());
                    }
                    break;
                case "/download_album@MyDemoFirstBot":
                case "/download_album":
                    deleteLastMessage(chatID, messageID);
                    sendMessage(chatID, "В разработке :)", backToMenu);
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
        catch(TelegramApiException ex){
            log.error("Error occured: " + ex.getMessage());
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
        catch(TelegramApiException ex){
            log.error("Error occured: " + ex.getMessage());
        }
    }
    private void deleteLastMessage(long chatID, int messageID){
        DeleteMessage deleteMessage = new DeleteMessage(Long.toString(chatID), messageID);

        try{
            execute(deleteMessage);
        } catch (TelegramApiException e){
            System.out.println("Error");
        }
    }
    private void welcomeMessage(long chatID, InlineKeyboardMarkup keyboard){
        final String message = "Приветствую тебя, друг!\nЯ могу отправить тебе MP3 файл песни, которую ты хочешь! Для начала работы посмотри " +
                "меню и выбери, что тебе нравится!\nСпасибо заранее, что выбрал имеено меня!";

        sendMessage(chatID, message, keyboard);
    }
    synchronized public static String requestToHitmo(String name) {
        try {
            var document = Jsoup.connect("https://rus.hitmotop.com/search?q=" + name).get();
            var body = document.select("a");
            for (var element : body) {
                if (isValidLinkFromHitmo(element.attr("href"))) return element.attr("href");
            }
        } catch (Exception e) {
            log.error("request is failed: " + e.getMessage());
        }
        return "";
    }
    public static boolean isValidLinkFromHitmo(String link) {
        return link.contains(".mp3");
    }
    synchronized public static InputFile downloadByLink(String link) {
        try (BufferedInputStream in = new BufferedInputStream(new URL(link).openStream());
                FileOutputStream out = new FileOutputStream("/home/moksem/JavaBots/media/track.mp3")){
            byte[] data = new byte[4096];
            int count;
            while ((count = in.read(data, 0,4096)) != -1) {
                out.write(data,0,count);
                out.flush();
            }
        } catch (MalformedURLException e) {
            log.error("URL not exist: " + e.getMessage());
        } catch (IOException e) {
            log.error("Stream open was failed: " + e.getMessage());
        }
        File file = new File("/home/moksem/JavaBots/media/track.mp3");
        InputFile inputFile = new InputFile(file);
        return inputFile;
    }
}