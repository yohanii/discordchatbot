package com.discordchatbot;

import com.discordchatbot.dto.QuoteDto;
import com.discordchatbot.entity.Quote;
import com.discordchatbot.service.QuotesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class DiscordListener extends ListenerAdapter {

    private final QuotesService quotesService;

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        log.info("onSlashCommandInteraction : " + event.getName());

        switch (event.getName()) {
            case "help":
                help(event);
                break;
            case "api-today":
                apiToday(event);
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
            case "db-loop":
                int time = event.getOption("time").getAsInt();
                dbLoop(event, time);
                break;
            case "db-all":
                dbAll(event);
                break;
            case "db-add":
                String addedAuthor = event.getOption("author").getAsString();
                String addedQuote = event.getOption("quote").getAsString();
                dbAdd(event, addedAuthor, addedQuote);
                break;
            case "db-update":
                int updatedId = event.getOption("id").getAsInt();
                String updatedAuthor = event.getOption("author").getAsString();
                String updatedQuote = event.getOption("quote").getAsString();
                break;
            case "db-delete":
                int deletedId = event.getOption("id").getAsInt();
                break;
            default:
                event.reply("이해할 수 없는 명령어 입니다.").queue();
        }
    }

    private void help(SlashCommandInteractionEvent event) {
        event.reply(
                "### 명령어\n" +
                        "- `/help`\n" +
                        "  - 명령어 목록과 사용 방법 출력\n" +
                        "- `/api-today`\n" +
                        "  - Quotes API로 오늘의 명언 출력\n" +
                        "- `/db-random`\n" +
                        "  - DB에서 random한 명언 출력\n" +
                        "- `/db-count {num}` \n" +
                        "  - 0 < num < 10\n" +
                        "  - DB에서 num 개수만큼 명언 출력\n" +
                        "- `/db-add {author} {quote}`\n" +
                        "  - DB에 명언 추가"
        ).queue();
    }

    private void apiToday(SlashCommandInteractionEvent event) {

        QuoteDto quoteDto = quotesService.getAPIQuoteOfTheDay();

        event.reply(toMessage(quoteDto)).queue();
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

        event.reply(toMessage(quoteDto)).queue();
    }

    private void dbLoop(SlashCommandInteractionEvent event, int time) {

        event.reply("지금부터 " + time + "초마다 명언을 출력해요!").queue();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                event.getChannel().sendMessage(toMessage(quotesService.getDBRandomQuote())).queue();
            }
        }, 3000L, time * 1000L);
    }

    private void dbAll(SlashCommandInteractionEvent event) {

        List<Quote> quotes = quotesService.findAll();

        String result = quotes.stream()
                .map(DiscordListener::toMessage)
                .collect(Collectors.joining("\n"));

        event.reply(result).queue();
    }

    private void dbAdd(SlashCommandInteractionEvent event, String author, String quote) {

        boolean result = quotesService.addQuote(author, quote);

        if (result) {
            event.reply("성공적으로 추가되었습니다.").queue();
            return;
        }
        event.reply("실패했습니다.").queue();
    }

    private static @NotNull String toMessage(QuoteDto quoteDto) {
        return "\"" + quoteDto.getQuote() + "\" -" + quoteDto.getAuthor();
    }

    private static @NotNull String toMessage(Quote q) {
        return q.getId() + ". \"" + q.getQuote() + "\" -" + q.getAuthor();
    }
}
