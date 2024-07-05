package com.discordchatbot.repository;

import com.discordchatbot.entity.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuotesRepository extends JpaRepository<Quote, Long> {
}
