package com.discordchatbot.feign.response;

import lombok.Getter;

@Getter
public class QuotesApiResponse<T> {

    private SuccessResponse success;
    private T contents;

}
