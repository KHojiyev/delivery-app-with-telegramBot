package module3.lesson10_TelegramBot.ExtraTask;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    private Long userChatId;
    private String name;
    private String address;
    private String phoneNumber;
    private double cash;

}
