package Pages;

import Utils.Utils;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.commons.configuration.ConfigurationException;
import org.testng.Assert;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.given;

public class User {
    Properties properties = new Properties();
    FileInputStream file = new FileInputStream("./src/test/resources/config.properties");

    public User() throws FileNotFoundException {
    }

    public void callingLoginApi() throws IOException, ConfigurationException {
        properties.load(file);
        RestAssured.baseURI = properties.getProperty("baseURL");

        Response response =
                given()
                        .contentType("application/json")
                        .body("{\n" +
                                "    \"email\":\"salmansrabon@gmail.com\",\n" +
                                "    \"password\":\"1234\"\n" +
                                "}")
                .when()
                        .post("/user/login")
                .then().statusCode(200).extract().response();

        JsonPath responseObj = response.jsonPath();
        String token = responseObj.get("token");
        Utils.setEnvironment("token", token);
    }

    public void callingLoginApiUsingInvEmail() throws IOException {
        properties.load(file);
        RestAssured.baseURI = properties.getProperty("baseURL");

        Response response =
                given()
                        .contentType("application/json")
                        .body("{\n" +
                                "    \"email\":\"salmansrabon123@gmail.com\",\n" +
                                "    \"password\":\"1234\"\n" +
                                "}")
                        .when()
                        .post("/user/login")
                        .then().statusCode(404).extract().response();

        JsonPath responseObj = response.jsonPath();
        String message = responseObj.get("message");
        Assert.assertEquals(message, "User not found");
    }

    public void callingLoginApiUsingIncPass() throws IOException {
        properties.load(file);
        RestAssured.baseURI = properties.getProperty("baseURL");

        Response response =
                given()
                        .contentType("application/json")
                        .body("{\n" +
                                "    \"email\":\"salmansrabon@gmail.com\",\n" +
                                "    \"password\":\"12345678914\"\n" +
                                "}")
                        .when()
                        .post("/user/login")
                        .then().statusCode(401).extract().response();

        JsonPath responseObj = response.jsonPath();
        String message = responseObj.get("message");
        Assert.assertEquals(message, "Password incorrect");
    }

    public void callingGetUserListApi() throws IOException {
        properties.load(file);
        RestAssured.baseURI = properties.getProperty("baseURL");

        Response response =
                given()
                        .contentType("application/json")
                        .header("Authorization", properties.getProperty("token"))
                        .header("X-AUTH-SECRET-KEY", properties.getProperty("X-AUTH-SECRET-KEY"))
                .when()
                        .get("/user/list")
                .then().statusCode(200).extract().response();
    }

    public void callingGetUserListApiWithoutAuth() throws IOException {
        properties.load(file);
        RestAssured.baseURI = properties.getProperty("baseURL");

        Response response =
                given()
                        .contentType("application/json")
                        .header("Authorization", properties.getProperty("token"))
                        .when()
                        .get("/user/list")
                        .then().statusCode(401).extract().response();

        JsonPath responseObj = response.jsonPath();
        String message = responseObj.get("error.message");
        Assert.assertEquals(message, "Secret auth key validation failure!");
    }

    public void callingSearchUserApi() throws IOException {
        properties.load(file);
        RestAssured.baseURI = properties.getProperty("baseURL");

        Response response =
                given()
                        .contentType("application/json")
                        .header("Authorization", properties.getProperty("token"))
                        .header("X-AUTH-SECRET-KEY", properties.getProperty("X-AUTH-SECRET-KEY"))
                        .when()
                        .get("/user/search?phone_number=01686606909")
                        .then().statusCode(200).extract().response();

        JsonPath responseObj = response.jsonPath();
        String message = responseObj.get("user.phone_number");
        Assert.assertEquals(message, "01686606909");
    }

    public void callingSearchUserApiIncPhone() throws IOException {
        properties.load(file);
        RestAssured.baseURI = properties.getProperty("baseURL");

        Response response =
                given()
                        .contentType("application/json")
                        .header("Authorization", properties.getProperty("token"))
                        .header("X-AUTH-SECRET-KEY", properties.getProperty("X-AUTH-SECRET-KEY"))
                        .when()
                        .get("/user/search?phone_number=01930004578")
                        .then().statusCode(404).extract().response();

        JsonPath responseObj = response.jsonPath();
        String message = responseObj.get("message");
        Assert.assertEquals(message, "User not found");
    }

    public void callingSearchUserApiByRole() throws IOException {
        properties.load(file);
        RestAssured.baseURI = properties.getProperty("baseURL");

        Response response =
                given()
                        .contentType("application/json")
                        .header("Authorization", properties.getProperty("token"))
                        .header("X-AUTH-SECRET-KEY", properties.getProperty("X-AUTH-SECRET-KEY"))
                        .when()
                        .get("/user/search/Customer")
                        .then().statusCode(200).extract().response();

        JsonPath responseObj = response.jsonPath();
        String message = responseObj.get("users[0].role");
        Assert.assertEquals(message, "Customer");
    }


}
