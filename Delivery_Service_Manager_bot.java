package module3.lesson10_TelegramBot.ExtraTask;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Delivery_Service_Manager_bot extends TelegramLongPollingBot {
    public static ConcurrentHashMap<Long, Integer> loginSteps = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<Long, String> managerLogin = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<Long, String> managerPassword = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<Long, OrderedPizza> orderedPizza = new ConcurrentHashMap<>();
    public static List<Manager> managerList = new ArrayList<>();
    public static String activeLanguage = "uz";
    public static Long orderNumber = 1L;
    public static Long index = 0L;


    @Override
    public void onUpdateReceived(Update update) {
        SendMessage sendMessage = new SendMessage();
        if (update.hasMessage()) {
            switch (update.getMessage().getText()) {
                case "/start":
                    sendMessage.setText("Assalomu aleykum");
                    tryCatch(sendMessage, update);
                    sendMessage.setText("Login:");
                    tryCatch(sendMessage, update);
                    loginSteps.put(update.getMessage().getChatId(), 1);
                    break;
                case "Mening buyurtmalarim":
                    for (Map.Entry<Long, OrderedPizza> pizzaEntry : orderedPizza.entrySet()) {
                        if (pizzaEntry.getValue().getManagerChatId().equals(update.getMessage().getChatId().toString())) {
                            if (pizzaEntry.getValue().getStatus().equals(Status.RECEIVED.getUz())) {
                                try {
                                    execute(setInlineSendToKitchen(update.getMessage().getChatId(), pizzaEntry.getValue().getPizzaType() + " " +
                                            pizzaEntry.getValue().getCount() + " " +
                                            pizzaEntry.getValue().getStatus()));
                                } catch (TelegramApiException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    for (Map.Entry<Long, OrderedPizza> pizzaEntry : orderedPizza.entrySet()) {
                        if (pizzaEntry.getValue().getManagerChatId().equals(update.getMessage().getChatId().toString())) {

                            if (pizzaEntry.getValue().getStatus().equals(Status.COOKING.getUz())) {
                                try {
                                    execute(setInlineSendToDelivery(update.getMessage().getChatId(), pizzaEntry.getValue().getPizzaType() + " " +
                                            pizzaEntry.getValue().getCount() + " " +
                                            pizzaEntry.getValue().getStatus()));
                                } catch (TelegramApiException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    break;
                case SelectLanguage.language_uz:
                    activeLanguage = "uz";
                    sendMessage.setText("👍");
                    myOrdersAndLanguage(sendMessage);
                    tryCatch(sendMessage, update);
                    break;
                case SelectLanguage.language_ru:
                    activeLanguage = "ru";
                    sendMessage.setText("👍");
                    myOrdersAndLanguage(sendMessage);
                    tryCatch(sendMessage, update);
                    break;
                case SelectLanguage.language_en:
                    activeLanguage = "en";
                    sendMessage.setText("👍");
                    myOrdersAndLanguage(sendMessage);
                    tryCatch(sendMessage, update);
                    break;
                default:
                    // finish login
                    if (loginSteps.get(update.getMessage().getChatId()) == 1) {
                        managerLogin.put(update.getMessage().getChatId(), update.getMessage().getText());
                        sendMessage.setText("parol:");
                        tryCatch(sendMessage, update);
                        loginSteps.put(update.getMessage().getChatId(), 2);
                    } else if (loginSteps.get(update.getMessage().getChatId()) == 2) {
                        managerPassword.put(update.getMessage().getChatId(), update.getMessage().getText());
                        for (Manager manager : managerList) {
                            if (manager.getLogin().equals(managerLogin.get(update.getMessage().getChatId())) &&
                                    manager.getPassword().equals(managerPassword.get(update.getMessage().getChatId()))) {
                                System.out.println("manager founded");
                                myOrdersAndLanguage(sendMessage);
                                loginSteps.put(update.getMessage().getChatId(), 3);
                                sendMessage.setText("Hush kelibsiz " + update.getMessage().getChat().getFirstName());
                                tryCatch(sendMessage, update);
                            }
                        }
                    }
                    break;
            }
        }
        else if (update.hasCallbackQuery()) {
            DeliveryService_bot deliveryService_bot = new DeliveryService_bot();
            Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            String data = update.getCallbackQuery().getData();
            String managerUserName = "@" + update.getCallbackQuery().getMessage().getChat().getUserName();
            String text = update.getCallbackQuery().getMessage().getText();
            String pizzaCount = update.getCallbackQuery().getMessage().getText().substring(text.indexOf("Count:"));
            String pizzaName = update.getCallbackQuery().getMessage().getText().substring(text.lastIndexOf("Name:"), text.indexOf("//")).trim();
            switch (activeLanguage) {
                case "uz":
                    EditMessageText editMessageText;

                    if (data.contains("qabulQilish")) {
                        Long indexx = Long.parseLong(data.substring(data.indexOf("&") + 1));
                        orderedPizza.get(indexx).setStatus(Status.RECEIVED.getUz());
                        orderedPizza.get(indexx).setManagerChatId(chatId.toString());
                        Long customerChatId = orderedPizza.get(indexx).getUser().getUserChatId();
                        editMessageText = new EditMessageText().setText("Buyurtma qabul qilindi").setChatId(chatId).setMessageId(messageId);
                        tryCatchForInline(editMessageText);
                        switch (DeliveryService_bot.activeLanguage){
                            case "uz":
                                sendMessage.setText("Buyurtmangiz qabul qilindi: " + pizzaName + " " + pizzaCount + "\n" + "Manager: " + managerUserName);
                                try {
                                    sendMessage.setChatId(customerChatId);
                                    deliveryService_bot.execute(sendMessage);
                                } catch (TelegramApiException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case "ru":
                                sendMessage.setText("ваш заказ был принят: " + pizzaName + " " + pizzaCount + "\n" + "Manager: " + managerUserName);
                                try {
                                    sendMessage.setChatId(customerChatId);
                                    deliveryService_bot.execute(sendMessage);
                                } catch (TelegramApiException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case "en":
                                sendMessage.setText("Your order has been accepted: " + pizzaName + " " + pizzaCount + "\n" + "Manager: " + managerUserName);
                                try {
                                    sendMessage.setChatId(customerChatId);
                                    deliveryService_bot.execute(sendMessage);
                                } catch (TelegramApiException e) {
                                    e.printStackTrace();
                                }
                                break;
                        }

                    }

                    else if (data.contains("oshxonagaJo'natish")) {

                        for (Map.Entry<Long, OrderedPizza> pizzaEntry : orderedPizza.entrySet()) {
                            if (pizzaEntry.getValue().getManagerChatId().equals(chatId.toString())) {
                                if (text.contains(pizzaEntry.getValue().getPizzaType())) {
                                    if (text.contains(pizzaEntry.getValue().getCount())) {
                                        pizzaEntry.getValue().setStatus(Status.COOKING.getUz());
                                        Long customerChatId = pizzaEntry.getValue().getUser().getUserChatId();
                                        editMessageText = new EditMessageText().setText("oshxonaga jo'natildi").setChatId(chatId).setMessageId(messageId);
                                        tryCatchForInline(editMessageText);
                                        switch (DeliveryService_bot.activeLanguage){
                                            case "uz":
                                                sendMessage.setText("pitsangiz pishirishga jo'natildi: " + pizzaName + " " + pizzaCount.substring(0, pizzaCount.indexOf("Qabul")));
                                                try {
                                                    deliveryService_bot.execute(sendMessage.setChatId(customerChatId));
                                                } catch (TelegramApiException e) {
                                                    e.printStackTrace();
                                                }
                                                break;
                                            case "ru":
                                                sendMessage.setText("Вашу пиццу отправили печь: " + pizzaName + " " + pizzaCount.substring(0, pizzaCount.indexOf("Qabul")));
                                                try {
                                                    deliveryService_bot.execute(sendMessage.setChatId(customerChatId));
                                                } catch (TelegramApiException e) {
                                                    e.printStackTrace();
                                                }
                                                break;
                                            case "en":
                                                sendMessage.setText("Your pizza was sent to bake: " + pizzaName + " " + pizzaCount.substring(0, pizzaCount.indexOf("Qabul")));
                                                try {
                                                    deliveryService_bot.execute(sendMessage.setChatId(customerChatId));
                                                } catch (TelegramApiException e) {
                                                    e.printStackTrace();
                                                }
                                                break;
                                        }

                                    }
                                }
                            }
                        }
                    }

                    else if (data.contains("yetkazuvchigaJo'natish")) {
                        for (Map.Entry<Long, OrderedPizza> pizzaEntry : orderedPizza.entrySet()) {
                            if (pizzaEntry.getValue().getManagerChatId().equals(chatId.toString())) {
                                if (text.contains(pizzaEntry.getValue().getPizzaType())) {
                                    if (text.contains(pizzaEntry.getValue().getCount())) {
                                        editMessageText = new EditMessageText().setText("Yetkazuvchiga jo'natildi").setChatId(chatId).setMessageId(messageId);
                                        tryCatchForInline(editMessageText);
                                        pizzaEntry.getValue().setStatus(Status.READYFORSEND.getUz());
                                        sendToDeliveryMen(pizzaEntry);
                                        switch (DeliveryService_bot.activeLanguage){
                                            case "uz":
                                                sendMessage.setText("Pitsangiz tayyor!!\n Yetkazuvchiga jo'natildi: " + pizzaName + " " + pizzaCount.substring(0, pizzaCount.indexOf("Pishirilyapti")));
                                                try {
                                                    deliveryService_bot.execute(sendMessage.setChatId(pizzaEntry.getValue().getUser().getUserChatId()));
                                                } catch (TelegramApiException e) {
                                                    e.printStackTrace();
                                                }
                                                break;
                                            case "ru":
                                                sendMessage.setText("Ваша пицца готова !!\n Отправлено поставщику : " + pizzaName + " " + pizzaCount.substring(0, pizzaCount.indexOf("Pishirilyapti")));
                                                try {
                                                    deliveryService_bot.execute(sendMessage.setChatId(pizzaEntry.getValue().getUser().getUserChatId()));
                                                } catch (TelegramApiException e) {
                                                    e.printStackTrace();
                                                }
                                                break;
                                            case "en":
                                                sendMessage.setText("Your pizza is ready !!\n Sent to supplier : " + pizzaName + " " + pizzaCount.substring(0, pizzaCount.indexOf("Pishirilyapti")));
                                                try {
                                                    deliveryService_bot.execute(sendMessage.setChatId(pizzaEntry.getValue().getUser().getUserChatId()));
                                                } catch (TelegramApiException e) {
                                                    e.printStackTrace();
                                                }
                                                break;
                                        }

                                    }
                                }
                            }
                        }
                    }
                    break;
                case "ru":
                    if (data.contains("qabulQilish")) {
                        Long indexx = Long.parseLong(data.substring(data.indexOf("&") + 1));
                        orderedPizza.get(indexx).setStatus(Status.RECEIVED.getRu());
                        orderedPizza.get(indexx).setManagerChatId(chatId.toString());
                        Long customerChatId = orderedPizza.get(indexx).getUser().getUserChatId();
                        editMessageText = new EditMessageText().setText("Buyurtma qabul qilindi").setChatId(chatId).setMessageId(messageId);
                        tryCatchForInline(editMessageText);
                        switch (DeliveryService_bot.activeLanguage){
                            case "uz":
                                sendMessage.setText("Buyurtmangiz qabul qilindi: " + pizzaName + " " + pizzaCount + "\n" + "Manager: " + managerUserName);
                                try {
                                    sendMessage.setChatId(customerChatId);
                                    deliveryService_bot.execute(sendMessage);
                                } catch (TelegramApiException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case "ru":
                                sendMessage.setText("ваш заказ был принят: " + pizzaName + " " + pizzaCount + "\n" + "Manager: " + managerUserName);
                                try {
                                    sendMessage.setChatId(customerChatId);
                                    deliveryService_bot.execute(sendMessage);
                                } catch (TelegramApiException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case "en":
                                sendMessage.setText("Your order has been accepted: " + pizzaName + " " + pizzaCount + "\n" + "Manager: " + managerUserName);
                                try {
                                    sendMessage.setChatId(customerChatId);
                                    deliveryService_bot.execute(sendMessage);
                                } catch (TelegramApiException e) {
                                    e.printStackTrace();
                                }
                                break;
                        }

                    }

                    else if (data.contains("oshxonagaJo'natish")) {
                        System.out.println(orderedPizza.size());
                        for (Map.Entry<Long, OrderedPizza> pizzaEntry : orderedPizza.entrySet()) {
                            if (pizzaEntry.getValue().getManagerChatId().equals(chatId.toString())) {
                                if (text.contains(pizzaEntry.getValue().getPizzaType())) {
                                    if (text.contains(pizzaEntry.getValue().getCount())) {
                                        pizzaEntry.getValue().setStatus(Status.COOKING.getRu());
                                        Long customerChatId = pizzaEntry.getValue().getUser().getUserChatId();
                                        editMessageText = new EditMessageText().setText("oshxonaga jo'natildi").setChatId(chatId).setMessageId(messageId);
                                        tryCatchForInline(editMessageText);
                                        switch (DeliveryService_bot.activeLanguage){
                                            case "uz":
                                                sendMessage.setText("pitsangiz pishirishga jo'natildi: " + pizzaName + " " + pizzaCount.substring(0, pizzaCount.indexOf("Qabul")));
                                                try {
                                                    deliveryService_bot.execute(sendMessage.setChatId(customerChatId));
                                                } catch (TelegramApiException e) {
                                                    e.printStackTrace();
                                                }
                                                break;
                                            case "ru":
                                                sendMessage.setText("Вашу пиццу отправили печь: " + pizzaName + " " + pizzaCount.substring(0, pizzaCount.indexOf("Qabul")));
                                                try {
                                                    deliveryService_bot.execute(sendMessage.setChatId(customerChatId));
                                                } catch (TelegramApiException e) {
                                                    e.printStackTrace();
                                                }
                                                break;
                                            case "en":
                                                sendMessage.setText("Your pizza was sent to bake: " + pizzaName + " " + pizzaCount.substring(0, pizzaCount.indexOf("Qabul")));
                                                try {
                                                    deliveryService_bot.execute(sendMessage.setChatId(customerChatId));
                                                } catch (TelegramApiException e) {
                                                    e.printStackTrace();
                                                }
                                                break;
                                        }

                                    }
                                }
                            }
                        }
                    }

                    else if (data.contains("yetkazuvchigaJo'natish")) {
                        for (Map.Entry<Long, OrderedPizza> pizzaEntry : orderedPizza.entrySet()) {
                            if (pizzaEntry.getValue().getManagerChatId().equals(update.getMessage().getChatId().toString())) {
                                if (text.contains(pizzaEntry.getValue().getPizzaType())) {
                                    if (text.contains(pizzaEntry.getValue().getCount())) {
                                        editMessageText = new EditMessageText().setText("Yetkazuvchiga jo'natildi").setChatId(chatId).setMessageId(messageId);
                                        tryCatchForInline(editMessageText);
                                        pizzaEntry.getValue().setStatus(Status.READYFORSEND.getRu());
                                        sendToDeliveryMen(pizzaEntry);
                                        switch (DeliveryService_bot.activeLanguage){
                                            case "uz":
                                                sendMessage.setText("Pitsangiz tayyor!!\n Yetkazuvchiga jo'natildi: " + pizzaName + " " + pizzaCount.substring(0, pizzaCount.indexOf("Qabul")));
                                                try {
                                                    deliveryService_bot.execute(sendMessage.setChatId(pizzaEntry.getValue().getUser().getUserChatId()));
                                                } catch (TelegramApiException e) {
                                                    e.printStackTrace();
                                                }
                                                break;
                                            case "ru":
                                                sendMessage.setText("Ваша пицца готова !!\n Отправлено поставщику : " + pizzaName + " " + pizzaCount.substring(0, pizzaCount.indexOf("Qabul")));
                                                try {
                                                    deliveryService_bot.execute(sendMessage.setChatId(pizzaEntry.getValue().getUser().getUserChatId()));
                                                } catch (TelegramApiException e) {
                                                    e.printStackTrace();
                                                }
                                                break;
                                            case "en":
                                                sendMessage.setText("Your pizza is ready !!\n Sent to supplier : " + pizzaName + " " + pizzaCount.substring(0, pizzaCount.indexOf("Qabul")));
                                                try {
                                                    deliveryService_bot.execute(sendMessage.setChatId(pizzaEntry.getValue().getUser().getUserChatId()));
                                                } catch (TelegramApiException e) {
                                                    e.printStackTrace();
                                                }
                                                break;
                                        }

                                    }
                                }
                            }
                        }
                    }
                    break;
                case "en":
                    if (data.contains("qabulQilish")) {
                        Long indexx = Long.parseLong(data.substring(data.indexOf("&") + 1));
                        orderedPizza.get(indexx).setStatus(Status.RECEIVED.getEn());
                        orderedPizza.get(indexx).setManagerChatId(chatId.toString());
                        Long customerChatId = orderedPizza.get(indexx).getUser().getUserChatId();
                        editMessageText = new EditMessageText().setText("Buyurtma qabul qilindi").setChatId(chatId).setMessageId(messageId);
                        tryCatchForInline(editMessageText);
                        switch (DeliveryService_bot.activeLanguage){
                            case "uz":
                                sendMessage.setText("Buyurtmangiz qabul qilindi: " + pizzaName + " " + pizzaCount + "\n" + "Manager: " + managerUserName);
                                try {
                                    sendMessage.setChatId(customerChatId);
                                    deliveryService_bot.execute(sendMessage);
                                } catch (TelegramApiException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case "ru":
                                sendMessage.setText("ваш заказ был принят: " + pizzaName + " " + pizzaCount + "\n" + "Manager: " + managerUserName);
                                try {
                                    sendMessage.setChatId(customerChatId);
                                    deliveryService_bot.execute(sendMessage);
                                } catch (TelegramApiException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case "en":
                                sendMessage.setText("Your order has been accepted: " + pizzaName + " " + pizzaCount + "\n" + "Manager: " + managerUserName);
                                try {
                                    sendMessage.setChatId(customerChatId);
                                    deliveryService_bot.execute(sendMessage);
                                } catch (TelegramApiException e) {
                                    e.printStackTrace();
                                }
                                break;
                        }

                    }

                    else if (data.contains("oshxonagaJo'natish")) {
                        System.out.println(orderedPizza.size());
                        for (Map.Entry<Long, OrderedPizza> pizzaEntry : orderedPizza.entrySet()) {
                            if (pizzaEntry.getValue().getManagerChatId().equals(chatId.toString())) {
                                if (text.contains(pizzaEntry.getValue().getPizzaType())) {
                                    if (text.contains(pizzaEntry.getValue().getCount())) {
                                        pizzaEntry.getValue().setStatus(Status.COOKING.getEn());
                                        Long customerChatId = pizzaEntry.getValue().getUser().getUserChatId();
                                        editMessageText = new EditMessageText().setText("oshxonaga jo'natildi").setChatId(chatId).setMessageId(messageId);
                                        tryCatchForInline(editMessageText);
                                        switch (DeliveryService_bot.activeLanguage){
                                            case "uz":
                                                sendMessage.setText("pitsangiz pishirishga jo'natildi: " + pizzaName + " " + pizzaCount.substring(0, pizzaCount.indexOf("Qabul")));
                                                try {
                                                    deliveryService_bot.execute(sendMessage.setChatId(customerChatId));
                                                } catch (TelegramApiException e) {
                                                    e.printStackTrace();
                                                }
                                                break;
                                            case "ru":
                                                sendMessage.setText("Вашу пиццу отправили печь: " + pizzaName + " " + pizzaCount.substring(0, pizzaCount.indexOf("Qabul")));
                                                try {
                                                    deliveryService_bot.execute(sendMessage.setChatId(customerChatId));
                                                } catch (TelegramApiException e) {
                                                    e.printStackTrace();
                                                }
                                                break;
                                            case "en":
                                                sendMessage.setText("Your pizza was sent to bake: " + pizzaName + " " + pizzaCount.substring(0, pizzaCount.indexOf("Qabul")));
                                                try {
                                                    deliveryService_bot.execute(sendMessage.setChatId(customerChatId));
                                                } catch (TelegramApiException e) {
                                                    e.printStackTrace();
                                                }
                                                break;
                                        }

                                    }
                                }
                            }
                        }
                    }

                    else if (data.contains("yetkazuvchigaJo'natish")) {
                        for (Map.Entry<Long, OrderedPizza> pizzaEntry : orderedPizza.entrySet()) {
                            if (pizzaEntry.getValue().getManagerChatId().equals(update.getMessage().getChatId().toString())) {
                                if (text.contains(pizzaEntry.getValue().getPizzaType())) {
                                    if (text.contains(pizzaEntry.getValue().getCount())) {
                                        editMessageText = new EditMessageText().setText("Yetkazuvchiga jo'natildi").setChatId(chatId).setMessageId(messageId);
                                        tryCatchForInline(editMessageText);
                                        pizzaEntry.getValue().setStatus(Status.READYFORSEND.getEn());
                                        sendToDeliveryMen(pizzaEntry);
                                        switch (DeliveryService_bot.activeLanguage){
                                            case "uz":
                                                sendMessage.setText("Pitsangiz tayyor!!\n Yetkazuvchiga jo'natildi: " + pizzaName + " " + pizzaCount.substring(0, pizzaCount.indexOf("Qabul")));
                                                try {
                                                    deliveryService_bot.execute(sendMessage.setChatId(pizzaEntry.getValue().getUser().getUserChatId()));
                                                } catch (TelegramApiException e) {
                                                    e.printStackTrace();
                                                }
                                                break;
                                            case "ru":
                                                sendMessage.setText("Ваша пицца готова !!\n Отправлено поставщику : " + pizzaName + " " + pizzaCount.substring(0, pizzaCount.indexOf("Qabul")));
                                                try {
                                                    deliveryService_bot.execute(sendMessage.setChatId(pizzaEntry.getValue().getUser().getUserChatId()));
                                                } catch (TelegramApiException e) {
                                                    e.printStackTrace();
                                                }
                                                break;
                                            case "en":
                                                sendMessage.setText("Your pizza is ready !!\n Sent to supplier : " + pizzaName + " " + pizzaCount.substring(0, pizzaCount.indexOf("Qabul")));
                                                try {
                                                    deliveryService_bot.execute(sendMessage.setChatId(pizzaEntry.getValue().getUser().getUserChatId()));
                                                } catch (TelegramApiException e) {
                                                    e.printStackTrace();
                                                }
                                                break;
                                        }

                                    }
                                }
                            }
                        }
                    }
                    break;


            }
        }
    }

    private void sendToDeliveryMen( Map.Entry<Long, OrderedPizza> pizzaEntry) {
        Delivery_Service_DeliveryMan_bot deliveryMan_bot = new Delivery_Service_DeliveryMan_bot();

        switch (Delivery_Service_DeliveryMan_bot.activeLanguage) {
            case "uz":
                try {
                    deliveryMan_bot.execute(setInlineOrder(745485034, pizzaEntry.getValue().getUser().getName() +
                            pizzaEntry.getValue().getUser().getAddress() + " // " + pizzaEntry.getValue().getUser().getPhoneNumber() +
                            " uchun\n" +pizzaEntry.getValue().getPizzaType() + " // " + pizzaEntry.getValue().getCount() + "\n" +
                            "ManagerChatId: " + pizzaEntry.getValue().getManagerChatId()));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                try {
                    deliveryMan_bot.execute(setInlineOrder(922039243, pizzaEntry.getValue().getUser().getName() +
                            pizzaEntry.getValue().getUser().getAddress() + " // " + pizzaEntry.getValue().getUser().getPhoneNumber() +
                            " uchun\n" +pizzaEntry.getValue().getPizzaType() + " // " + pizzaEntry.getValue().getCount() + "\n" +
                            "ManagerChatId: " + pizzaEntry.getValue().getManagerChatId()));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                break;
            case "ru":
                try {
                    deliveryMan_bot.execute(setInlineOrder(745485034, "Для "+pizzaEntry.getValue().getUser().getName() +
                            pizzaEntry.getValue().getUser().getAddress() + " // " + pizzaEntry.getValue().getUser().getPhoneNumber() +
                            "\n" +pizzaEntry.getValue().getPizzaType() + " // " + pizzaEntry.getValue().getCount() + "\n" +
                            "ManagerChatId: " + pizzaEntry.getValue().getManagerChatId()));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                try {
                    deliveryMan_bot.execute(setInlineOrder(922039243, "Для "+pizzaEntry.getValue().getUser().getName() +
                            pizzaEntry.getValue().getUser().getAddress() + " // " + pizzaEntry.getValue().getUser().getPhoneNumber() +
                            " uchun\n" +pizzaEntry.getValue().getPizzaType() + " // " + pizzaEntry.getValue().getCount() + "\n" +
                            "ManagerChatId: " + pizzaEntry.getValue().getManagerChatId()));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                break;
            case "en":
                try {
                    deliveryMan_bot.execute(setInlineOrder(745485034, "For "+pizzaEntry.getValue().getUser().getName() +
                            pizzaEntry.getValue().getUser().getAddress() + " // " + pizzaEntry.getValue().getUser().getPhoneNumber() +
                            " uchun\n" +pizzaEntry.getValue().getPizzaType() + " // " + pizzaEntry.getValue().getCount() + "\n" +
                            "ManagerChatId: " + pizzaEntry.getValue().getManagerChatId()));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                try {
                    deliveryMan_bot.execute(setInlineOrder(922039243, "For "+pizzaEntry.getValue().getUser().getName() +
                            pizzaEntry.getValue().getUser().getAddress() + " // " + pizzaEntry.getValue().getUser().getPhoneNumber() +
                            " uchun\n" +pizzaEntry.getValue().getPizzaType() + " // " + pizzaEntry.getValue().getCount() + "\n" +
                            "ManagerChatId: " + pizzaEntry.getValue().getManagerChatId()));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void tryCatchForInline(EditMessageText editMessageText) {
        try {
            execute(editMessageText);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public SendMessage setInlineOrder(long chatId, String text) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        switch (Delivery_Service_DeliveryMan_bot.activeLanguage) {
            case "uz":
                button1.setText(SelectLanguage.receive_uz);
                button1.setCallbackData("qabulQilish");
                break;
            case "ru":
                button1.setText(SelectLanguage.receive_ru);
                button1.setCallbackData("Принять");
                break;
            case "en":
                button1.setText(SelectLanguage.receive_en);
                button1.setCallbackData("Receive");
                break;
        }

        List<InlineKeyboardButton> buttonList = new ArrayList<>();
        buttonList.add(button1);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(buttonList);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return new SendMessage().setText(text).setChatId(chatId).setReplyMarkup(inlineKeyboardMarkup);
    }

    public static SendMessage setInlineSendToKitchen(long managerChatId, String text) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        switch (activeLanguage) {
            case "uz":
                button1.setText(SelectLanguage.sendToKitchen_uz);
                button1.setCallbackData("oshxonagaJo'natish");
                break;
            case "ru":
                button1.setCallbackData(SelectLanguage.sendToKitchen_ru);
                button1.setText(SelectLanguage.sendToKitchen_ru);
                break;
            case "en":
                button1.setCallbackData(SelectLanguage.sendToKitchen_en);
                button1.setText(SelectLanguage.sendToKitchen_en);
                break;
        }
        List<InlineKeyboardButton> buttonList = new ArrayList<>();
        buttonList.add(button1);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(buttonList);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return new SendMessage().setText(text).setChatId(managerChatId).setReplyMarkup(inlineKeyboardMarkup);
    }

    public static SendMessage setInlineSendToDelivery(long managerChatId, String text) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        switch (activeLanguage) {
            case "uz":
                button1.setText(SelectLanguage.sendToDelivery_uz);
                button1.setCallbackData("yetkazuvchigaJo'natish");
                break;
            case "ru":
                button1.setCallbackData(SelectLanguage.sendToDelivery_ru);
                button1.setText(SelectLanguage.sendToDelivery_ru);
                break;
            case "en":
                button1.setCallbackData(SelectLanguage.sendToDelivery_en);
                button1.setText(SelectLanguage.sendToDelivery_en);
                break;
        }
        List<InlineKeyboardButton> buttonList = new ArrayList<>();
        buttonList.add(button1);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(buttonList);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return new SendMessage().setText(text).setChatId(managerChatId).setReplyMarkup(inlineKeyboardMarkup);
    }

    private void myOrdersAndLanguage(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        switch (activeLanguage) {
            case "uz":
                row1.add(new KeyboardButton(SelectLanguage.myOrders_uz));
                row2.add(new KeyboardButton(Language.LANGUAGE.getEng()));
                row2.add(new KeyboardButton(Language.LANGUAGE.getRus()));
                row2.add(new KeyboardButton(Language.LANGUAGE.getUzb()));
                break;
            case "ru":
                row1.add(new KeyboardButton(SelectLanguage.myOrders_ru));
                row2.add(new KeyboardButton(Language.LANGUAGE.getEng()));
                row2.add(new KeyboardButton(Language.LANGUAGE.getRus()));
                row2.add(new KeyboardButton(Language.LANGUAGE.getUzb()));
                break;
            case "en":
                row1.add(new KeyboardButton(SelectLanguage.myOrders_en));
                row2.add(new KeyboardButton(Language.LANGUAGE.getEng()));
                row2.add(new KeyboardButton(Language.LANGUAGE.getRus()));
                row2.add(new KeyboardButton(Language.LANGUAGE.getUzb()));
                break;
        }
        keyboardRowList.add(row1);
        keyboardRowList.add(row2);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
    }

    private void tryCatch(SendMessage sendMessage, Update update) {
        try {
            sendMessage.setChatId(update.getMessage().getChatId());
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public static void setManagerList() {
        managerList.add(new Manager("745485034", getRandomId(), "Alekx", "123321", "123321"));
        managerList.add(new Manager("922039243", getRandomId(), "Bobur", "456654", "456654"));

    }

    public static String getRandomId() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    @Override
    public String getBotUsername() {
        return "http://t.me/Delivery_Service_Manager_BOT";
    }

    @Override
    public String getBotToken() {
        return "1763927910:AAHgx-Om9mmGcd1081LUT6YuWhIJM2KjwH8";
    }
}
