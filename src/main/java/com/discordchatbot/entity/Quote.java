package com.discordchatbot.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

@Getter
@Entity
public class Quote {

    @Id @GeneratedValue
    @Column(name = "quote_id")
    private Long id;

    private String content;
}
