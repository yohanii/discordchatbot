package com.discordchatbot.feign.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuotesApiResponse<T> {

    private SuccessResponse success;
    private T contents;

}
