package files;

import io.restassured.path.json.JsonPath;

public class ReusableMethods {
    public static JsonPath rawToJson(String response) {
        JsonPath json = new JsonPath(response);
        return json;
    }
}