package com.discordchatbot.service;

import com.discordchatbot.dto.QuoteDto;
import com.discordchatbot.entity.Quote;
import com.discordchatbot.repository.QuotesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuotesService {

    private final QuotesRepository quotesRepository;

    public List<Quote> getDBRandomQuotes(int count) {

        List<Quote> result = new ArrayList<>();

        long quotesCount = quotesRepository.count();
        log.info("quotesCount : " + quotesCount);


        for (int i = 0; i < count; i++) {
            Quote quote = quotesRepository.findById(getRandomNumber(quotesCount))
                    .orElseThrow(() -> new NoSuchElementException("해당하는 명언이 존재하지 않습니다."));
            result.add(quote);
        }

        return result;
    }

    private static long getRandomNumber(long max) {
        return new Random().nextLong(max);
    }

    public boolean addQuote(String author, String quote) {

        Quote quoteToAdd = Quote.builder()
                .author(author)
                .quote(quote)
                .build();

        Quote saved = quotesRepository.save(quoteToAdd);

        return saved.getId() != null;
    }

    public QuoteDto getDBRandomQuote() {

        long count = quotesRepository.count();

        Quote quote = quotesRepository.findById(new Random().nextLong(count) + 1)
                .orElseThrow(() -> new NoSuchElementException("해당하는 명언이 없습니다."));

        return new QuoteDto(quote.getAuthor(), quote.getQuote());
    }
}
