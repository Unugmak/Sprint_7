package order_test;

import order.Order;
import org.junit.Before;
import order.OrderAction;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;

import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    private OrderAction orderStep;
    private final String[] color;
    public CreateOrderTest(String[] color) {
        this.color = color;
    }

    @Parameterized.Parameters(name= "color = {0}")
    public static Object[][] colorData() {
        Object[][] objects;
        objects = new Object[][]{
                {new String[]{"GREY"}},
                {new String[]{"BLACK"}},
                {new String[]{"GREY", "BLACK"}},
                {null},
        };
        return objects;
    }

    @Before
    public void setUp(){
        orderStep = new OrderAction();
    }

    @Test
    @DisplayName("Создание заказа")
    public void checkCreateOrderTest() {
        Order order = new Order("Михаил", "Казаков", "Малинина, 30", "Партизанская",
                "77777777777", 3, "2023-10-12", "Кайфы!", color);
        ValidatableResponse response = orderStep.createOrder(order);
        response
                .assertThat()
                .body("track", notNullValue())
                .and()
                .statusCode(201);
    }

}