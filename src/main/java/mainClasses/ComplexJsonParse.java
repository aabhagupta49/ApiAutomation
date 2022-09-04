package mainClasses;

import files.Payload;
import files.ReusableMethods;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.*;

public class ComplexJsonParse {

    JsonPath js = ReusableMethods.rawToJson(Payload.CourseContent());
    int sum = 0;
    int rpaCopies = 0;

    @Test
    public void getCourseSize() {
        System.out.println("Print No of courses returned by API");
        System.out.println(js.getInt("courses.size()"));
    }

    @Test
    public void printPurchaseAmount() {
        System.out.println("Print Purchase Amount");
        System.out.println(js.getInt("dashboard.purchaseAmount"));
    }

    @Test
    public void printFirstCourseTitle() {
        System.out.println("Print Title of the first course ");
        System.out.println(js.getString("courses.title[0]"));
    }

    @Test
    public void printCourseTitlesAndPrices() {
        System.out.println("Print All course titles and their respective Prices");
        for (int i=0; i<js.getInt("courses.size()"); i++) {
            System.out.println(js.getString("courses.title["+i+"]") + " " + js.getInt("courses.price["+i+"]"));
        }
    }

    @Test
    public void printRpaCopies() {
        System.out.println("Print no of copies sold by RPA Course");
        for (int i=0; i<js.getInt("courses.size()"); i++) {
            if (js.getString("courses.title["+i+"]").equals("RPA")) {
                rpaCopies = js.getInt("courses.copies[" + i + "]");
                System.out.println(rpaCopies);
                break;
            }
        }
    }

    @Test
    public void verifySumOfCourses() {
        System.out.println("Verify if Sum of all Course prices matches with Purchase Amount");
        for (int i=0; i<js.getInt("courses.size()"); i++) {
            sum += js.getInt("courses["+i+"].price") * js.getInt("courses["+i+"].copies");
        }
        System.out.println("Sum: " + sum);
        Assert.assertEquals(sum, js.getInt("dashboard.purchaseAmount"));
    }
}
