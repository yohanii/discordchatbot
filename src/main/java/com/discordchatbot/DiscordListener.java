package com.discordchatbot;

import com.discordchatbot.dto.QuoteDto;
import com.discordchatbot.service.QuotesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DiscordListener extends ListenerAdapter {

    private final QuotesService quotesService;

    private static final int MAX_CONTENT_LENGTH = 9;

//    @Override
//    public void onMessageReceived(MessageReceivedEvent event) {
//
//        User user = event.getAuthor();
//        TextChannel channel = event.getChannel().asTextChannel();
//        Message message = event.getMessage();
//        String content = message.getContentDisplay();
//
//        log.info("user: " + user.getName());
//        log.info("user.isBot(): " + user.isBot());
//        log.info("channel: " + channel.getName());
//        log.info("message: " + content);
//
//        if (user.isBot()) {
//            return;
//        }
//
//
//        if (isPositiveInteger(content)) {
//            sendWithCount(content, channel);
//        }
//
//
//    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        log.info("onSlashCommandInteraction : " + event.getName());

        switch (event.getName()) {
            case "api-random":
                event.reply("hi api-random").queue();
                break;
            case "api-count":
                event.reply("hi api-count").queue();
                break;
            case "db-random":
                dbRandom(event);
                break;
            case "db-count":
                int num = event.getOption("num").getAsInt();
                dbCount(event, num);
                break;
            case "db-add":
                String author = event.getOption("author").getAsString();
                String quote = event.getOption("quote").getAsString();
                dbAdd(event, author, quote);
                break;
            default:
                event.reply("이해할 수 없는 명령어 입니다.").queue();
        }
    }

    private void dbCount(SlashCommandInteractionEvent event, int num) {

        List<QuoteDto> quoteDtos = quotesService.getDBRandomQuotes(num);

        StringBuilder sb = new StringBuilder();
        for (QuoteDto quoteDto : quoteDtos) {
            sb.append("\"")
                    .append(quoteDto.getQuote())
                    .append("\" -")
                    .append(quoteDto.getAuthor())
                    .append("\n");
        }
        event.reply(sb.toString()).queue();
    }

    private void dbRandom(SlashCommandInteractionEvent event) {

        QuoteDto quoteDto = quotesService.getDBRandomQuote();

        event.reply("\"" + quoteDto.getQuote() + "\" -" + quoteDto.getAuthor()).queue();
    }

    private void dbAdd(SlashCommandInteractionEvent event, String author, String quote) {

        boolean result = quotesService.addQuote(author, quote);

        if (result) {
            event.reply("성공적으로 추가되었습니다.").queue();
            return;
        }
        event.reply("실패했습니다.").queue();
    }

//    private void sendWithCount(String content, TextChannel channel) {
//
//        int count = Integer.parseInt(content);
//
//        List<Quote> quotes = quotesService.getDBRandomQuotes(count);
//        for (Quote quote: quotes) {
//            channel.sendMessage(quote.getContent()).queue();
//        }
//    }
//
//    private boolean isPositiveInteger(String content) {
//        return content != null && !content.isEmpty() && !content.equals("0") && content.length() < MAX_CONTENT_LENGTH && content.matches("\\d+");
//    }
}
