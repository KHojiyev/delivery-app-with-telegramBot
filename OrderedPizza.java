package module3.lesson10_TelegramBot.ExtraTask;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderedPizza {
    private Long orderId;
    private User user;
    private String ManagerChatId;
    private String DeliveryManChatId;
    private String pizzaType;
    private String count;
    private LocalDateTime localDateTime;
    private String status;


}
