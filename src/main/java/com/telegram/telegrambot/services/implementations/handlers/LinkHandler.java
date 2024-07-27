package com.telegram.telegrambot.services.implementations.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

@Component
@Slf4j
public class LinkHandler {

    public InputFile downloadFileByLink(String link, String directoryToStore, String fileName) {
        try (BufferedInputStream inputStream = new BufferedInputStream(new URL(link).openStream());
             FileOutputStream outputStream = new FileOutputStream(directoryToStore.concat("/").concat(fileName))){
            byte[] data = new byte[4096];
            int count;

            while ((count = inputStream.read(data, 0,4096)) != -1) {
                outputStream.write(data,0,count);
                outputStream.flush();
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return new InputFile(new File(directoryToStore.concat("/").concat(fileName)));
    }
}
