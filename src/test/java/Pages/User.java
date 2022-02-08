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

    Integer ID;
    String name;
    String email;
    String password;
    String phone_number;
    String nid;
    String role;

    public void generateUser() throws IOException, ConfigurationException {
        properties.load(file);
        RestAssured.baseURI = "https://randomuser.me/api";

        Response response =
                given()
                        .contentType("application/json")
                .when()
                        .get()
                        .then().statusCode(200).extract().response();

        JsonPath resObj = response.jsonPath();

        ID = (int) Math.floor(Math.random() * (999999-100000) +1);
        name = resObj.get("results[0].name.first");
        email = resObj.get("results[0].email");
        password = resObj.get("results[0].login.password");
        phone_number = resObj.get("results[0].phone");
        nid = resObj.get("results[0].login.uuid");
        role = "Customer";

        Utils.setEnvironment("id", ID.toString());
        Utils.setEnvironment("name", name);
        Utils.setEnvironment("email", email);
        Utils.setEnvironment("password", password);
        Utils.setEnvironment("phone_number", phone_number);
        Utils.setEnvironment("nid", nid);
        Utils.setEnvironment("role", role);
    }

    public void callingCreateUserApi() throws IOException {
        properties.load(file);
        RestAssured.baseURI = properties.getProperty("baseURL");

        Response response =
                given()
                        .contentType("application/json")
                        .header("Authorization", properties.getProperty("token"))
                        .header("X-AUTH-SECRET-KEY", properties.getProperty("X-AUTH-SECRET-KEY"))
                        .body("" +
                                "{\"id\":" + properties.getProperty("id") + ",\n" +
                                "    \"name\":\"" + properties.getProperty("name") + "\", \n" +
                                "    \"email\":\"" + properties.getProperty("email") + "\",\n" +
                                "    \"password\":\""+properties.getProperty("password")+"\",\n" +
                                "    \"phone_number\":\""+properties.getProperty("phone_number")+"\",\n" +
                                "    \"nid\":\""+properties.getProperty("nid")+"\",\n" +
                                "    \"role\":\"" + properties.getProperty("role") + "\"}")

                .when()
                        .post("/user/create")
                .then().statusCode(201).extract().response();

        JsonPath responseObj = response.jsonPath();
        String message = responseObj.get("message");
        Assert.assertEquals(message, "User created successfully");

    }

    public void callingCreateExistingUserApi() throws IOException {
        properties.load(file);
        RestAssured.baseURI = properties.getProperty("baseURL");

        Response response =
                given()
                        .contentType("application/json")
                        .header("Authorization", properties.getProperty("token"))
                        .header("X-AUTH-SECRET-KEY", properties.getProperty("X-AUTH-SECRET-KEY"))
                        .body("{\n" +
                                "    \"name\":\"Salman Rahman\",\n" +
                                "    \"email\":\"salmansrabon@gmail.com\",\n" +
                                "    \"password\":\"1234\",\n" +
                                "    \"phone_number\":\"01686606909\",\n" +
                                "    \"nid\":\"123456789\",\n" +
                                "    \"role\":\"Customer\"\n" +
                                "}")
                .when()
                        .post("/user/create")
                .then().statusCode(208).extract().response();

        JsonPath responseObj = response.jsonPath();
        String message = responseObj.get("message");
        Assert.assertEquals(message,"User already exists");
    }

    public void callingUpdateUserApi() throws IOException {
        properties.load(file);
        RestAssured.baseURI = properties.getProperty("baseURL");

        Response response =
                given()
                        .contentType("application/json")
                        .header("Authorization", properties.getProperty("token"))
                        .header("X-AUTH-SECRET-KEY", properties.getProperty("X-AUTH-SECRET-KEY"))
                        .body("{\n" +
                                "    \"name\": \"Hudson\",\n" +
                                "    \"email\": \"hudson.levesque@example.com\",\n" +
                                "    \"password\": \"golfer1\",\n" +
                                "    \"phone_number\": \"954-396-8278\",\n" +
                                "    \"nid\": \"69992b33-6739-41a8-b676-69bbe965ead3\",\n" +
                                "    \"role\": \"Agent\"\n" +
                                "\n" +
                                "}")
                .when()
                        .put("/user/update/163")
                        .then().statusCode(200).extract().response();

        JsonPath responseObj = response.jsonPath();
        String message = responseObj.get("message");
        Assert.assertEquals(message,"User updated successfully");
    }

    public void callingUpdateUserByPropsApi() throws IOException {
        properties.load(file);
        RestAssured.baseURI = properties.getProperty("baseURL");

        Response response =
                given()
                        .contentType("application/json")
                        .header("Authorization", properties.getProperty("token"))
                        .header("X-AUTH-SECRET-KEY", properties.getProperty("X-AUTH-SECRET-KEY"))
                        .body("{\n" +
                                "    \"name\": \"Novel\"\n" +
                                "}")
                .when()
                        .patch("/user/update/151")
                        .then().statusCode(200).extract().response();

        JsonPath responseObj = response.jsonPath();
        String message = responseObj.get("message");
        Assert.assertEquals(message,"User updated successfully");
    }

    public void callingDeleteUSerInvIDApi() throws IOException {
        properties.load(file);
        RestAssured.baseURI = properties.getProperty("baseURL");

        Response response =
                given()
                        .contentType("application/json")
                        .header("Authorization", properties.getProperty("token"))
                        .header("X-AUTH-SECRET-KEY", properties.getProperty("X-AUTH-SECRET-KEY"))

                .when()
                        .delete("/user/delete/2100")
                        .then().statusCode(404).extract().response();

        JsonPath responseObj = response.jsonPath();
        String message = responseObj.get("message");
        Assert.assertEquals(message,"User not found");
    }


}
