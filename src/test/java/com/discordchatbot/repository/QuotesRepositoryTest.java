package com.discordchatbot.repository;

import com.discordchatbot.entity.Quote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class QuotesRepositoryTest {

    @Autowired
    private QuotesRepository quotesRepository;

    @BeforeEach
    void setUp() {
        Quote quote = new Quote(1L, "test author", "test quote");
        quotesRepository.save(quote);
    }

    @Test
    @DisplayName("한 개의 quote를 반환한다.")
    void findRandom() {

        Quote result = quotesRepository.findRandom();

        assertThat(result).isNotNull();
    }
}