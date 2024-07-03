package com.discordchatbot;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@Slf4j
public class DiscordListener extends ListenerAdapter {

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

        if (!isPositiveInteger(message.getContentDisplay())) {
            return;
        }

        int count = Integer.parseInt(content);

        for (int i = 0; i < count; i++) {
            channel.sendMessage("명언").queue();
        }

    }

    private boolean isPositiveInteger(String content) {
        return content != null && !content.isEmpty() && !content.equals("0") && content.length() < 9 && content.matches("\\d+");
    }
}
