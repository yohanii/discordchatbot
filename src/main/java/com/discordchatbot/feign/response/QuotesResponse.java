package com.discordchatbot.feign.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class QuotesResponse {

    private List<QuoteResponse> quotes;
}
