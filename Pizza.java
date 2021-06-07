package module3.lesson10_TelegramBot.ExtraTask;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pizza {
    private String name;
    private double cost;
    private String cookingOfWhat;
    private int quantity;

}
