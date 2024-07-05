package com.discordchatbot.feign;

import com.discordchatbot.feign.response.QuotesApiResponse;
import com.discordchatbot.feign.response.QuotesResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "quotes", url = "https://quotes.rest")
public interface QuotesFeignClient {

    @GetMapping("/qod")
    QuotesApiResponse<QuotesResponse> getQuoteOfTheDay(@RequestParam String api_key);
}
