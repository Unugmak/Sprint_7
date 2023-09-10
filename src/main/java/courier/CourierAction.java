package courier;

import io.restassured.response.ValidatableResponse;
import io.qameta.allure.Step;
import client.Client;

import static io.restassured.RestAssured.given;

public class CourierAction extends Client {

    private static final String COURIER_PATH = "/api/v1/courier";
    private static final String COURIER_LOGIN_PATH = "/api/v1/courier/login";

    @Step("Создание курьера")
    public ValidatableResponse create(Courier courier) {
        return given()
                .spec(getBaseSpec())
                .body(courier)
                .when()
                .post(COURIER_PATH)
                .then();
    }
    @Step("Логин курьера")
    public ValidatableResponse login(CourierCredentials credentials) {
        return given()
                .spec(getBaseSpec())
                .body(credentials)
                .when()
                .post(COURIER_LOGIN_PATH)
                .then();
    }

    @Step("Удаление курьера")
    public ValidatableResponse deleteCourier (int courierId) {
        return given()
                .spec(getBaseSpec())
                .delete(COURIER_PATH + courierId)
                .then();
    }

}