package com.discordchatbot.feign.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class QuoteResponse {

    private String quote;
    private String author;
}
