package com.discordchatbot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DiscordBotTokenComponent {

    @Value("${discord.bot.token}")
    private String token;

    public String getToken() {
        log.info("token: " + token);
        return token;
    }
}
