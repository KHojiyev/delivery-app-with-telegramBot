package module3.lesson10_TelegramBot.ExtraTask;

public enum Status {

    NEW("Yangi buyurtma","Новый заказ","New Order"),
    RECEIVED("Qabul qilindi","Принялься","Received"),
    COOKING("Pishirilyapti","Готовиться","Cooking"),
    READYFORSEND("Jonatishga tayyor","Готов для отправки","Ready for sending"),
    SENDINGTOCUSTOMER("Mijozga yetkazilyapti","Отправка заказчику","Shipping to customer"),
    DELIVERED("Yetkazildi","Доставилься","Delivered");

    private String uz;
    private String ru;
    private String en;

    Status(String uz, String ru, String en) {
        this.uz = uz;
        this.ru = ru;
        this.en = en;
    }

    public void setUz(String uz) {
        this.uz = uz;
    }

    public void setRu(String ru) {
        this.ru = ru;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getUz() {
        return uz;
    }

    public String getRu() {
        return ru;
    }

    public String getEn() {
        return en;
    }
}
