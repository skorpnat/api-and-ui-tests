package api;

import models.AddBooksListModel;
import models.DeleteRequestModel;

import static io.restassured.RestAssured.given;
import static specs.ApiSpecs.*;

public class BooksApi {

    public static void cleanAllBooks (String token, String userId) {
        given(request)
                .header("Authorization", "Bearer " + token)
                .queryParam("UserId", userId)
                .when()
                .delete("/BookStore/v1/Books")
                .then()
                .log().all()
                .spec(response204);
    }
    public static void deleteOneBook (String token, DeleteRequestModel deleteBook) {
        given(request)
                .header("Authorization", "Bearer " + token)
                .body(deleteBook)
                .when()
                .delete("/BookStore/v1/Book")
                .then()
                .log().all()
                .spec(response204);

    }

    public static void addNewBookToProfile (String token, AddBooksListModel addNewBook) {
        given(request)
                .header("Authorization", "Bearer " + token)
                .body(addNewBook)
                .when()
                .post("/BookStore/v1/Books")
                .then()
                .spec(response201);

    }

}
