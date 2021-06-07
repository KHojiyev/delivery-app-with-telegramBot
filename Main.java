package module3.lesson10_TelegramBot.ExtraTask;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

public class Main {
    public static void main(String[] args) {
        Delivery_Service_DeliveryMan_bot.setDeliveryManList();
        Delivery_Service_Manager_bot.setManagerList();
        DeliveryService_bot.setPizza();
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();

        try {
            telegramBotsApi.registerBot(new DeliveryService_bot());
            telegramBotsApi.registerBot(new Delivery_Service_Manager_bot());
            telegramBotsApi.registerBot(new Delivery_Service_DeliveryMan_bot());

        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }
}
