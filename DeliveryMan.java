package module3.lesson10_TelegramBot.ExtraTask;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryMan {
    private String chatId;
    private String id;
    private String name;
    private String login;
    private String password;

}
