package com.discordchatbot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;

import java.util.Random;

@Slf4j
@Component
@RequiredArgsConstructor
public class DiscordListener extends ListenerAdapter {

    private final QuotesMemoryRepository quotesRepository;

    private static final int MAX_CONTENT_LENGTH = 9;

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        User user = event.getAuthor();
        TextChannel channel = event.getChannel().asTextChannel();
        Message message = event.getMessage();
        String content = message.getContentDisplay();

        log.info("user: " + user.getName());
        log.info("user.isBot(): " + user.isBot());
        log.info("channel: " + channel.getName());
        log.info("message: " + content);

        if (user.isBot()) {
            return;
        }

        sendWithCount(content, channel);

    }

    private void sendWithCount(String content, TextChannel channel) {

        if (!isPositiveInteger(content)) {
            return;
        }

        int count = Integer.parseInt(content);
        int quotesCount = quotesRepository.count();

        for (int i = 0; i < count; i++) {
            channel.sendMessage(quotesRepository.findById(getRandomNumber(quotesCount))).queue();
        }
    }

    private static int getRandomNumber(int max) {
        return new Random().nextInt(max);
    }

    private boolean isPositiveInteger(String content) {
        return content != null && !content.isEmpty() && !content.equals("0") && content.length() < MAX_CONTENT_LENGTH && content.matches("\\d+");
    }
}
