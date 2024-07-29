package com.discordchatbot.service;

import com.discordchatbot.dto.QuoteDto;
import com.discordchatbot.entity.Quote;
import com.discordchatbot.feign.QuotesFeignClient;
import com.discordchatbot.repository.QuotesRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;

@SpringBootTest
class QuotesServiceTest {

    @InjectMocks
    QuotesService quotesService;

    @Mock
    QuotesRepository quotesRepository;

    @Mock
    QuotesFeignClient quotesFeignClient;

    @Test
    @DisplayName("명언 리스트의 길이는 count와 같다")
    void getDBRandomQuotes() {
        //Given
        int count = 5;
        Quote quote = Quote.builder()
                .id(0L)
                .author("author")
                .quote("quote")
                .build();

        given(quotesRepository.count()).willReturn(10L);
        given(quotesRepository.findById(anyLong())).willReturn(Optional.of(quote));

        //When
        List<QuoteDto> result = quotesService.getDBRandomQuotes(5);

        //Then
        assertThat(result).hasSize(count);
    }

    @Test
    @DisplayName("랜덤한 quote를 반환한다.")
    void getDBRandomQuote() {
        // Given
        long quoteCount = 5L;
        Quote randomQuote = Quote.builder()
                .author("Test Author")
                .quote("Test Quote")
                .build();

        given(quotesRepository.count()).willReturn(quoteCount);
        given(quotesRepository.findById(anyLong())).willReturn(Optional.of(randomQuote));

        // When
        QuoteDto quoteDto = quotesService.getDBRandomQuote();

        // Then
        then(quotesRepository).should(times(1)).count();
        then(quotesRepository).should(atLeastOnce()).findById(anyLong());

        assertThat(quoteDto).isNotNull();
        assertThat(quoteDto.getAuthor()).isEqualTo("Test Author");
        assertThat(quoteDto.getQuote()).isEqualTo("Test Quote");
    }

    @Test
    @DisplayName("")
    void getAPIQuoteOfTheDay() {
    }

    @Test
    @DisplayName("quotesRepository.findAll()을 리턴한다.")
    void findAll() {
        //Given
        Quote quote1 = Quote.builder().build();
        Quote quote2 = Quote.builder().build();

        given(quotesRepository.findAll()).willReturn(List.of(quote1, quote2));

        //When
        List<Quote> result = quotesService.findAll();

        //Then
        assertThat(result).contains(quote1, quote2);
        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("저장 성공 시 true를 반환한다.")
    void addQuote_success() {
        //Given
        Quote savedQuote = Quote.builder()
                .id(0L)
                .build();

        given(quotesRepository.save(any(Quote.class))).willReturn(savedQuote);

        //When
        boolean result = quotesService.addQuote("author", "quote");

        //Then
        assertTrue(result);
    }

    @Test
    @DisplayName("저장 실패 시 false를 반환한다.")
    void addQuote_fail() {
        //Given
        Quote savedQuote = Quote.builder().build();

        given(quotesRepository.save(any(Quote.class))).willReturn(savedQuote);

        //When
        boolean result = quotesService.addQuote("author", "quote");

        //Then
        assertFalse(result);
    }

    @Test
    @DisplayName("id에 해당하는 quote를 입력받은 author, quote로 업데이트한다")
    void updateQuote_success() {
        //Given
        Quote quote = Quote.builder()
                .id(0L)
                .author("test author")
                .quote("test quote")
                .build();

        given(quotesRepository.findById(0L)).willReturn(Optional.of(quote));

        //When
        quotesService.updateQuote(0, "author", "quote");

        //Then
        assertThat(quote.getAuthor()).isEqualTo("author");
        assertThat(quote.getQuote()).isEqualTo("quote");
    }

    @Test
    @DisplayName("id에 해당하는 quote가 없다면, NoSuchElementException 던진다")
    void updateQuote_fail() {
        //Given
        given(quotesRepository.findById(anyLong())).willReturn(Optional.empty());

        //When
        //Then
        assertThatThrownBy(() -> quotesService.updateQuote(0, "author", "quote"))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("quotesRepository.deleteById를 사용해 id에 해당하는 quote를 제거한다.")
    void deleteQuote() {
        // Given
        long quoteId = 1L;

        // When
        quotesService.deleteQuote((int) quoteId);

        // Then
        then(quotesRepository).should(times(1)).deleteById(quoteId);
    }
}