package ru.fines.tools.fineschecker.services;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;

import java.util.List;

public class TelegramNotifier {

    private final TelegramBot bot;
    private final long chatId;

    public TelegramNotifier(TelegramBot bot, long chatId) {
        this.bot = bot;
        this.chatId = chatId;
    }

    public void sendMessage(List<String> messages) {
        sendMessage(String.join("\n", messages));
    }

    public void sendMessage(String message) {
        bot.execute(new SendMessage(chatId, message));
    }

}
