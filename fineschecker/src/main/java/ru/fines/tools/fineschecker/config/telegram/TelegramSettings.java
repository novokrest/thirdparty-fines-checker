package ru.fines.tools.fineschecker.config.telegram;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TelegramSettings {

    private final long chatId;
    private final String botToken;

    @JsonCreator
    public TelegramSettings(@JsonProperty("chat-id") long chatId,
                            @JsonProperty("bot-token") String botToken) {
        this.chatId = chatId;
        this.botToken = botToken;
    }

    public long getChatId() {
        return chatId;
    }

    public String getBotToken() {
        return botToken;
    }

}
