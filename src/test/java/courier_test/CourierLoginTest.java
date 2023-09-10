package courier_test;

import io.restassured.response.ValidatableResponse;
import io.qameta.allure.junit4.DisplayName;
import courier.Courier;
import courier.CourierAction;
import courier.CourierCredentials;
import courier.CourierGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;

public class CourierLoginTest {

    private CourierAction courierStep;
    private Courier courier;
    private Integer courierId;
    private final Courier notExistCourier = CourierGenerator.getRandom();

    @Before
    public void setUp() {
        courierStep = new CourierAction();
        courier = CourierGenerator.getRandom();
        courierStep.create(courier);
    }

    @After
    public void cleanUp() {
        if (courierId != null) {
            courierStep.deleteCourier(courierId);
        }
    }

    @Test
    @DisplayName("Успешная авторизация")
    public void successLoginCourierTest(){
        ValidatableResponse loginResponse = courierStep.login(CourierCredentials.from(courier));
        courierId = loginResponse.extract().path("id");
        loginResponse
                .statusCode(200)
                .assertThat()
                .body("id",is(notNullValue()));
    }

    @Test
    @DisplayName("Авторизации без логина")
    public void loginCourierWithoutLoginTest(){
        courierId = courierStep.login(CourierCredentials.from(courier)).extract().path("id");
        ValidatableResponse loginResponse = courierStep.login(CourierCredentials.from(courier.setLogin("")));
        loginResponse
                .statusCode(400)
                .assertThat()
                .body("code",equalTo(400))
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизации без пароля")
    public void loginCourierWithoutPasswordTest(){
        courierId = courierStep.login(CourierCredentials.from(courier)).extract().path("id");
        ValidatableResponse loginResponse = courierStep.login(CourierCredentials.from(courier.setPassword("")));
        loginResponse
                .statusCode(400)
                .assertThat()
                .body("code",equalTo(400))
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация курьера c некорректным логином")
    public void loginWithIncorrectLoginTest() {
        courierId = courierStep.login(CourierCredentials.from(courier)).extract().path("id");
        ValidatableResponse loginResponse = courierStep.login(CourierCredentials.from(courier.setLogin("INCORRECT")));
        loginResponse
                .statusCode(404)
                .assertThat()
                .body("code",equalTo(404))
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Авторизация курьера c некорректным паролем")
    public void loginWithIncorrectPasswordTest() {
        courierId = courierStep.login(CourierCredentials.from(courier)).extract().path("id");
        ValidatableResponse loginResponse = courierStep.login(CourierCredentials.from(courier.setPassword("INCORRECT")));
        loginResponse
                .statusCode(404)
                .assertThat()
                .body("code",equalTo(404))
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Авторизация несуществующего курьера")
    public void loginUnknownCourierTest() {
        courierId = courierStep.login(CourierCredentials.from(courier)).extract().path("id");
        ValidatableResponse loginResponse = courierStep.login(CourierCredentials.from(notExistCourier));
        loginResponse
                .statusCode(404)
                .assertThat()
                .body("code", equalTo(404))
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
    }

}