package mainClasses;

import files.ReusableMethods;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;

import java.text.ParseException;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class Basics {
    public static void main(String args[]) {
        baseURI = "http://rahulshettyacademy.com";

        String payload = "{\n" +
                "  \"location\": {\n" +
                "    \"lat\": -231,\n" +
                "    \"lng\": 33.427362\n" +
                "  },\n" +
                "  \"accuracy\": 50,\n" +
                "  \"name\": \"Frontline house\",\n" +
                "  \"phone_number\": \"(+91) 983 893 3937\",\n" +
                "  \"address\": \"29, side layout, cohen 09\",\n" +
                "  \"types\": [\n" +
                "    \"shoe park\",\n" +
                "    \"shop\"\n" +
                "  ],\n" +
                "  \"website\": \"http://rahulshettyacademy.c\",\n" +
                "  \"language\": \"French-IN\"\n" +
                "}";

        //POST
        String postResponse = given().queryParam("key", "qaclick123").header("Content-Type", "application/json")
                .body(payload)
                .when().post("/maps/api/place/add/json")
                .then().assertThat().statusCode(200).body("status", equalTo("OK"))
                .header("Content-Type", equalTo("application/json;charset=UTF-8")).extract().response().asString();

        JsonPath json = ReusableMethods.rawToJson(postResponse);
        String place_id = json.getString("place_id");
        System.out.println(place_id);

        // PUT
        String updatedAddress = "711 winter walk, USA";
        given().queryParam("place_id", place_id).queryParam("key", "qaclick123")
                .body("{\n" +
                        "\"place_id\":\""+place_id+"\",\n" +
                        "\"address\":\""+updatedAddress+"\",\n" +
                        "\"name\": \"Frontline1 house\",\n" +
                        "\"key\":\"qaclick123\"\n" +
                        "}")
                .when().put("/maps/api/place/update/json")
                .then().log().all().assertThat().statusCode(200).body("msg", equalTo("Address successfully updated"));

        // GET
        String getResponse = given().queryParam("place_id", place_id).queryParam("key", "qaclick123")
                .when().get("/maps/api/place/get/json")
                .then().log().all().assertThat().statusCode(200).extract().response().asString();
        JsonPath getJson = ReusableMethods.rawToJson(getResponse);
        Assert.assertEquals(getJson.getString("address"), updatedAddress);


        // DELETE
        given().queryParam("key", "qaclick123")
                .body("{\n" +
                        "    \"place_id\":\""+place_id+"\"\n" +
                        "}")
                .when().delete("/maps/api/place/delete/json")
                .then().log().all().assertThat().statusCode(200).body("status", equalTo("OK"));

        // GET
        given().queryParam("place_id", place_id).queryParam("key", "qaclick123")
                .when().get("/maps/api/place/get/json")
                .then().log().all().assertThat().statusCode(404).body("msg", equalTo("Get operation failed, looks like place_id  doesn't exists"));

    }
}
