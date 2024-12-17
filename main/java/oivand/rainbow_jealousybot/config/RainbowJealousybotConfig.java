package oivand.rainbow_jealousybot.config;

import oivand.rainbow_jealousybot.bot.RainbowJealousybot;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class RainbowJealousybotConfig {

    @Bean
    public TelegramBotsApi telegramBotApi(RainbowJealousybot rainbowJealousybot) throws TelegramApiException {
        var api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(rainbowJealousybot);
        return api;
    }

    @Bean
    public OkHttpClient okHttpClient() {
        // Создание нового экземпляра OkHttpClient
        return new OkHttpClient.Builder()
                .connectTimeout(10, java.util.concurrent.TimeUnit.SECONDS) // пример таймаутов
                .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .build();
    }
}

