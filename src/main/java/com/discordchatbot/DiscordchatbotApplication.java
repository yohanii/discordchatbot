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

		JDA jda = createJDA(discordBotTokenComponent, discordListener);

		addSlashCommand(jda);
	}

	private static void addSlashCommand(JDA jda) {

		CommandListUpdateAction commands = jda.updateCommands();

		commands.addCommands(
				Commands.slash("help", "명령어 목록과 사용 방법 출력")
		);

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
				Commands.slash("db-loop", "단위 시간 마다 명언 출력")
						.addOption(INTEGER, "time", "time 단위 : s", true)
		);

		commands.addCommands(
				Commands.slash("db-all", "DB 모든 명언 조회")
		);

		commands.addCommands(
				Commands.slash("db-add", "DB에 명언 추가")
						.addOption(STRING, "author", "저자", true)
						.addOption(STRING, "quote", "명언", true)
		);

		commands.addCommands(
				Commands.slash("db-update", "DB 명언 수정")
						.addOption(INTEGER, "id", "id", true)
						.addOption(STRING, "author", "저자", true)
						.addOption(STRING, "quote", "명언", true)
		);

		commands.addCommands(
				Commands.slash("db-delete", "DB 명언 삭제")
						.addOption(INTEGER, "id", "id", true)
		);

		commands.queue();
	}

	private static JDA createJDA(DiscordBotTokenComponent discordBotTokenComponent, DiscordListener discordListener) {

		return JDABuilder.createDefault(discordBotTokenComponent.getToken())
				.setActivity(Activity.playing("메시지 대기"))
				.enableIntents(GatewayIntent.MESSAGE_CONTENT)
				.addEventListeners(discordListener)
				.build();
	}

}
