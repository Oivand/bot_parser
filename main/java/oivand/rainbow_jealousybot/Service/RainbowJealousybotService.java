package oivand.rainbow_jealousybot.Service;

import oivand.rainbow_jealousybot.exception.ServiceException;

public interface RainbowJealousybotService {

    String getUSD() throws ServiceException;

    String getEURO() throws ServiceException;
}
