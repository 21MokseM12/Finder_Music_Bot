package com.telegram.telegrambot;

import com.telegram.telegrambot.service.TelegramBot;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class TelegrammBotApplicationTests {

    @Test
    void contextLoads() throws IOException {
//        System.out.println(TelegramBot.requestToHitmo("Velvet-a-ha"));
        TelegramBot.downloadByLink("https://rus.hitmotop.com/get/music/20170902/A-Ha_-_Velvet_47960119.mp3");
    }

}
