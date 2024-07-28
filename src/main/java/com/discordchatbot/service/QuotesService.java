package com.discordchatbot.service;

import com.discordchatbot.dto.QuoteDto;
import com.discordchatbot.entity.Quote;
import com.discordchatbot.feign.QuotesFeignClient;
import com.discordchatbot.feign.response.QuoteResponse;
import com.discordchatbot.feign.response.QuotesApiResponse;
import com.discordchatbot.feign.response.QuotesResponse;
import com.discordchatbot.repository.QuotesRepository;
import feign.FeignException;
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

        while (result.size() < count) {
            quotesRepository.findById(getRandomNumber(quotesCount))
                    .ifPresent(value -> result.add(new QuoteDto(value.getAuthor(), value.getQuote())));
        }

        return result;
    }

    public QuoteDto getDBRandomQuote() {

        long count = quotesRepository.count();

        Optional<Quote> quote = Optional.empty();
        while (quote.isEmpty()) {
            quote = quotesRepository.findById(getRandomNumber(count));
        }

        return new QuoteDto(quote.get().getAuthor(), quote.get().getQuote());
    }

    private long getRandomNumber(long max) {
        return new Random().nextLong(max) + 1;
    }

    public QuoteDto getAPIQuoteOfTheDay() throws NoSuchElementException, IllegalStateException, FeignException {

        if (QUOTES_API_KEY.isBlank()) {
            throw new NoSuchElementException("QUOTES_API_KEY가 존재하지 않습니다.");
        }

        QuotesApiResponse<QuotesResponse> response = null;

        response = quotesFeignClient.getQuoteOfTheDay(QUOTES_API_KEY);

        List<QuoteResponse> quotes = response.getContents().getQuotes();

        if (quotes.size() != 1) {
            throw new IllegalStateException("잘못된 API 응답입니다.");
        }

        QuoteResponse quote = quotes.getFirst();
        return new QuoteDto(quote.getAuthor(), quote.getQuote());

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
