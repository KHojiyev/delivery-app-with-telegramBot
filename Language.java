package module3.lesson10_TelegramBot.ExtraTask;

public enum Language {
    LANGUAGE("O'zbekcha","На Русском Языке","In English"),
    NAME("Ismingiz: ","Ваше имя: ","Your name: "),
    FIRST_ENTER("Keling tanishib olamiz:","Давайте познакомимся ","Let's introduce "),
    ADDRESS("Yashash tumaningiz?","Ваш аддресс","Your Address?"),
    PHONE_NUMBER("Telefon raqamingiz?","Ваш контактний номер?","Your phone number?"),
    CASH("Boshlang'ich mablag' qo'shing","Добавьте первоначално кеш","Add cash first of all"),
    FINISH_REGISTER("Muvaffaqqiyatli qo'shildingiz!","Регистровали успешно!!","Succesfully added"),
    SELECT_PIZZA("Pitsa tanlash","Вибрать свою пиццу","Select pizza"),
    ORDER_BOX("Buyurtma savatchasi","Корзинка заказов","Order basket"),
    CONTACT("Opetator bilan aloqa","Связаться с операторам","Call to operator"),
    SETTINGS("Sozlamalar","Настройки","Settings"),
    INFO("Ma'lumotlar","Информацы","Information"),
    SELECT("Tanlang","Выберите","Choice please"),
    BACK_BUTTON("Orqaga","Назад","Back"),
    ADD_TO_BASKET("Xarid savatchaga qo'shish","Добавить корзинку","Add to shopping box"),
    ADDED_TO_BASKET("Savatchaga qo'shildi\uD83D\uDC4D","Добавился в корзинку\uD83D\uDC4D","Added to basket\uD83D\uDC4D"),
    COUNT_PIZZA("Nechta?","Сколько?","How many?"),
    ORDER_PIZZA("Buyurtma qilish","Заказать","Order"),
    ORDER_PIZZA_THROUGH("Buyurtma berish","Заказивать","Order to"),
    DELETE_ORDER("O'chirish","Отменить","Delete"),
    ORDER_SUCCESSFULLY("Buyurtmangiz jo'natildi ","Ваш заказ отправлен","Your order was sent"),
    DELETED_SUCCESSFULLY("Buyurtmangiz o'chirildi ","Ваш заказ отменился","Your order was deleted");


    public String Uzb;
    public String Rus;
    public String Eng;

    Language(String uzb, String rus, String eng) {
        Uzb = uzb;
        Rus = rus;
        Eng = eng;
    }

    public String getUzb() {
        return Uzb;
    }

    public void setUzb(String uzb) {
        Uzb = uzb;
    }

    public String getRus() {
        return Rus;
    }

    public void setRus(String rus) {
        Rus = rus;
    }

    public String getEng() {
        return Eng;
    }

    public void setEng(String eng) {
        Eng = eng;
    }
}
