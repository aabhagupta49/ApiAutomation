package mainClasses;

import files.Payload;
import files.ReusableMethods;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

import io.restassured.path.json.JsonPath;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DynamicJson {

    JsonPath jsonResponse;

    @DataProvider(name="BookData")
    public Object[][] getBookData() {
        return new Object[][] {{"Book1", "111"}, {"Book2", "112"}, {"Book3", "122"}};
    }

    @Test (dataProvider = "BookData")
    public void addBook(String isbn, String aisle) {
        RestAssured.baseURI = "http://216.10.245.166";
        String postResponse = given().header("Content-Type", "application/json")
                .body(Payload.addBook(aisle, isbn))
                .when().post("/Library/Addbook.php")
                .then().log().all().assertThat().statusCode(200)
                .extract().response().asString();

        jsonResponse = ReusableMethods.rawToJson(postResponse);
    }

    @Test
    public void getBookByAuthorName() {

    }

    @Test
    public void getBookById() {

    }

    @Test (priority = 1, dataProvider = "BookData")
    public void deleteBook(String isbn, String aisle) {
        baseURI = "http://216.10.245.166";
        given().log().all().header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"ID\": \""+isbn + aisle+"\"\n" +
                        "}")
                .when().delete("/Library/DeleteBook.php")
                .then().log().all().assertThat().statusCode(200)
                .body("msg", equalTo("book is successfully deleted"));
    }

    @Test (priority = 2, dataProvider = "BookData")
    public void deleteAlreadyDeletedBook(String isbn, String aisle) {
        baseURI = "http://216.10.245.166";
        given().log().all().header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"ID\": \""+isbn + aisle+"\"\n" +
                        "}")
                .when().delete("/Library/DeleteBook.php")
                .then().log().all().assertThat().statusCode(404)
                .body("msg", equalTo("Delete Book operation failed, looks like the book doesnt exists"));
    }

}
