package com.discordchatbot;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;

import static net.dv8tion.jda.api.interactions.commands.OptionType.INTEGER;
import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

@Slf4j
@EnableFeignClients
@SpringBootApplication
public class DiscordchatbotApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(DiscordchatbotApplication.class, args);
		DiscordBotTokenComponent discordBotTokenComponent = context.getBean(DiscordBotTokenComponent.class);
		DiscordListener discordListener = context.getBean(DiscordListener.class);

		createJDA(discordBotTokenComponent, discordListener);
	}

	private static void createJDA(DiscordBotTokenComponent discordBotTokenComponent, DiscordListener discordListener) {

		JDA jda = JDABuilder.createDefault(discordBotTokenComponent.getToken())
				.setActivity(Activity.playing("메시지 대기"))
				.enableIntents(GatewayIntent.MESSAGE_CONTENT)
				.addEventListeners(discordListener)
				.build();

		CommandListUpdateAction commands = jda.updateCommands();

		commands.addCommands(
				Commands.slash("api-today", "Quotes API로 오늘의 명언 출력")
		);

		commands.addCommands(
				Commands.slash("db-random", "DB에서 random한 명언 출력")
		);

		commands.addCommands(
				Commands.slash("db-count", "db에서 num 개수만큼 명언 출력")
						.addOption(INTEGER, "num", "0 < num < 10", true)
		);

		commands.addCommands(
				Commands.slash("db-add", "Quotes API로 num 개수만큼 명언 출력")
						.addOption(STRING, "author", "author", true)
						.addOption(STRING, "quote", "quote", true)
		);


		commands.queue();
	}

}
