package com.discordchatbot.repository;

import com.discordchatbot.entity.Quote;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class QuotesRepositoryTest {

    @Autowired
    private QuotesRepository quotesRepository;

    @Test
    void findRandom() {
        Quote result = quotesRepository.findRandom();
        System.out.println(result);
    }
}