package com.discordchatbot;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@Slf4j
@SpringBootApplication
public class DiscordchatbotApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(DiscordchatbotApplication.class, args);
		DiscordBotTokenComponent bean = context.getBean(DiscordBotTokenComponent.class);

		JDA jda = JDABuilder.createDefault(bean.getToken())
				.setActivity(Activity.playing("메시지 대기"))
				.enableIntents(GatewayIntent.MESSAGE_CONTENT)
				.addEventListeners(new DiscordListener())
				.build();
	}

}
