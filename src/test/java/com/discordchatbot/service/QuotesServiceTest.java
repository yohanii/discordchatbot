package com.discordchatbot.service;

import com.discordchatbot.dto.QuoteDto;
import com.discordchatbot.entity.Quote;
import com.discordchatbot.feign.QuotesFeignClient;
import com.discordchatbot.feign.response.QuoteResponse;
import com.discordchatbot.feign.response.QuotesApiResponse;
import com.discordchatbot.feign.response.QuotesResponse;
import com.discordchatbot.repository.QuotesRepository;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class QuotesServiceTest {

    QuotesService quotesService;

    @Mock
    QuotesRepository quotesRepository;

    @Mock
    QuotesFeignClient quotesFeignClient;

    private static final String VALID_QUOTES_API_KEY = "valid_api_key";
    private static final String INVALID_QUOTES_API_KEY = "";

    @BeforeEach
    void setUp() {
        quotesService = new QuotesService(quotesRepository, quotesFeignClient, VALID_QUOTES_API_KEY);
    }

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

        given(quotesRepository.findRandom()).willReturn(quote);

        //When
        List<QuoteDto> result = quotesService.getDBRandomQuotes(count);

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

        given(quotesRepository.findRandom()).willReturn(randomQuote);

        // When
        QuoteDto quoteDto = quotesService.getDBRandomQuote();

        // Then
        assertThat(quoteDto).isNotNull();
        assertThat(quoteDto.getAuthor()).isEqualTo("Test Author");
        assertThat(quoteDto.getQuote()).isEqualTo("Test Quote");
    }

    @Test
    @DisplayName("getQuoteOfTheDay에서 FeignException 날 경우, 그대로 던진다.")
    void getAPIQuoteOfTheDay_FeignException() {

        //Given
        given(quotesFeignClient.getQuoteOfTheDay(anyString())).willThrow(FeignException.class);

        //When
        //Then
        assertThatThrownBy(() -> quotesService.getAPIQuoteOfTheDay())
                .isInstanceOf(FeignException.class);
    }

    @Test
    @DisplayName("API key가 blank이면, NoSuchElementException을 던진다.")
    void getAPIQuoteOfTheDay_INVALID_QUOTES_API_KEY() {

        //Given
        quotesService = new QuotesService(quotesRepository, quotesFeignClient, INVALID_QUOTES_API_KEY);

        //When
        //Then
        assertThatThrownBy(() -> quotesService.getAPIQuoteOfTheDay())
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("response의 quotes 개수가 1이 아니면, IllegalStateException을 던진다.")
    void getAPIQuoteOfTheDay_quotes_count_not_one() {

        //Given
        QuoteResponse quote1 = new QuoteResponse("author1", "quote1");
        QuoteResponse quote2 = new QuoteResponse("author2", "quote2");
        QuotesApiResponse<QuotesResponse> apiResponse = new QuotesApiResponse<>(null, new QuotesResponse(List.of(quote1, quote2)));

        given(quotesFeignClient.getQuoteOfTheDay(VALID_QUOTES_API_KEY)).willReturn(apiResponse);

        //When
        //Then
        assertThatThrownBy(() -> quotesService.getAPIQuoteOfTheDay())
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("api의 응답을 QuoteDto로 반환한다.")
    void getAPIQuoteOfTheDa() {

        //Given
        QuoteResponse quote = new QuoteResponse("quote1", "author1");
        QuotesApiResponse<QuotesResponse> apiResponse = new QuotesApiResponse<>(null, new QuotesResponse(List.of(quote)));

        given(quotesFeignClient.getQuoteOfTheDay(VALID_QUOTES_API_KEY)).willReturn(apiResponse);

        //When
        QuoteDto result = quotesService.getAPIQuoteOfTheDay();

        //Then
        assertThat(result).isNotNull();
        assertThat(result.getAuthor()).isEqualTo("author1");
        assertThat(result.getQuote()).isEqualTo("quote1");
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