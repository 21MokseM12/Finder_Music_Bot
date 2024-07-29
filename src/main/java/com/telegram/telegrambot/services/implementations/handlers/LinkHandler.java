package com.telegram.telegrambot.services.implementations.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

@Component
@Slf4j
public class LinkHandler {

    public void downloadFileByLink(String link, String path) {
        try (BufferedInputStream inputStream = new BufferedInputStream(new URL(link).openStream());
             FileOutputStream outputStream = new FileOutputStream(path)){
            byte[] data = new byte[4096];
            int count;
            while ((count = inputStream.read(data, 0,4096)) != -1) {
                outputStream.write(data,0,count);
                outputStream.flush();
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
