package com.discordchatbot;

import com.discordchatbot.dto.QuoteDto;
import com.discordchatbot.entity.Quote;
import com.discordchatbot.service.QuotesService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Timer;
import java.util.TimerTask;

@Slf4j
@Component
@RequiredArgsConstructor
public class DiscordListener extends ListenerAdapter {

    private final QuotesService quotesService;

    private static final String COMMAND_CHANNEL_NAME = "명언채널";

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        log.info("onSlashCommandInteraction : " + event.getName());
        log.info("event.getChannel().getName() : " + event.getChannel().getName());

        if (event.getChannel().getName().equals(COMMAND_CHANNEL_NAME)) {
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
                    dbUpdate(event, updatedId, updatedAuthor, updatedQuote);
                    break;
                case "db-delete":
                    int deletedId = event.getOption("id").getAsInt();
                    dbDelete(event, deletedId);
                    break;
                default:
                    event.reply("이해할 수 없는 명령어 입니다.").queue();
            }
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
                        "- `/db-loop {time}`\n" +
                        "  - time 단위 : s\n" +
                        "  - 단위 시간 마다 명언 출력\n" +
                        "- `/db-all`\n" +
                        "  - DB 모든 명언 조회\n" +
                        "- `/db-add {author} {quote}`\n" +
                        "  - DB에 명언 추가\n" +
                        "- `/db-update {id} {author} {quote}`\n" +
                        "  - DB 명언 수정\n" +
                        "- `/db-delete {id}`\n" +
                        "  - DB 명언 삭제"
        ).queue();
    }

    private void apiToday(SlashCommandInteractionEvent event) {

        try {
            QuoteDto quoteDto = quotesService.getAPIQuoteOfTheDay();
            event.reply(toMessage(quoteDto)).queue();
        } catch (FeignException e) {
            log.error("FeignException", e);
            if (e.status() == 429) {
                event.reply("오늘 볼 수 있는 한계를 넘었습니다! 다른 명령어를 사용해주세요!!").queue();
                return;
            }
            event.reply("문제가 발생했습니다. 다시 시도하거나 다른 명령어를 사용해주세요!").queue();
        } catch (NoSuchElementException | IllegalStateException e) {
            log.error(e.getCause() + e.getMessage());
            event.reply("문제가 발생했습니다. 다시 시도하거나 다른 명령어를 사용해주세요!").queue();
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

        StringBuilder sb = new StringBuilder();
        quotes.stream()
                .map(DiscordListener::toMessage)
                .forEach(s -> sb.append(s).append("\n"));

        event.reply(sb.toString()).queue();
    }

    private void dbAdd(SlashCommandInteractionEvent event, String author, String quote) {

        boolean result = quotesService.addQuote(author, quote);

        if (result) {
            event.reply("성공적으로 추가되었습니다.").queue();
            return;
        }
        event.reply("실패했습니다.").queue();
    }

    private void dbUpdate(SlashCommandInteractionEvent event, int id, String author, String quote) {

        try {
            quotesService.updateQuote(id, author, quote);
            event.reply("성공적으로 수정되었습니다.").queue();
        } catch (NoSuchElementException e) {
            event.reply("입력한 id에 해당하는 명언이 존재하지 않습니다! id를 확인해주세요~").queue();
        }
    }

    private void dbDelete(SlashCommandInteractionEvent event, int id) {

        try {
            quotesService.deleteQuote(id);
            event.reply("성공적으로 삭제되었습니다.").queue();
        } catch (NoSuchElementException e) {
            event.reply("입력한 id에 해당하는 명언이 존재하지 않습니다! id를 확인해주세요~").queue();
        }
    }

    private static @NotNull String toMessage(QuoteDto quoteDto) {
        return "\"" + quoteDto.getQuote() + "\" -" + quoteDto.getAuthor();
    }

    private static @NotNull String toMessage(Quote q) {
        return "id" + q.getId() + ". \"" + q.getQuote() + "\" -" + q.getAuthor();
    }
}
