package GoRest;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class GoRestUsersTests {
    Faker randomUretici = new Faker();
    int userID;

    RequestSpecification reqSpec;

    @BeforeClass
    public void setup() {

        baseURI = "https://gorest.co.in/public/v2/users";  // baseUri RequestSpecification den önce tanımlanmalı.
        //baseURI ="https://test.gorest.co.in/public/v2/users/";

        reqSpec = new RequestSpecBuilder()
                .addHeader("Authorization", "Bearer e4b22047188da067d3bd95431d94259f63896347f9864894a0a7013ee5f9c703")
                .setContentType(ContentType.JSON)
                .build();
    }

    @Test(enabled = false)
    public void createUserJson() {
        // POST https://gorest.co.in/public/v2/users
        // "Authorization: Bearer 523891d26e103bab0089022d20f1820be2999a7ad693304f560132559a2a152d"
        // {"name":"{{$randomFullName}}", "gender":"male", "email":"{{$randomEmail}}", "status":"active"}
        String rndFullname = randomUretici.name().fullName(); // FAKE sonrası bu kütüphane dilini kullanabildim.
        String rndEmail = randomUretici.internet().emailAddress();

        userID =
                given()
                        .spec(reqSpec)
                        .body("{\"name\":\"" + rndFullname + "\", \"gender\":\"male\", \"email\":\"" + rndEmail + "\", \"status\":\"active\"}")
                        //.log().uri()
                        //.log().body()

                .when()
                        .post("") // BaseURI den ötürü boş bıraktım. Yukarıda otomatik çalışıyor.

                .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id");
    }

    @Test
    public void createUserMap() {

        System.out.println("baseURI = " + baseURI);
        String rndFullname = randomUretici.name().fullName();
        String rndEmail = randomUretici.internet().emailAddress();

        Map<String, String> newUser = new HashMap<>();
        newUser.put("name", rndFullname);
        newUser.put("gender", "male");
        newUser.put("email", rndEmail);
        newUser.put("status", "active");

        userID =
                given()
                        .header("Authorization", "Bearer e4b22047188da067d3bd95431d94259f63896347f9864894a0a7013ee5f9c703")
                        .contentType(ContentType.JSON)
                        .body(newUser)
                        .log().uri()
                        //.log().body()

                .when()
                        .post("") // BaseURI den ötürü boş bıraktım. Yukarıda otomatik çalışıyor.

                .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id");
    }

    @Test(enabled = false)
    public void createUserClass() {
        String rndFullname = randomUretici.name().fullName();
        String rndEmail = randomUretici.internet().emailAddress();

        User newUser = new User();
        newUser.name = rndFullname;
        newUser.gender = "male";
        newUser.email = rndEmail;
        newUser.status = "active";

        userID =
                given()
                        .spec(reqSpec)
                        .body(newUser)
                        //.log().uri()
                        //.log().body()

                .when()
                        .post("") // BaseURI den ötürü boş bıraktım. Yukarıda otomatik çalışıyor.

                .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id");
    }

    @Test(dependsOnMethods = "createUserMap")
    public void getUserByID() {

        given()
                .spec(reqSpec)

        .when()
                .get("" + userID) // BaseURI den ötürü boş bıraktım. Yukarıda otomatik çalışıyor.

        .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", equalTo(userID))
        ;
    }

    @Test(dependsOnMethods = "getUserByID")
    public void updateUser() {

        Map<String, String> updateUser = new HashMap<>();
        updateUser.put("name", "ömer faruk");

        given()
                .spec(reqSpec)
                .body(updateUser)
                .log().uri()

        .when()
                .put("" + userID)

        .then()
                .log().body()
                .statusCode(200)
                .body("id", equalTo(userID))
                .body("name", equalTo("ömer faruk"))
        ;
    }

    //TODO
    @Test(dependsOnMethods = "updateUser")
    public void deleteUser() {

        given()
                .spec(reqSpec)
        .when()
                .delete(""+userID)

        .then()
                .log().all()
                .statusCode(204)
        ;
    }

    //TODO
    @Test(dependsOnMethods = "deleteUser")
    public void deleteUserNegative() {
        given()
                .spec(reqSpec)
        .when()
                .delete(""+userID)

        .then()
                .log().all()
                .statusCode(404);
    }

     // TODO :HaftaSonu TODO:  GoRest de daha önce yaptığınız posts ve comments resourcelarını
     // TODO  API Automation yapınız(Create,get,update,delete,deletenegatife)
}
