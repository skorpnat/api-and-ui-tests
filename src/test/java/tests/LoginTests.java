package tests;

import models.LoginResponseModel;
import org.junit.jupiter.api.Test;
import io.restassured.response.Response;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static tests.TestData.*;

import org.openqa.selenium.Cookie;


public class LoginTests extends TestBase {
    @Test
    void successfulLoginWithUiTest()  {
        open("/login");
        $("#userName").setValue(login);
        $("#password").setValue(password).pressEnter();
        $("#userName-value").shouldHave(text(login));
    }

    @Test
    void successfulLoginWithApiTest() {
        String authData = "{\"userName\":\"nnk\",\"password\":\"N#123%Jk6\"}";

        Response authResponse = given()
                .log().uri()
                .log().method()
                .log().body()
                .contentType(JSON)
                .body(authData)
                .when()
                .post("/Account/v1/Login")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().response();

        open("/favicon.ico");
        getWebDriver().manage().addCookie(new Cookie("userID", authResponse.path("userId")));
        getWebDriver().manage().addCookie(new Cookie("expires", authResponse.path("expires")));
        getWebDriver().manage().addCookie(new Cookie("token", authResponse.path("token")));

        open("/profile");
        $("#userName-value").shouldHave(text(login));
    }

    @Test
    void successfulLoginTest() {
        LoginResponseModel loginResponse = authorizationApi.login(credentials);

        open("/favicon.ico");
        getWebDriver().manage().addCookie(new Cookie("userID", loginResponse.getUserId()));
        getWebDriver().manage().addCookie(new Cookie("token", loginResponse.getToken()));
        getWebDriver().manage().addCookie(new Cookie("expires", loginResponse.getExpires()));

        open("/profile");
        $("#userName-value").shouldHave(text(credentials.getUserName()));

    }
}
