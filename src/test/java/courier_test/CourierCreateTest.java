package courier_test;

import io.restassured.response.ValidatableResponse;
import io.qameta.allure.junit4.DisplayName;
import courier.Courier;
import courier.CourierGenerator;
import courier.CourierCredentials;
import courier.CourierAction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class CourierCreateTest {

    private CourierAction courierStep;
    private Courier courier;
    private Integer courierId;

    @Before
    public void setUp() {
        courierStep = new CourierAction();
        courier = CourierGenerator.getRandom();
    }

    @After
    public void cleanUp() {
        if (courierId != null) {
            courierStep.deleteCourier(courierId);
        }
    }

    @Test
    @DisplayName("Cоздание курьера с валидными данными")
    public void courierCanBeCreatedTest(){
        ValidatableResponse createResponse = courierStep.create(courier);
        ValidatableResponse loginResponse = courierStep.login(CourierCredentials.from(courier));
        courierId = loginResponse.extract().path("id");
        createResponse
                .statusCode(201)
                .assertThat()
                .body("ok", is(true));
    }

    @Test
    @DisplayName("Cоздание курьера без логина")
    public void createCourierWithoutLoginTest(){
        courier.setLogin(null);
        ValidatableResponse createResponse = courierStep.create(courier);
        createResponse
                .statusCode(400)
                .assertThat()
                .body("message",equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Cоздание курьера без пароля")
    public void createCourierWithoutPasswordTest(){
        courier.setPassword(null);
        ValidatableResponse createResponse = courierStep.create(courier);
        createResponse
                .statusCode(400)
                .assertThat()
                .body("message",equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Cоздание курьера с существующим логином")
    public void createDuplicateCourierTest(){
        ValidatableResponse createResponseFirst = courierStep.create(courier);
        ValidatableResponse createResponseSecond = courierStep.create(courier);
        ValidatableResponse loginResponse = courierStep.login(CourierCredentials.from(courier));
        courierId = loginResponse.extract().path("id");
        createResponseSecond
                .statusCode(409)
                .assertThat()
                .body("code",equalTo(409))
                .and()
                .body("message",equalTo("Этот логин уже используется"));
    }

}