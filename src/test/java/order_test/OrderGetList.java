package order_test;

import order.OrderAction;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.notNullValue;

public class OrderGetList {

    private OrderAction orderStep;

    @Before
    public void setUp(){
        orderStep = new OrderAction();
    }

    @Test
    @DisplayName("Получение списка заказов")
    public void getListOrdersTest() {
        ValidatableResponse response = orderStep.getListOrders();
        response
                .assertThat()
                .body("orders", notNullValue())
                .and()
                .statusCode(200);
    }
}