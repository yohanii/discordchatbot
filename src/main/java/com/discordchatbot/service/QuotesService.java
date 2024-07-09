package com.discordchatbot.service;

import com.discordchatbot.dto.QuoteDto;
import com.discordchatbot.entity.Quote;
import com.discordchatbot.feign.QuotesFeignClient;
import com.discordchatbot.feign.response.QuoteResponse;
import com.discordchatbot.feign.response.QuotesApiResponse;
import com.discordchatbot.feign.response.QuotesResponse;
import com.discordchatbot.repository.QuotesRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuotesService {

    private final QuotesRepository quotesRepository;
    private final QuotesFeignClient quotesFeignClient;

    @Value("${quotes.api-key}")
    private String QUOTES_API_KEY;

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

    public QuoteDto getDBRandomQuote() {

        long count = quotesRepository.count();

        Quote quote = quotesRepository.findById(getRandomNumber(count))
                .orElseThrow(() -> new NoSuchElementException("해당하는 명언이 없습니다."));

        return new QuoteDto(quote.getAuthor(), quote.getQuote());
    }

    private long getRandomNumber(long max) {
        return new Random().nextLong(max) + 1;
    }

    public QuoteDto getAPIQuoteOfTheDay() {

        QuotesApiResponse<QuotesResponse> response = quotesFeignClient.getQuoteOfTheDay(QUOTES_API_KEY);
        List<QuoteResponse> quotes = response.getContents().getQuotes();

        if (quotes.size() == 1) {

            QuoteResponse quote = quotes.getFirst();

            return new QuoteDto(quote.getAuthor(), quote.getQuote());
        }

        return null;
    }

    public List<Quote> findAll() {
        return quotesRepository.findAll();
    }

    @Transactional
    public boolean addQuote(String author, String quote) {

        Quote quoteToAdd = Quote.builder()
                .author(author)
                .quote(quote)
                .build();

        Quote saved = quotesRepository.save(quoteToAdd);

        return saved.getId() != null;
    }

    @Transactional
    public void updateQuote(int id, String author, String quote) {

        Quote findQuote = quotesRepository.findById((long) id)
                .orElseThrow(() -> new NoSuchElementException("해당하는 명언이 존재하지 않습니다."));

        findQuote.updateQuote(author, quote);
    }

    @Transactional
    public void deleteQuote(int id) {
        quotesRepository.deleteById((long) id);
    }
}
