package api;
import models.CredentialsModel;
import models.LoginResponseModel;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static specs.ApiSpecs.request;

public class AuthorizationApi {
    public LoginResponseModel login(CredentialsModel credentials){
        return given(request)
                .body(credentials)
                .contentType(JSON)
                .when()
                .post("/Account/v1/Login")
                .then()
                .statusCode(200)
                .extract().as(LoginResponseModel.class);
    }
}
