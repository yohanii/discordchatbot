package com.discordchatbot.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Quote {

    @Id @GeneratedValue
    @Column(name = "quote_id")
    private Long id;

    private String author;
    private String quote;

    @Builder
    public Quote(Long id, String author, String quote) {
        this.id = id;
        this.author = author;
        this.quote = quote;
    }

    public void updateQuote(String author, String quote) {
        this.author = author;
        this.quote = quote;
    }
}
