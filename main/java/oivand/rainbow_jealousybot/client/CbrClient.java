package oivand.rainbow_jealousybot.client;

import oivand.rainbow_jealousybot.exception.ServiceException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CbrClient {

    @Autowired
    private OkHttpClient client;

    @Value("${cbr.currency.ratws.xml.url}")
    private String url;

    public String getCurrencyRatesXml() throws ServiceException {
        var request = new Request.Builder().url(url).build();

        try( var response = client.newCall(request).execute();){
            var body = response.body();
            return body == null ? "empty" : body.string();
        } catch (IOException e) {
            throw new ServiceException("Ошибка получения курсов валют.", e);
        }
    }
}
