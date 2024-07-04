package com.discordchatbot;

import com.discordchatbot.entity.Quote;
import com.discordchatbot.repository.QuotesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.Random;

@Slf4j
@Component
@RequiredArgsConstructor
public class DiscordListener extends ListenerAdapter {

    private final QuotesRepository quotesRepository;

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
        long quotesCount = quotesRepository.count();

        for (int i = 0; i < count; i++) {
            Quote quote = quotesRepository.findById(getRandomNumber(quotesCount))
                    .orElseThrow(() -> new NoSuchElementException("해당하는 명언이 존재하지 않습니다."));
            channel.sendMessage(quote.getContent()).queue();
        }
    }

    private static long getRandomNumber(long max) {
        return new Random().nextLong(max);
    }

    private boolean isPositiveInteger(String content) {
        return content != null && !content.isEmpty() && !content.equals("0") && content.length() < MAX_CONTENT_LENGTH && content.matches("\\d+");
    }
}
