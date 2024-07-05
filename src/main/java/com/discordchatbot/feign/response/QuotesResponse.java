package com.discordchatbot.feign.response;

import lombok.Getter;

import java.util.List;

@Getter
public class QuotesResponse {

    private List<QuoteResponse> quotes;
}
