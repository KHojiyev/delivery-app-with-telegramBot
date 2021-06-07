package module3.lesson10_TelegramBot.ExtraTask;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class DeliveryService_bot extends TelegramLongPollingBot {
    public static String activeLanguage = "uz";
    public static ConcurrentHashMap<String, User> userList = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, String> registerName = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, String> registerAddress = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, String> registerPhoneNumber = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, Double> registerCash = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<Long, Integer> registerStep = new ConcurrentHashMap<>();
    public static String PizzaTypes = "";
    public static List<Pizza> pizzas = new ArrayList<>();
    public static ConcurrentHashMap<String, Pizza> selectedPizza = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, Integer> countPizza = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<Long, Boolean> addToBasket = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<Long, Boolean> orderFast = new ConcurrentHashMap<>();



    @Override
    public void onUpdateReceived(Update update) {
        System.out.println(update.getMessage().getChatId());
        userList.put("jnljnlnoln", new User(15646848469L,"AAAAAAAA", "AAAAAAAA", "AAAAAAAA", 0));
        userList.put("745485034", new User(745485034L,"Lochinbek", "Chilonzor", "997986017", 1500000));
        SendMessage sendMessage = new SendMessage();
        boolean notFound = true;
        if (update.hasMessage()) {
            for (ConcurrentHashMap.Entry<String, User> stringUserEntry : userList.entrySet()) {
                if (stringUserEntry.getKey().equals(update.getMessage().getChatId().toString())) {
                    notFound = false;
                    switch (activeLanguage) {
                        case "uz":
                            switch (update.getMessage().getText()) {
                                case "/start":
                                    setMainMenu(sendMessage, activeLanguage);
                                    sendMessage.setText(Language.SELECT.getUzb());
                                    tryCatch(sendMessage, update);
                                    break;
                                case SelectLanguage.selectPizza_uz:
                                    sendMessage.setText(PizzaTypes);
                                    backButton(sendMessage, activeLanguage);
                                    tryCatch(sendMessage, update);
                                    break;
                                case SelectLanguage.setting_uz:
                                    sendMessage.setText(Language.SELECT.getUzb());
                                    setButtonForLanguageOfBot(sendMessage);
                                    tryCatch(sendMessage, update);
                                    break;
                                case SelectLanguage.backButton_uz:
                                    selectedPizza.remove(update.getMessage().getChatId().toString());
                                    setMainMenu(sendMessage, activeLanguage);
                                    sendMessage.setText(Language.SELECT.getUzb());
                                    tryCatch(sendMessage, update);
                                    break;
                                case "/Neapolitan":
                                    for (Pizza pizza : pizzas) {
                                        if (pizza.getName().startsWith("Neapolitan")) {
                                            sendMessage.setText(pizza.getName() + "\n" + pizza.getCost() + "\n" + pizza.getCookingOfWhat());
                                            selectedPizza.put(update.getMessage().getChatId().toString(),
                                                    new Pizza(pizza.getName(), pizza.getCost(), pizza.getCookingOfWhat(), pizza.getQuantity()));
                                        }
                                    }
                                    addToShoppingBoxButtons(sendMessage);
                                    tryCatch(sendMessage, update);
                                    break;
                                case "/New":
                                    for (Pizza pizza : pizzas) {
                                        if (pizza.getName().startsWith("New")) {
                                            sendMessage.setText(pizza.getName() + "\n" + pizza.getCost() + "\n" + pizza.getCookingOfWhat());
                                            selectedPizza.put(update.getMessage().getChatId().toString(),
                                                    new Pizza(pizza.getName(), pizza.getCost(), pizza.getCookingOfWhat(), pizza.getQuantity()));
                                        }
                                    }
                                    addToShoppingBoxButtons(sendMessage);
                                    tryCatch(sendMessage, update);
                                    break;
                                case "/Chicago":
                                    for (Pizza pizza : pizzas) {
                                        if (pizza.getName().startsWith("Chicago")) {
                                            sendMessage.setText(pizza.getName() + "\n" + pizza.getCost() + "\n" + pizza.getCookingOfWhat());
                                            selectedPizza.put(update.getMessage().getChatId().toString(),
                                                    new Pizza(pizza.getName(), pizza.getCost(), pizza.getCookingOfWhat(), pizza.getQuantity()));
                                        }
                                    }
                                    addToShoppingBoxButtons(sendMessage);
                                    tryCatch(sendMessage, update);
                                    break;
                                case "/Sicilian":
                                    for (Pizza pizza : pizzas) {
                                        if (pizza.getName().startsWith("Sicilian")) {
                                            sendMessage.setText(pizza.getName() + "\n" + pizza.getCost() + "\n" + pizza.getCookingOfWhat());
                                            selectedPizza.put(update.getMessage().getChatId().toString(),
                                                    new Pizza(pizza.getName(), pizza.getCost(), pizza.getCookingOfWhat(), pizza.getQuantity()));
                                        }
                                    }
                                    addToShoppingBoxButtons(sendMessage);
                                    tryCatch(sendMessage, update);
                                    break;
                                case "/Greek":
                                    for (Pizza pizza : pizzas) {
                                        if (pizza.getName().startsWith("Greek")) {
                                            sendMessage.setText(pizza.getName() + "\n" + pizza.getCost() + "\n" + pizza.getCookingOfWhat());
                                            selectedPizza.put(update.getMessage().getChatId().toString(),
                                                    new Pizza(pizza.getName(), pizza.getCost(), pizza.getCookingOfWhat(), pizza.getQuantity()));
                                        }
                                    }
                                    addToShoppingBoxButtons(sendMessage);
                                    tryCatch(sendMessage, update);
                                    break;
                                case SelectLanguage.order_through_uz:
                                    orderFast.put(update.getMessage().getChatId(), false);
                                    addToBasket.put(update.getMessage().getChatId(), true);
                                    sendMessage.setText(Language.COUNT_PIZZA.getUzb());
                                    backButton(sendMessage, activeLanguage);
                                    tryCatch(sendMessage, update);
                                    break;
                                case SelectLanguage.addToBasket_uz:
                                    addToBasket.put(update.getMessage().getChatId(), false);
                                    orderFast.put(update.getMessage().getChatId(), true);
                                    sendMessage.setText(Language.COUNT_PIZZA.getUzb());
                                    backButton(sendMessage, activeLanguage);
                                    tryCatch(sendMessage, update);
                                    break;
                                case SelectLanguage.orderBasket_uz:
                                    orderDeleteButtons(sendMessage);
                                    try {
                                        BufferedReader bufferedReader = new BufferedReader(new
                                                FileReader("src/main/resources/" + update.getMessage().getChatId() + ".txt"));
                                        String line;
                                        while (null != (line = bufferedReader.readLine())) {
                                            sendMessage.setText(line);
                                            tryCatch(sendMessage, update);
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                case SelectLanguage.order_Pizza_uz:
                                    File file = new File("src/main/resources/" + update.getMessage().getChatId() + ".txt");
                                    sentToManager(update, file);
                                    backButton(sendMessage, activeLanguage);
                                    sendMessage.setText(Language.ORDER_SUCCESSFULLY.getUzb());
                                    tryCatch(sendMessage, update);
                                    cleanFile(update);
                                    break;
                                case SelectLanguage.delete_Order_uz:
                                    backButton(sendMessage, activeLanguage);
                                    sendMessage.setText(Language.DELETED_SUCCESSFULLY.getUzb());
                                    tryCatch(sendMessage, update);
                                    cleanFile(update);
                                    break;
                                case SelectLanguage.language_uz:
                                    activeLanguage = "uz";
                                    setMainMenu(sendMessage, activeLanguage);
                                    sendMessage.setText("👍");
                                    tryCatch(sendMessage, update);
                                    break;
                                case SelectLanguage.language_ru:
                                    activeLanguage = "ru";
                                    setMainMenu(sendMessage, activeLanguage);
                                    sendMessage.setText("👍");
                                    tryCatch(sendMessage, update);
                                    break;
                                case SelectLanguage.language_en:
                                    activeLanguage = "en";
                                    setMainMenu(sendMessage, activeLanguage);
                                    sendMessage.setText("👍");
                                    tryCatch(sendMessage, update);
                                    break;
                                default:
                                    if (!addToBasket.get(update.getMessage().getChatId())) {
                                        System.out.println("enter default");
                                        sendMessage.setText(Language.ADDED_TO_BASKET.getUzb());
                                        tryCatch(sendMessage, update);
                                        sendMessage.setText(PizzaTypes);
                                        backButton(sendMessage, activeLanguage);
                                        tryCatch(sendMessage, update);
                                        addToBasket.remove(update.getMessage().getChatId());
                                        countPizza.put(update.getMessage().getChatId().toString(),
                                                Integer.parseInt(update.getMessage().getText()));
                                        try {
                                            addOrderPizza(update);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                     else if (!orderFast.get(update.getMessage().getChatId())) {
                                        System.out.println("fast send");
                                        countPizza.put(update.getMessage().getChatId().toString(), Integer.parseInt(update.getMessage().getText()));
                                        sendMessage.setText(Language.ORDER_SUCCESSFULLY.getUzb());
                                        tryCatch(sendMessage, update);
                                        backButton(sendMessage, activeLanguage);
                                        orderFast.remove(update.getMessage().getChatId());
                                        sendFastToManager(update);

                                    }

                                    break;

                            }
                            break;
                        case "ru":
                            switch (update.getMessage().getText()) {
                                case "/start":
                                    setMainMenu(sendMessage, activeLanguage);
                                    sendMessage.setText(Language.SELECT.getUzb());
                                    tryCatch(sendMessage, update);
                                    break;
                                case SelectLanguage.selectPizza_ru:
                                    sendMessage.setText(PizzaTypes);
                                    backButton(sendMessage, activeLanguage);
                                    tryCatch(sendMessage, update);
                                    break;
                                case SelectLanguage.setting_ru:
                                    sendMessage.setText(Language.SELECT.getUzb());
                                    setButtonForLanguageOfBot(sendMessage);
                                    tryCatch(sendMessage, update);
                                    break;
                                case SelectLanguage.backButton_ru:
                                    selectedPizza.remove(update.getMessage().getChatId().toString());
                                    setMainMenu(sendMessage, activeLanguage);
                                    sendMessage.setText(Language.SELECT.getRus());
                                    tryCatch(sendMessage, update);
                                    break;
                                case "/Neapolitan":
                                    for (Pizza pizza : pizzas) {
                                        if (pizza.getName().startsWith("Neapolitan")) {
                                            sendMessage.setText(pizza.getName() + "\n" + pizza.getCost() + "\n" + pizza.getCookingOfWhat());
                                            selectedPizza.put(update.getMessage().getChatId().toString(),
                                                    new Pizza(pizza.getName(), pizza.getCost(), pizza.getCookingOfWhat(), pizza.getQuantity()));
                                        }
                                    }
                                    addToShoppingBoxButtons(sendMessage);
                                    tryCatch(sendMessage, update);
                                    break;
                                case "/New":
                                    for (Pizza pizza : pizzas) {
                                        if (pizza.getName().startsWith("New")) {
                                            sendMessage.setText(pizza.getName() + "\n" + pizza.getCost() + "\n" + pizza.getCookingOfWhat());
                                            selectedPizza.put(update.getMessage().getChatId().toString(),
                                                    new Pizza(pizza.getName(), pizza.getCost(), pizza.getCookingOfWhat(), pizza.getQuantity()));
                                        }
                                    }
                                    addToShoppingBoxButtons(sendMessage);
                                    tryCatch(sendMessage, update);
                                    break;
                                case "/Chicago":
                                    for (Pizza pizza : pizzas) {
                                        if (pizza.getName().startsWith("Chicago")) {
                                            sendMessage.setText(pizza.getName() + "\n" + pizza.getCost() + "\n" + pizza.getCookingOfWhat());
                                            selectedPizza.put(update.getMessage().getChatId().toString(),
                                                    new Pizza(pizza.getName(), pizza.getCost(), pizza.getCookingOfWhat(), pizza.getQuantity()));
                                        }
                                    }
                                    addToShoppingBoxButtons(sendMessage);
                                    tryCatch(sendMessage, update);
                                    break;
                                case "/Sicilian":
                                    for (Pizza pizza : pizzas) {
                                        if (pizza.getName().startsWith("Sicilian")) {
                                            sendMessage.setText(pizza.getName() + "\n" + pizza.getCost() + "\n" + pizza.getCookingOfWhat());
                                            selectedPizza.put(update.getMessage().getChatId().toString(),
                                                    new Pizza(pizza.getName(), pizza.getCost(), pizza.getCookingOfWhat(), pizza.getQuantity()));
                                        }
                                    }
                                    addToShoppingBoxButtons(sendMessage);
                                    tryCatch(sendMessage, update);
                                    break;
                                case "/Greek":
                                    for (Pizza pizza : pizzas) {
                                        if (pizza.getName().startsWith("Greek")) {
                                            sendMessage.setText(pizza.getName() + "\n" + pizza.getCost() + "\n" + pizza.getCookingOfWhat());
                                            selectedPizza.put(update.getMessage().getChatId().toString(),
                                                    new Pizza(pizza.getName(), pizza.getCost(), pizza.getCookingOfWhat(), pizza.getQuantity()));
                                        }
                                    }
                                    addToShoppingBoxButtons(sendMessage);
                                    tryCatch(sendMessage, update);
                                    break;
                                case SelectLanguage.order_through_ru:
                                    orderFast.put(update.getMessage().getChatId(), false);
                                    addToBasket.put(update.getMessage().getChatId(), true);
                                    sendMessage.setText(Language.COUNT_PIZZA.getRus());
                                    backButton(sendMessage, activeLanguage);
                                    tryCatch(sendMessage, update);
                                    break;
                                case SelectLanguage.addToBasket_ru:
                                    addToBasket.put(update.getMessage().getChatId(), false);
                                    orderFast.put(update.getMessage().getChatId(), true);
                                    sendMessage.setText(Language.COUNT_PIZZA.getRus());
                                    backButton(sendMessage, activeLanguage);
                                    tryCatch(sendMessage, update);
                                    break;
                                case SelectLanguage.orderBasket_ru:
                                    System.out.println("savatchaga kirdi");
                                    orderDeleteButtons(sendMessage);
                                    try {
                                        BufferedReader bufferedReader = new BufferedReader(new
                                                FileReader("src/main/resources/" + update.getMessage().getChatId() + ".txt"));
                                        String line;
                                        while (null != (line = bufferedReader.readLine())) {
                                            sendMessage.setText(line);
                                            tryCatch(sendMessage, update);
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                case SelectLanguage.order_Pizza_ru:
                                    File file = new File("src/main/resources/" + update.getMessage().getChatId() + ".txt");
                                    sentToManager(update, file);
                                    backButton(sendMessage, activeLanguage);
                                    sendMessage.setText(Language.ORDER_SUCCESSFULLY.getRus());
                                    tryCatch(sendMessage, update);
                                    cleanFile(update);
                                    break;
                                case SelectLanguage.delete_Order_ru:
                                    backButton(sendMessage, activeLanguage);
                                    sendMessage.setText(Language.DELETED_SUCCESSFULLY.getRus());

                                    tryCatch(sendMessage, update);
                                    cleanFile(update);
                                    break;
                                case SelectLanguage.language_uz:
                                    activeLanguage = "uz";
                                    setMainMenu(sendMessage, activeLanguage);
                                    sendMessage.setText("👍");
                                    tryCatch(sendMessage, update);
                                    break;
                                case SelectLanguage.language_ru:
                                    activeLanguage = "ru";
                                    setMainMenu(sendMessage, activeLanguage);
                                    sendMessage.setText("👍");
                                    tryCatch(sendMessage, update);
                                    break;
                                case SelectLanguage.language_en:
                                    activeLanguage = "en";
                                    setMainMenu(sendMessage, activeLanguage);
                                    sendMessage.setText("👍");
                                    tryCatch(sendMessage, update);
                                    break;
                                default:
                                    if (!addToBasket.get(update.getMessage().getChatId())) {
                                        System.out.println("enter default");
                                        sendMessage.setText(Language.ADDED_TO_BASKET.getRus());
                                        tryCatch(sendMessage, update);
                                        sendMessage.setText(PizzaTypes);
                                        backButton(sendMessage, activeLanguage);
                                        tryCatch(sendMessage, update);
                                        addToBasket.remove(update.getMessage().getChatId());
                                        countPizza.put(update.getMessage().getChatId().toString(),
                                                Integer.parseInt(update.getMessage().getText()));
                                        try {
                                            addOrderPizza(update);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    } else if (!orderFast.get(update.getMessage().getChatId())) {
                                        System.out.println("fast send");
                                        countPizza.put(update.getMessage().getChatId().toString(), Integer.parseInt(update.getMessage().getText()));
                                        sendMessage.setText(Language.ORDER_SUCCESSFULLY.getRus());
                                        tryCatch(sendMessage, update);
                                        backButton(sendMessage, activeLanguage);
                                        orderFast.remove(update.getMessage().getChatId());
                                        sendFastToManager(update);

                                    }
                                    break;


                            }
                            break;
                        case "en":
                            switch (update.getMessage().getText()) {
                                case "/start":
                                    setMainMenu(sendMessage, activeLanguage);
                                    sendMessage.setText(Language.SELECT.getUzb());
                                    tryCatch(sendMessage, update);
                                    break;
                                case SelectLanguage.selectPizza_en:
                                    sendMessage.setText(PizzaTypes);
                                    backButton(sendMessage, activeLanguage);
                                    tryCatch(sendMessage, update);
                                    break;
                                case SelectLanguage.setting_ru:
                                    sendMessage.setText(Language.SELECT.getUzb());
                                    setButtonForLanguageOfBot(sendMessage);
                                    tryCatch(sendMessage, update);
                                    break;
                                case SelectLanguage.backButton_en:
                                    selectedPizza.remove(update.getMessage().getChatId().toString());
                                    setMainMenu(sendMessage, activeLanguage);
                                    sendMessage.setText(Language.SELECT.getEng());
                                    tryCatch(sendMessage, update);
                                    break;
                                case "/Neapolitan":
                                    for (Pizza pizza : pizzas) {
                                        if (pizza.getName().startsWith("Neapolitan")) {
                                            sendMessage.setText(pizza.getName() + "\n" + pizza.getCost() + "\n" + pizza.getCookingOfWhat());
                                            selectedPizza.put(update.getMessage().getChatId().toString(),
                                                    new Pizza(pizza.getName(), pizza.getCost(), pizza.getCookingOfWhat(), pizza.getQuantity()));
                                        }
                                    }
                                    addToShoppingBoxButtons(sendMessage);
                                    tryCatch(sendMessage, update);
                                    break;
                                case "/New":
                                    for (Pizza pizza : pizzas) {
                                        if (pizza.getName().startsWith("New")) {
                                            sendMessage.setText(pizza.getName() + "\n" + pizza.getCost() + "\n" + pizza.getCookingOfWhat());
                                            selectedPizza.put(update.getMessage().getChatId().toString(),
                                                    new Pizza(pizza.getName(), pizza.getCost(), pizza.getCookingOfWhat(), pizza.getQuantity()));
                                        }
                                    }
                                    addToShoppingBoxButtons(sendMessage);
                                    tryCatch(sendMessage, update);
                                    break;
                                case "/Chicago":
                                    for (Pizza pizza : pizzas) {
                                        if (pizza.getName().startsWith("Chicago")) {
                                            sendMessage.setText(pizza.getName() + "\n" + pizza.getCost() + "\n" + pizza.getCookingOfWhat());
                                            selectedPizza.put(update.getMessage().getChatId().toString(),
                                                    new Pizza(pizza.getName(), pizza.getCost(), pizza.getCookingOfWhat(), pizza.getQuantity()));
                                        }
                                    }
                                    addToShoppingBoxButtons(sendMessage);
                                    tryCatch(sendMessage, update);
                                    break;
                                case "/Sicilian":
                                    for (Pizza pizza : pizzas) {
                                        if (pizza.getName().startsWith("Sicilian")) {
                                            sendMessage.setText(pizza.getName() + "\n" + pizza.getCost() + "\n" + pizza.getCookingOfWhat());
                                            selectedPizza.put(update.getMessage().getChatId().toString(),
                                                    new Pizza(pizza.getName(), pizza.getCost(), pizza.getCookingOfWhat(), pizza.getQuantity()));
                                        }
                                    }
                                    addToShoppingBoxButtons(sendMessage);
                                    tryCatch(sendMessage, update);
                                    break;
                                case "/Greek":
                                    for (Pizza pizza : pizzas) {
                                        if (pizza.getName().startsWith("Greek")) {
                                            sendMessage.setText(pizza.getName() + "\n" + pizza.getCost() + "\n" + pizza.getCookingOfWhat());
                                            selectedPizza.put(update.getMessage().getChatId().toString(),
                                                    new Pizza(pizza.getName(), pizza.getCost(), pizza.getCookingOfWhat(), pizza.getQuantity()));
                                        }
                                    }
                                    addToShoppingBoxButtons(sendMessage);
                                    tryCatch(sendMessage, update);
                                    break;
                                case SelectLanguage.order_through_en:
                                    orderFast.put(update.getMessage().getChatId(), false);
                                    addToBasket.put(update.getMessage().getChatId(), true);
                                    sendMessage.setText(Language.COUNT_PIZZA.getEng());
                                    backButton(sendMessage, activeLanguage);
                                    tryCatch(sendMessage, update);
                                    break;
                                case SelectLanguage.addToBasket_en:
                                    addToBasket.put(update.getMessage().getChatId(), false);
                                    orderFast.put(update.getMessage().getChatId(), true);
                                    sendMessage.setText(Language.COUNT_PIZZA.getEng());
                                    backButton(sendMessage, activeLanguage);
                                    tryCatch(sendMessage, update);
                                    break;
                                case SelectLanguage.orderBasket_en:
                                    System.out.println("savatchaga kirdi");
                                    orderDeleteButtons(sendMessage);
                                    try {
                                        BufferedReader bufferedReader = new BufferedReader(new
                                                FileReader("src/main/resources/" + update.getMessage().getChatId() + ".txt"));
                                        String line;
                                        while (null != (line = bufferedReader.readLine())) {
                                            sendMessage.setText(line);
                                            tryCatch(sendMessage, update);
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                case SelectLanguage.order_Pizza_en:
                                    File file = new File("src/main/resources/" + update.getMessage().getChatId() + ".txt");
                                    sentToManager(update, file);
                                    backButton(sendMessage, activeLanguage);
                                    sendMessage.setText(Language.ORDER_SUCCESSFULLY.getEng());
                                    tryCatch(sendMessage, update);
                                    cleanFile(update);
                                    break;
                                case SelectLanguage.delete_Order_en:
                                    backButton(sendMessage, activeLanguage);
                                    sendMessage.setText(Language.DELETED_SUCCESSFULLY.getEng());

                                    tryCatch(sendMessage, update);
                                    cleanFile(update);
                                    break;
                                case SelectLanguage.language_uz:
                                    activeLanguage = "uz";
                                    setMainMenu(sendMessage, activeLanguage);
                                    sendMessage.setText("👍");
                                    tryCatch(sendMessage, update);
                                    break;
                                case SelectLanguage.language_ru:
                                    activeLanguage = "ru";
                                    setMainMenu(sendMessage, activeLanguage);
                                    sendMessage.setText("👍");
                                    tryCatch(sendMessage, update);
                                    break;
                                case SelectLanguage.language_en:
                                    activeLanguage = "en";
                                    setMainMenu(sendMessage, activeLanguage);
                                    sendMessage.setText("👍");
                                    tryCatch(sendMessage, update);
                                    break;
                                default:
                                    if (!addToBasket.get(update.getMessage().getChatId())) {
                                        System.out.println("enter default");
                                        sendMessage.setText(Language.ADDED_TO_BASKET.getEng());
                                        tryCatch(sendMessage, update);
                                        sendMessage.setText(PizzaTypes);
                                        backButton(sendMessage, activeLanguage);
                                        tryCatch(sendMessage, update);
                                        addToBasket.remove(update.getMessage().getChatId());
                                        countPizza.put(update.getMessage().getChatId().toString(),
                                                Integer.parseInt(update.getMessage().getText()));
                                        try {
                                            addOrderPizza(update);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    } else if (!orderFast.get(update.getMessage().getChatId())) {
                                        System.out.println("fast send");
                                        countPizza.put(update.getMessage().getChatId().toString(), Integer.parseInt(update.getMessage().getText()));
                                        sendMessage.setText(Language.ORDER_SUCCESSFULLY.getEng());
                                        tryCatch(sendMessage, update);
                                        backButton(sendMessage, activeLanguage);
                                        orderFast.remove(update.getMessage().getChatId());
                                        sendFastToManager(update);

                                    }
                                    break;


                            }
                            break;
                    }
                }
            }
            if (notFound) {
                switch (update.getMessage().getText()) {
                    case "/start":
                        sendMessage.setText("AssalomuAleykum\n" +
                                "Добро пожеловать\n" +
                                "Welcome to Delivery Service bot ");
                        setButtonForLanguageOfBot(sendMessage);
                        tryCatch(sendMessage, update);
                        break;
                    case SelectLanguage.language_uz:
                        activeLanguage = "uz";
                        sendMessage.setText(Language.FIRST_ENTER.getUzb() + "\n" + Language.NAME.getUzb());
                        tryCatch(sendMessage, update);
                        registerStep.put(update.getMessage().getChatId(), 1);
                        break;
                    case SelectLanguage.language_ru:
                        activeLanguage = "ru";
                        sendMessage.setText(Language.FIRST_ENTER.getRus() + "\n" + Language.NAME.getRus());
                        tryCatch(sendMessage, update);
                        registerStep.put(update.getMessage().getChatId(), 1);
                        break;
                    case SelectLanguage.language_en:
                        activeLanguage = "en";
                        sendMessage.setText(Language.FIRST_ENTER.getEng() + "\n" + Language.NAME.getEng());
                        tryCatch(sendMessage, update);
                        registerStep.put(update.getMessage().getChatId(), 1);
                        break;
                    default:
                        String temp = update.getMessage().getText();
                        switch (activeLanguage) {
                            case "uz":
                                switch (registerStep.get(update.getMessage().getChatId())) {
                                    case 1:
                                        registerName.put(update.getMessage().getChatId().toString(), temp);
                                        sendMessage.setText(Language.ADDRESS.getUzb());
                                        registerStep.put(update.getMessage().getChatId(), 2);
                                        tryCatch(sendMessage, update);
                                        break;
                                    case 2:
                                        registerAddress.put(update.getMessage().getChatId().toString(), temp);
                                        sendMessage.setText(Language.PHONE_NUMBER.getUzb());
                                        registerStep.put(update.getMessage().getChatId(), 3);
                                        tryCatch(sendMessage, update);
                                        break;
                                    case 3:
                                        registerPhoneNumber.put(update.getMessage().getChatId().toString(), temp);
                                        sendMessage.setText(Language.CASH.getUzb());
                                        registerStep.put(update.getMessage().getChatId(), 4);
                                        tryCatch(sendMessage, update);
                                        break;
                                    case 4:
                                        registerCash.put(update.getMessage().getChatId().toString(), Double.parseDouble(temp));
                                        sendMessage.setText(Language.FINISH_REGISTER.getUzb());
                                        finishRegister(update);
                                        setMainMenu(sendMessage, activeLanguage);
                                        registerStep.put(update.getMessage().getChatId(), 5);
                                        registerStep.remove(update.getMessage().getChatId());
                                        tryCatch(sendMessage, update);
                                        break;
                                }
                                break;
                            case "ru":
                                switch (registerStep.get(update.getMessage().getChatId())) {
                                    case 1:
                                        registerName.put(update.getMessage().getChatId().toString(), temp);
                                        sendMessage.setText(Language.ADDRESS.getRus());
                                        registerStep.put(update.getMessage().getChatId(), 2);
                                        tryCatch(sendMessage, update);
                                        break;
                                    case 2:
                                        registerAddress.put(update.getMessage().getChatId().toString(), temp);
                                        sendMessage.setText(Language.PHONE_NUMBER.getRus());
                                        registerStep.put(update.getMessage().getChatId(), 3);
                                        tryCatch(sendMessage, update);
                                        break;
                                    case 3:
                                        registerPhoneNumber.put(update.getMessage().getChatId().toString(), temp);
                                        sendMessage.setText(Language.CASH.getRus());
                                        registerStep.put(update.getMessage().getChatId(), 4);
                                        tryCatch(sendMessage, update);
                                        break;
                                    case 4:
                                        registerCash.put(update.getMessage().getChatId().toString(), Double.parseDouble(temp));
                                        sendMessage.setText(Language.FINISH_REGISTER.getRus());
                                        finishRegister(update);
                                        setMainMenu(sendMessage, activeLanguage);
                                        registerStep.put(update.getMessage().getChatId(), 5);
                                        registerStep.remove(update.getMessage().getChatId());
                                        tryCatch(sendMessage, update);
                                        break;
                                }
                                break;
                            case "en":
                                switch (registerStep.get(update.getMessage().getChatId())) {
                                    case 1:
                                        registerName.put(update.getMessage().getChatId().toString(), temp);
                                        sendMessage.setText(Language.ADDRESS.getEng());
                                        registerStep.put(update.getMessage().getChatId(), 2);
                                        tryCatch(sendMessage, update);
                                        break;
                                    case 2:
                                        registerAddress.put(update.getMessage().getChatId().toString(), temp);
                                        sendMessage.setText(Language.PHONE_NUMBER.getEng());
                                        registerStep.put(update.getMessage().getChatId(), 3);
                                        tryCatch(sendMessage, update);
                                        break;
                                    case 3:
                                        registerPhoneNumber.put(update.getMessage().getChatId().toString(), temp);
                                        sendMessage.setText(Language.CASH.getEng());
                                        registerStep.put(update.getMessage().getChatId(), 4);
                                        tryCatch(sendMessage, update);
                                        break;
                                    case 4:
                                        registerCash.put(update.getMessage().getChatId().toString(), Double.parseDouble(temp));
                                        sendMessage.setText(Language.FINISH_REGISTER.getEng());
                                        finishRegister(update);
                                        setMainMenu(sendMessage, activeLanguage);
                                        registerStep.put(update.getMessage().getChatId(), 5);
                                        registerStep.remove(update.getMessage().getChatId());
                                        tryCatch(sendMessage, update);
                                        break;
                                }
                                break;

                        }
                        break;
                }
            }
        }
    }

    private void sendFastToManager(Update update) {
        LocalDateTime localDateTime = LocalDateTime.now();
        Delivery_Service_Manager_bot manager_bot = new Delivery_Service_Manager_bot();
        String chatId = update.getMessage().getChatId().toString();
        switch (Delivery_Service_Manager_bot.activeLanguage) {
            case "uz":
                Delivery_Service_Manager_bot.orderedPizza.put(Delivery_Service_Manager_bot.index, new OrderedPizza(Delivery_Service_Manager_bot.orderNumber,
                        userList.get(chatId), "","","Name: " +selectedPizza.get(chatId).getName(),"// Count: "+countPizza.get(chatId),localDateTime, Status.NEW.getUz()));
                try {
                    manager_bot.execute(setInlineOrder(745485034, "@" + update.getMessage().getChat().getUserName() +"("+update.getMessage().getChatId()+
                            ") dan Yangi buyurtma " + "Name: " + selectedPizza.get(update.getMessage().getChatId().toString()).getName() +
                            " // Count: " + countPizza.get(update.getMessage().getChatId().toString())));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                try {
                    manager_bot.execute(setInlineOrder(922039243, "@" + update.getMessage().getChat().getUserName() +"("+update.getMessage().getChatId()+
                            ") dan Yangi buyurtma " + "Name: " + selectedPizza.get(update.getMessage().getChatId().toString()).getName() +
                            " // Count: " + countPizza.get(update.getMessage().getChatId().toString())));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                Delivery_Service_Manager_bot.index++;

                break;
            case "ru":
                Delivery_Service_Manager_bot.orderedPizza.put(Delivery_Service_Manager_bot.index, new OrderedPizza(Delivery_Service_Manager_bot.orderNumber,
                        userList.get(chatId), "","","Name: " +selectedPizza.get(chatId).getName(),"// Count: "+countPizza.get(chatId),localDateTime,Status.NEW.getRu()));
                try {

                    manager_bot.execute(setInlineOrder(745485034, "@" + update.getMessage().getChat().getUserName() +"("+update.getMessage().getChatId()+
                            ") Новый заказ\n" + "Name: " + selectedPizza.get(update.getMessage().getChatId().toString()).getName() +
                            " // Count: " + countPizza.get(update.getMessage().getChatId().toString())));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                try {
                    manager_bot.execute(setInlineOrder(922039243, "@" + update.getMessage().getChat().getUserName() +"("+update.getMessage().getChatId()+
                            ") Новый заказ\n" + "Name: " + selectedPizza.get(update.getMessage().getChatId().toString()).getName() +
                            " // Count: " + countPizza.get(update.getMessage().getChatId().toString())));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                Delivery_Service_Manager_bot.index++;

                break;
            case "en":
                Delivery_Service_Manager_bot.orderedPizza.put(Delivery_Service_Manager_bot.index, new OrderedPizza(Delivery_Service_Manager_bot.orderNumber,
                        userList.get(chatId), "","","Name: " +selectedPizza.get(chatId).getName(),"// Count: "+countPizza.get(chatId),localDateTime,Status.NEW.getEn()));
                try {
                    System.out.println("sending");
                    manager_bot.execute(setInlineOrder(745485034, "@" + update.getMessage().getChat().getUserName() +"("+update.getMessage().getChatId()+
                            ") New order \n" + "Name: " + selectedPizza.get(update.getMessage().getChatId().toString()).getName() +
                            " /// Count: " + countPizza.get(update.getMessage().getChatId().toString())));

                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                try {
                    manager_bot.execute(setInlineOrder(922039243, "@" + update.getMessage().getChat().getUserName() +"("+update.getMessage().getChatId()+
                            ") New Order\n" + "Name: " + selectedPizza.get(update.getMessage().getChatId().toString()).getName() +
                            " /// Count: " + countPizza.get(update.getMessage().getChatId().toString())));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                Delivery_Service_Manager_bot.index++;

                break;
        }

    }

    private void sentToManager(Update update, File file) {
        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println("sent ");
        Delivery_Service_Manager_bot manager_bot = new Delivery_Service_Manager_bot();
        switch (Delivery_Service_Manager_bot.activeLanguage) {
            case "uz":
                try {
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                    String chatId = update.getMessage().getChatId().toString();
                    String line;
                    while (null != (line = bufferedReader.readLine())) {
                        Delivery_Service_Manager_bot.orderedPizza.put(Delivery_Service_Manager_bot.index, new OrderedPizza(Delivery_Service_Manager_bot.orderNumber,
                                userList.get(chatId), "","",line.substring(0, line.indexOf("//")),line.substring(line.indexOf("//"),line.lastIndexOf("//")),
                                localDateTime,Status.NEW.getUz()));
                        try {
                            manager_bot.execute(setInlineOrder(922039243, "@" + update.getMessage().getChat().getUserName()
                                    +"("+update.getMessage().getChatId()+ ") dan Yangi buyurtma " + line.substring(0, line.lastIndexOf("//"))));
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                        try {
                            manager_bot.execute(setInlineOrder(745485034, "@" + update.getMessage().getChat().getUserName()
                                    +"("+update.getMessage().getChatId()+ ") dan Yangi buyurtma " + line.substring(0, line.lastIndexOf("//"))));
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                        Delivery_Service_Manager_bot.index++;

                    }
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "ru":
                try {
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                    String chatId = update.getMessage().getChatId().toString();
                    String line;
                    while (null != (line = bufferedReader.readLine())) {
                        Delivery_Service_Manager_bot.orderedPizza.put(Delivery_Service_Manager_bot.index, new OrderedPizza(Delivery_Service_Manager_bot.orderNumber,
                                userList.get(chatId), "","",line.substring(0, line.indexOf("//")),line.substring(line.indexOf("//"),line.lastIndexOf("//")),
                                localDateTime,Status.NEW.getUz()));
                        try {
                            manager_bot.execute(setInlineOrder(922039243, "Новый заказ от @" + update.getMessage().getChat().getUserName()
                                    +"("+update.getMessage().getChatId()+ ") " + line.substring(0, line.lastIndexOf("//"))));
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                        try {
                            manager_bot.execute(setInlineOrder(745485034, "Новый заказ от @" + update.getMessage().getChat().getUserName()
                                    +"("+update.getMessage().getChatId()+ ") " + line.substring(0, line.lastIndexOf("//"))));
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                        Delivery_Service_Manager_bot.index++;

                    }
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "en":
                try {
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                    String chatId = update.getMessage().getChatId().toString();
                    String line;
                    while (null != (line = bufferedReader.readLine())) {
                        Delivery_Service_Manager_bot.orderedPizza.put(Delivery_Service_Manager_bot.index, new OrderedPizza(Delivery_Service_Manager_bot.orderNumber,
                                userList.get(chatId), "","",line.substring(0, line.indexOf("//")),line.substring(line.indexOf("//"),line.lastIndexOf("//")),
                                localDateTime,Status.NEW.getUz()));
                        try {
                            manager_bot.execute(setInlineOrder(922039243, "New order from @" + update.getMessage().getChat().getUserName()
                                    +"("+update.getMessage().getChatId()+ ") " + line.substring(0, line.lastIndexOf("//"))));
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                        try {
                            manager_bot.execute(setInlineOrder(745485034, "New order from @" + update.getMessage().getChat().getUserName()
                                    +"("+update.getMessage().getChatId()+ ")  " + line.substring(0, line.lastIndexOf("//"))));
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                        Delivery_Service_Manager_bot.index++;

                    }
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }

    }

    private void cleanFile(Update update) {
        File file = new File("src/main/resources/" + update.getMessage().getChatId() + ".txt");
        Path path = file.toPath();
        try {
            Files.newBufferedWriter(path, StandardOpenOption.TRUNCATE_EXISTING);
            Files.newInputStream(path, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SendMessage setInlineOrder(long chatId, String text) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        switch (Delivery_Service_Manager_bot.activeLanguage) {
            case "uz":
                button1.setText(SelectLanguage.receive_uz);
                button1.setCallbackData("qabulQilish&"+Delivery_Service_Manager_bot.index);
                break;
            case "ru":
                button1.setText(SelectLanguage.receive_ru);
                button1.setCallbackData("Принять&"+Delivery_Service_Manager_bot.index);
                break;
            case "en":
                button1.setText(SelectLanguage.receive_en);
                button1.setCallbackData("Receive&"+Delivery_Service_Manager_bot.index);
                break;
        }

        List<InlineKeyboardButton> buttonList = new ArrayList<>();
        buttonList.add(button1);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(buttonList);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return new SendMessage().setText(text).setChatId(chatId).setReplyMarkup(inlineKeyboardMarkup);
    }

    private void orderDeleteButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        switch (activeLanguage) {
            case "uz":
                row1.add(new KeyboardButton(Language.ORDER_PIZZA.getUzb()));
                row1.add(new KeyboardButton(Language.DELETE_ORDER.getUzb()));
                row2.add(new KeyboardButton(Language.BACK_BUTTON.getUzb()));
                break;
            case "ru":
                row1.add(new KeyboardButton(Language.ORDER_PIZZA.getRus()));
                row1.add(new KeyboardButton(Language.DELETE_ORDER.getRus()));
                row2.add(new KeyboardButton(Language.BACK_BUTTON.getUzb()));
                break;
            case "en":
                row1.add(new KeyboardButton(Language.ORDER_PIZZA.getEng()));
                row1.add(new KeyboardButton(Language.DELETE_ORDER.getEng()));
                row2.add(new KeyboardButton(Language.BACK_BUTTON.getUzb()));
                break;
        }
        keyboardRowList.add(row1);
        keyboardRowList.add(row2);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
    }

    private void addOrderPizza(Update update) throws IOException {
        File file = new File("src/main/resources/" + update.getMessage().getChatId() + ".txt");
        if (!file.exists()) {
            file.createNewFile();
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
                bufferedWriter.write("Name: " +
                        selectedPizza.get(update.getMessage().getChatId().toString()).getName() + " // Count:" +
                        countPizza.get(update.getMessage().getChatId().toString()) + " // Payment: " +
                        selectedPizza.get(update.getMessage().getChatId().toString()).getCost() *
                                countPizza.get(update.getMessage().getChatId().toString()) +
                        System.getProperty("line.separator"));
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true));
                bufferedWriter.write("Name: " +
                        selectedPizza.get(update.getMessage().getChatId().toString()).getName() + " // Count:" +
                        countPizza.get(update.getMessage().getChatId().toString()) + " // Payment: " +
                        selectedPizza.get(update.getMessage().getChatId().toString()).getCost() *
                                countPizza.get(update.getMessage().getChatId().toString()) +
                        System.getProperty("line.separator"));
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public static void addToShoppingBoxButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        switch (activeLanguage) {
            case "uz":
                row1.add(new KeyboardButton(Language.ADD_TO_BASKET.getUzb()));
                row1.add(new KeyboardButton(SelectLanguage.order_through_uz));
                row2.add(new KeyboardButton(Language.BACK_BUTTON.getUzb()));
                break;
            case "ru":
                row1.add(new KeyboardButton(Language.ADD_TO_BASKET.getRus()));
                row1.add(new KeyboardButton(SelectLanguage.order_through_ru));
                row2.add(new KeyboardButton(Language.BACK_BUTTON.getUzb()));
                break;
            case "en":
                row1.add(new KeyboardButton(Language.ADD_TO_BASKET.getEng()));
                row1.add(new KeyboardButton(SelectLanguage.order_through_en));
                row2.add(new KeyboardButton(Language.BACK_BUTTON.getUzb()));
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

    public static void backButton(SendMessage sendMessage, String activeLanguage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        switch (activeLanguage) {
            case "uz":
                row1.add(new KeyboardButton(Language.BACK_BUTTON.getUzb()));
                break;
            case "ru":
                row1.add(new KeyboardButton(Language.BACK_BUTTON.getRus()));
                break;
            case "en":
                row1.add(new KeyboardButton(Language.BACK_BUTTON.getEng()));
                break;
        }
        keyboardRowList.add(row1);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
    }

    public static void setPizza() {

        pizzas.add(new Pizza("Neapolitan Pizza", 50000, " ", 50));
        pizzas.add(new Pizza("Chicago Pizza", 45000, " ", 50));
        pizzas.add(new Pizza("New York-Style Pizza", 60000, " ", 50));
        pizzas.add(new Pizza("Sicilian Pizza", 40000, " ", 50));
        pizzas.add(new Pizza("Greek Pizza", 42000, " ", 50));
        for (Pizza pizza : pizzas) {
            PizzaTypes += "/" + pizza.getName() + "\n" + pizza.getCost() + "\n" + "---------------\n";
        }

    }

    private void finishRegister(Update update) {
        System.out.println("register successfully");
        userList.put(update.getMessage().getChatId().toString(),
                new User(update.getMessage().getChatId()
                        ,registerName.get(update.getMessage().getChatId().toString()),
                        registerAddress.get(update.getMessage().getChatId().toString()),
                        registerPhoneNumber.get(update.getMessage().getChatId().toString()),
                        registerCash.get(update.getMessage().getChatId().toString())));

    }

    public static void setButtonForLanguageOfBot(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton(Language.LANGUAGE.getEng()));
        row1.add(new KeyboardButton(Language.LANGUAGE.getRus()));
        row1.add(new KeyboardButton(Language.LANGUAGE.getUzb()));

        keyboardRowList.add(row1);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
    }

    public static void setMainMenu(SendMessage sendMessage, String activeLanguage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();
        KeyboardRow row4 = new KeyboardRow();
        switch (activeLanguage) {
            case "uz":
                row1.add(new KeyboardButton(Language.SELECT_PIZZA.getUzb()));
                row2.add(new KeyboardButton(Language.ORDER_BOX.getUzb()));
                row3.add(new KeyboardButton(Language.CONTACT.getUzb()));
                row4.add(new KeyboardButton(Language.SETTINGS.getUzb()));
                break;
            case "ru":
                row1.add(new KeyboardButton(Language.SELECT_PIZZA.getRus()));
                row2.add(new KeyboardButton(Language.ORDER_BOX.getRus()));
                row3.add(new KeyboardButton(Language.CONTACT.getRus()));
                row4.add(new KeyboardButton(Language.SETTINGS.getRus()));
                break;
            case "en":
                row1.add(new KeyboardButton(Language.SELECT_PIZZA.getEng()));
                row2.add(new KeyboardButton(Language.ORDER_BOX.getEng()));
                row3.add(new KeyboardButton(Language.CONTACT.getEng()));
                row4.add(new KeyboardButton(Language.SETTINGS.getEng()));
                break;
        }
        keyboardRowList.add(row1);
        keyboardRowList.add(row2);
        keyboardRowList.add(row3);
        keyboardRowList.add(row4);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
    }

    @Override
    public String getBotUsername() {
        return "http://t.me/MasterDelivery_BOT";
    }

    @Override
    public String getBotToken() {
        return "1739773282:AAGhUDQATJWriZV4VE6yziXH-5HQHMqv8ko";
    }
}
