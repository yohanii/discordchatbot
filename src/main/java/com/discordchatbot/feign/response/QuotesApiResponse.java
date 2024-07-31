package com.discordchatbot.feign.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class QuotesApiResponse<T> {

    private SuccessResponse success;
    private T contents;

}
