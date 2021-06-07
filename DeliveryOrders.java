package module3.lesson10_TelegramBot.ExtraTask;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryOrders {
    private Long ManagerChatId;
    private String orderPizzaType;
    private String count;
    private String orderStatus;
}
