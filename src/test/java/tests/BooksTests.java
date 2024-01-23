package tests;

import api.BooksApi;
import models.AddBooksListModel;
import models.DeleteRequestModel;
import models.IsbnModel;
import models.LoginResponseModel;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import java.util.ArrayList;
import java.util.List;

import static api.BooksApi.*;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static tests.TestData.credentials;


public class BooksTests extends TestBase {
    @Test
    void checkDeleteOneBook() {
        LoginResponseModel loginResponse = authorizationApi.login(credentials);

        step("Очищаем список книг через API в Profile", () ->
                cleanAllBooks(loginResponse.getToken(), loginResponse.getUserId())
        );

        step("Добавляем книгу через API в Profile", () -> {
            IsbnModel newIsbn = new IsbnModel();
            newIsbn.setIsbn("9781449331818");
            List<IsbnModel> isbnList = new ArrayList<>();
            isbnList.add(newIsbn);
            AddBooksListModel addNewBook = new AddBooksListModel();
            addNewBook.setUserId(loginResponse.getUserId());
            addNewBook.setCollectionOfIsbns(isbnList);

            addNewBookToProfile(loginResponse.getToken(), addNewBook);
        });

        step("Проверяем имя пользователя и название добавленной книги на UI", () -> {

            open("/favicon.ico");
            getWebDriver().manage().addCookie(new Cookie("userID", loginResponse.getUserId()));
            getWebDriver().manage().addCookie(new Cookie("expires", loginResponse.getExpires()));
            getWebDriver().manage().addCookie(new Cookie("token", loginResponse.getToken()));

            open("/profile");
            assertThat($("#userName-value").getText()).isEqualTo(credentials.getUserName());
            assertThat($("[id='see-book-Learning JavaScript Design Patterns']").getText()).isEqualTo("Learning JavaScript Design Patterns");
        });

        step("Удаляем добавленную книгу через API", () -> {

            DeleteRequestModel deleteBook = new DeleteRequestModel();
            deleteBook.setIsbn("9781449331818");
            deleteBook.setUserId(loginResponse.getUserId());

            deleteOneBook(loginResponse.getToken(), deleteBook);
        });

        step("Проверяем на UI отсутствие добавленной книги в профайле", () -> {
            open("/profile");
            assertThat($(".rt-noData").getText()).isEqualTo("No rows found");
        });
    }
}
