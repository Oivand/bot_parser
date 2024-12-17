package oivand.rainbow_jealousybot.Service.impl;

import oivand.rainbow_jealousybot.Service.RainbowJealousybotService;
import oivand.rainbow_jealousybot.client.CbrClient;
import oivand.rainbow_jealousybot.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.InputSource;
import org.w3c.dom.Document;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;

@Service
public class RainbowJealousyImpl implements RainbowJealousybotService {

    private static final Logger LOG = LoggerFactory.getLogger(RainbowJealousyImpl.class);
    private static final String USD_XPATH = "/ValCurs//Valute[@ID='R01235']/Value";
    private static final String EURO_XPATH = "/ValCurs//Valute[@ID='R01239']/Value";

    @Autowired
    private CbrClient client;

    @Override
    public String getUSD() throws ServiceException {
        var xml = client.getCurrencyRatesXml();
        return extractCurrencyValueFromXML(xml, USD_XPATH);
    }

    @Override
    public String getEURO() throws ServiceException {
        var xml = client.getCurrencyRatesXml();
        return extractCurrencyValueFromXML(xml, EURO_XPATH);
    }

    private static String extractCurrencyValueFromXML(String xml, String xpathExpression) throws ServiceException {
        var source = new InputSource(new StringReader(xml));
        try {
            // Создаем объект XPath
            var xpath = XPathFactory.newInstance().newXPath();

            // Преобразуем XML в документ
            var document = (Document) xpath.evaluate("/", source, XPathConstants.NODE);

            // Извлекаем нужное значение с помощью XPath выражения
            return xpath.evaluate(xpathExpression, document);
        } catch (XPathExpressionException e) {
            LOG.error("Ошибка при обработке XML: ", e);
            throw new ServiceException("Не удалось распарсить XML", e);
        }
    }
}
