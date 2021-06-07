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

public class Delivery_Service_DeliveryMan_bot extends TelegramLongPollingBot {
    public static String activeLanguage = "uz";
    public static ConcurrentHashMap<Long, Integer> loginSteps = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<Long, String> deliveryManLogin = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<Long, String> deliveryManPassword = new ConcurrentHashMap<>();
    public static List<DeliveryMan> deliveryManList = new ArrayList<>();
    public static ConcurrentHashMap<DeliveryOrders, String> deliveryOrderList = new ConcurrentHashMap<>();


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
                    Long chatId = update.getMessage().getChatId();
                    for (Map.Entry<Long, OrderedPizza> pizzaEntry : Delivery_Service_Manager_bot.orderedPizza.entrySet()) {
                        if (pizzaEntry.getValue().getDeliveryManChatId().equals(chatId.toString())) {
                            switch (activeLanguage) {
                                case "uz":
                                    try {
                                        execute(setInlineDeliveredButton(chatId, pizzaEntry.getValue().getUser().getName() +
                                                pizzaEntry.getValue().getUser().getAddress() + " // " + pizzaEntry.getValue().getUser().getPhoneNumber() +
                                                " uchun\n" + pizzaEntry.getValue().getPizzaType() + " // " + pizzaEntry.getValue().getCount() + "\n" +
                                                "ManagerChatId: " + pizzaEntry.getValue().getManagerChatId()));
                                    } catch (TelegramApiException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                case "ru":
                                    try {
                                        execute(setInlineDeliveredButton(chatId, pizzaEntry.getValue().getUser().getName() +
                                                pizzaEntry.getValue().getUser().getAddress() + " // " + pizzaEntry.getValue().getUser().getPhoneNumber() +
                                                "\n" + pizzaEntry.getValue().getPizzaType() + " // " + pizzaEntry.getValue().getCount() + "\n" +
                                                "ManagerChatId: " + pizzaEntry.getValue().getManagerChatId()));
                                    } catch (TelegramApiException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                case "en":
                                    try {
                                        execute(setInlineDeliveredButton(chatId, pizzaEntry.getValue().getUser().getName() +
                                                pizzaEntry.getValue().getUser().getAddress() + " // " + pizzaEntry.getValue().getUser().getPhoneNumber() +
                                                " uchun\n" + pizzaEntry.getValue().getPizzaType() + " // " + pizzaEntry.getValue().getCount() + "\n" +
                                                "ManagerChatId: " + pizzaEntry.getValue().getManagerChatId()));
                                    } catch (TelegramApiException e) {
                                        e.printStackTrace();
                                    }
                                    break;
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
                        deliveryManLogin.put(update.getMessage().getChatId(), update.getMessage().getText());
                        sendMessage.setText("parol:");
                        tryCatch(sendMessage, update);
                        loginSteps.put(update.getMessage().getChatId(), 2);
                    } else if (loginSteps.get(update.getMessage().getChatId()) == 2) {
                        deliveryManPassword.put(update.getMessage().getChatId(), update.getMessage().getText());
                        for (DeliveryMan deliveryMan : deliveryManList) {
                            if (deliveryMan.getLogin().equals(deliveryManLogin.get(update.getMessage().getChatId())) &&
                                    deliveryMan.getPassword().equals(deliveryManPassword.get(update.getMessage().getChatId()))) {
                                System.out.println("deliveryMan founded");
                                myOrdersAndLanguage(sendMessage);
                                loginSteps.put(update.getMessage().getChatId(), 3);
                                sendMessage.setText("Hush kelibsiz " + update.getMessage().getChat().getFirstName());
                                tryCatch(sendMessage, update);
                            }
                        }
                    }
                    break;
            }
        } else if (update.hasCallbackQuery()) {
            Delivery_Service_Manager_bot manager_bot = new Delivery_Service_Manager_bot();
            Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            String data = update.getCallbackQuery().getData();
            String text = update.getCallbackQuery().getMessage().getText();
            switch (activeLanguage) {
                case "uz":
                    switch (data) {
                        case "qabulQilish":
                            String deliveryUserName = "@" + update.getCallbackQuery().getMessage().getChat().getUserName();
                            EditMessageText editMessageText = new EditMessageText().setText("Buyurtma qabul qilindi").setChatId(chatId).setMessageId(messageId);
                            tryCatchForInline(editMessageText);
                            for (Map.Entry<Long, OrderedPizza> pizzaEntry : Delivery_Service_Manager_bot.orderedPizza.entrySet()) {
                                if (pizzaEntry.getValue().getStatus().equals(Status.READYFORSEND.getUz())) {
                                    if (text.contains(pizzaEntry.getValue().getPizzaType())) {
                                        if (text.contains(pizzaEntry.getValue().getCount())) {
                                            pizzaEntry.getValue().setDeliveryManChatId(chatId.toString());
                                            pizzaEntry.getValue().setStatus(Status.SENDINGTOCUSTOMER.getUz());
                                            Long managerChatId = Long.parseLong(pizzaEntry.getValue().getManagerChatId());
                                            Long customerChatId = pizzaEntry.getValue().getUser().getUserChatId();
                                            switch (Delivery_Service_Manager_bot.activeLanguage) {
                                                case "uz":
                                                    sendMessage.setText("Buyurtmani qabul qildim:\n " +
                                                            "Yetkazuvchi" + deliveryUserName);
                                                    break;
                                                case "ru":
                                                    sendMessage.setText("Заказ принял:\n" +
                                                            "  Поставщик " + deliveryUserName);
                                                    break;
                                                case "en":
                                                    sendMessage.setText("I accepted the order:\n" +
                                                            "  Supplier " + deliveryUserName);
                                                    break;
                                            }
                                            try {
                                                manager_bot.execute(sendMessage.setChatId(managerChatId));
                                            } catch (TelegramApiException e) {
                                                e.printStackTrace();
                                            }
                                            DeliveryService_bot deliveryServiceBot = new DeliveryService_bot();
                                            switch (DeliveryService_bot.activeLanguage) {
                                                case "uz":
                                                    sendMessage.setText("Buyurtmangiz yo'lda ");
                                                    break;
                                                case "ru":
                                                    sendMessage.setText("Ваш заказ уже в пути ");
                                                    break;
                                                case "en":
                                                    sendMessage.setText("Your order is on the way");
                                                    break;
                                            }
                                            try {
                                                deliveryServiceBot.execute(sendMessage.setChatId(customerChatId));
                                            } catch (TelegramApiException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
                            }

                            break;
                        case "yetkazildi":
                            deliveryUserName = "@" + update.getCallbackQuery().getMessage().getChat().getUserName();
                             editMessageText = new EditMessageText().setText("Buyurtma yetkazildi").setChatId(chatId).setMessageId(messageId);
                            tryCatchForInline(editMessageText);
                            for (Map.Entry<Long, OrderedPizza> pizzaEntry : Delivery_Service_Manager_bot.orderedPizza.entrySet()) {
                                if (pizzaEntry.getValue().getDeliveryManChatId().equals(chatId.toString())) {
                                    if (text.contains(pizzaEntry.getValue().getPizzaType())) {
                                        if (text.contains(pizzaEntry.getValue().getCount())) {
                                            pizzaEntry.getValue().setStatus(Status.DELIVERED.getUz());
                                            Long managerChatId = Long.parseLong(pizzaEntry.getValue().getManagerChatId());
                                            Long customerChatId = pizzaEntry.getValue().getUser().getUserChatId();
                                            switch (Delivery_Service_Manager_bot.activeLanguage) {
                                                case "uz":
                                                    sendMessage.setText("Buyurtma yetkazildi:\n " +
                                                            "Yetkazuvchi" + deliveryUserName);
                                                    break;
                                                case "ru":
                                                    sendMessage.setText("Заказ доставлен:\n" +
                                                            "  Поставщик " + deliveryUserName);
                                                    break;
                                                case "en":
                                                    sendMessage.setText("The order has been delivered:\n" +
                                                            "  Supplier " + deliveryUserName);
                                                    break;
                                            }
                                            try {
                                                manager_bot.execute(sendMessage.setChatId(managerChatId));
                                            } catch (TelegramApiException e) {
                                                e.printStackTrace();
                                            }

                                            DeliveryService_bot deliveryServiceBot = new DeliveryService_bot();
                                            switch (DeliveryService_bot.activeLanguage) {
                                                case "uz":
                                                    sendMessage.setText("Buyurtmangiz yetib keldi ");
                                                    break;
                                                case "ru":
                                                    sendMessage.setText("Ваш заказ прибыл ");
                                                    break;
                                                case "en":
                                                    sendMessage.setText("Your order has arrived");
                                                    break;
                                            }
                                            try {
                                                deliveryServiceBot.execute(sendMessage.setChatId(customerChatId));
                                            } catch (TelegramApiException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
                            }
                            break;
                    }
                    break;
                case "ru":
                    break;
                case "en":
                    break;
            }
        }
    }

    public SendMessage setInlineDeliveredButton(long chatId, String text) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        switch (activeLanguage) {
            case "uz":
                button1.setText(SelectLanguage.delivered_uz);
                button1.setCallbackData("yetkazildi");
                break;
            case "ru":
                button1.setText(SelectLanguage.delivered_ru);
                button1.setCallbackData("отправлено");
                break;
            case "en":
                button1.setText(SelectLanguage.delivered_en);
                button1.setCallbackData("delivered");
                break;
        }
        List<InlineKeyboardButton> buttonList = new ArrayList<>();
        buttonList.add(button1);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(buttonList);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return new SendMessage().setText(text).setChatId(chatId).setReplyMarkup(inlineKeyboardMarkup);
    }

    private void tryCatchForInline(EditMessageText editMessageText) {
        try {
            execute(editMessageText);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
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

    public static void setDeliveryManList() {

        deliveryManList.add(new DeliveryMan("745485034", getRandomId(), "Alekx", "123321", "123321"));
        deliveryManList.add(new DeliveryMan("216179264", getRandomId(), "Davlat", "456654", "456654"));
    }

    private void tryCatch(SendMessage sendMessage, Update update) {
        try {
            sendMessage.setChatId(update.getMessage().getChatId());
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public static String getRandomId() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    @Override
    public String getBotUsername() {
        return "http://t.me/DeliveryMan_service_bot";
    }

    @Override
    public String getBotToken() {
        return "1786956713:AAE7uLpi7cKB2SDak_5nqAoHJKO32ASi1zI";
    }
}
