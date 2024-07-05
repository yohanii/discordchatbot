package com.discordchatbot;

import com.discordchatbot.entity.Quote;
import com.discordchatbot.service.QuotesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DiscordListener extends ListenerAdapter {

    private final QuotesService quotesService;

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


        if (isPositiveInteger(content)) {
            sendWithCount(content, channel);
        }


    }

    private void sendWithCount(String content, TextChannel channel) {

        int count = Integer.parseInt(content);

        List<Quote> quotes = quotesService.getDBRandomQuotes(count);
        for (Quote quote: quotes) {
            channel.sendMessage(quote.getContent()).queue();
        }
    }

    private boolean isPositiveInteger(String content) {
        return content != null && !content.isEmpty() && !content.equals("0") && content.length() < MAX_CONTENT_LENGTH && content.matches("\\d+");
    }
}
