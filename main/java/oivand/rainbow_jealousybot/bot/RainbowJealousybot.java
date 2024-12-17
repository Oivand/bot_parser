package oivand.rainbow_jealousybot.bot;

import oivand.rainbow_jealousybot.Service.RainbowJealousybotService;
import oivand.rainbow_jealousybot.exception.ServiceException;
import okhttp3.internal.connection.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;

@Component
public class RainbowJealousybot extends TelegramLongPollingBot implements RainbowJealousybotInterface {

    private static final Logger LOG = LoggerFactory.getLogger(RainbowJealousybot.class);
    private static final String START = "/start";
    private static final String USD = "/usd";
    private static final String EURO = "/euro";
    private static final String HELP = "/help";

    @Autowired
    private RainbowJealousybotService exchangeRatesService;

    public RainbowJealousybot(@Value("${bot.token}") String botToken){
        super(botToken);
    }


    @Override
    public void onUpdateReceived(Update update){
        if (!update.hasMessage() || !update.getMessage().hasText()){
            return;
        }
        var message = update.getMessage().getText();
        var chatID = update.getMessage().getChatId();
        switch (message){
            case START -> {
                String userName = update.getMessage().getChat().getUserName();
                stratCommand(chatID, userName);
            }
            case USD -> usdCommand(chatID);
            case EURO -> euroCommand(chatID);
            case HELP -> helpCommand(chatID);
            default -> unknownCommand(chatID);
        }
    }

    @Override
    public String getBotUsername() {
        return "rainbow_jealousybot";
    }

    private void stratCommand(Long chatId, String userName){
        var text = """
                Добро пожаловать в бот, %s!
                
                Здесь Вы сможете узнать официальные курсы валют на сегодня, установленные ЦБ РФ.
                
                Для этого воспользуйтесь командами:
                /usd - курс доллара
                /eur - курс евро
                
                Дополнительные команды:
                /help - получение справки
                """;
        var formattedText = String.format(text, userName);
        sendMessage(chatId, formattedText);

    }

    private void usdCommand(Long chatId){
        String formattedText;
        try {
            var usd = exchangeRatesService.getUSD();
            var text = "Курс доллара на %s составляет %s рублей";
            formattedText = String.format(text, LocalDate.now(), usd);
        } catch (ServiceException e) {
            LOG.error("Ошибка получения курса доллара", e);
            formattedText = "Не удалось получить текущий курс доллара. Попробуйте позже.";
        }
        sendMessage(chatId, formattedText);
    }
    private void euroCommand(Long chatId){
        String formattedText;
        try {
            var usd = exchangeRatesService.getEURO();
            var text = "Курс евро на %s составляет %s рублей";
            formattedText = String.format(text, LocalDate.now(), usd);
        } catch (ServiceException e) {
            LOG.error("Ошибка получения курса евро", e);
            formattedText = "Не удалось получить текущий курс евро. Попробуйте позже.";
        }
        sendMessage(chatId, formattedText);
    }
    private void helpCommand(Long chatId) {
        var text = """
                Справочная информация по боту
                
                Для получения текущих курсов валют воспользуйтесь командами:
                /usd - курс доллара
                /eur - курс евро
                """;
        sendMessage(chatId, text);
    }

    private void unknownCommand(Long chatId) {
        var text = "Не удалось распознать команду!";
        sendMessage(chatId, text);
    }

    private void sendMessage(Long chatId, String text){
        var chatIdStr = String.valueOf(chatId);
        var sendMessage = new SendMessage(chatIdStr, text);
        try{
            execute(sendMessage);
        } catch (TelegramApiException e){
            LOG.error("cannot send message",
                    e);
        }
    }

    @Override
    public String getBotUserName() {
        return "";
    }
}
