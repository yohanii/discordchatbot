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

    public List<QuoteDto> getDBRandomQuotes(int count) {

        List<QuoteDto> result = new ArrayList<>();

        long quotesCount = quotesRepository.count();

        for (int i = 0; i < count; i++) {
            Quote quote = quotesRepository.findById(getRandomNumber(quotesCount))
                    .orElseThrow(() -> new NoSuchElementException("해당하는 명언이 존재하지 않습니다."));
            result.add(new QuoteDto(quote.getAuthor(), quote.getQuote()));
        }

        return result;
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

        Quote quote = quotesRepository.findById(getRandomNumber(count))
                .orElseThrow(() -> new NoSuchElementException("해당하는 명언이 없습니다."));

        return new QuoteDto(quote.getAuthor(), quote.getQuote());
    }

    private long getRandomNumber(long max) {
        return new Random().nextLong(max) + 1;
    }
}
