package com.discordchatbot;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@Slf4j
public class DiscordListener extends ListenerAdapter {

    public static final int MAX_CONTENT_LENGTH = 9;

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

        for (int i = 0; i < count; i++) {
            channel.sendMessage("명언").queue();
        }
    }

    private boolean isPositiveInteger(String content) {
        return content != null && !content.isEmpty() && !content.equals("0") && content.length() < MAX_CONTENT_LENGTH && content.matches("\\d+");
    }
}
