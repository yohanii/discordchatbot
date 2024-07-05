package com.discordchatbot.repository;

import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class QuotesMemoryRepository {

    private final Map<Integer, String> quotes = Map.of(
            0, "삶이 있는 한 희망은 있다 -키케로",
            1, "산다는것 그것은 치열한 전투이다. -로망로랑",
            2, "하루에 3시간을 걸으면 7년 후에 지구를 한바퀴 돌 수 있다. -사무엘존슨",
            3, "언제나 현재에 집중할수 있다면 행복할것이다. -파울로 코엘료",
            4, "진정으로 웃으려면 고통을 참아야하며 , 나아가 고통을 즐길 줄 알아야 해 -찰리 채플린"
    );

    public String findById(int id) {
        return quotes.get(id);
    }

    public int count() {
        return quotes.size();
    }
}
