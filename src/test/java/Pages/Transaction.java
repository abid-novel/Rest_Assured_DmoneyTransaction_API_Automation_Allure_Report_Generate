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

public class Transaction {
    Properties properties = new Properties();
    FileInputStream file = new FileInputStream("./src/test/resources/config.properties");

    public Transaction() throws FileNotFoundException {
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

    public void callingGetAgentBalanceApi() throws IOException {
        properties.load(file);
        RestAssured.baseURI = properties.getProperty("baseURL");

        Response response =
                given()
                        .contentType("application/json")
                        .header("Authorization", properties.getProperty("token"))
                        .header("X-AUTH-SECRET-KEY", properties.getProperty("X-AUTH-SECRET-KEY"))
                .when()
                        .get("/transaction/balance/01753851797")
                .then().statusCode(200).extract().response();
    }

    public void callingGetUserBalanceApi() throws IOException {
        properties.load(file);
        RestAssured.baseURI = properties.getProperty("baseURL");

        Response response =
                given()
                        .contentType("application/json")
                        .header("Authorization", properties.getProperty("token"))
                        .header("X-AUTH-SECRET-KEY", properties.getProperty("X-AUTH-SECRET-KEY"))
                .when()
                        .get("/transaction/balance/01686606909")
                .then().statusCode(200).extract().response();
    }

    public void callingDepositApi() throws IOException {
        properties.load(file);
        RestAssured.baseURI = properties.getProperty("baseURL");

        Response response =
                given()
                        .contentType("application/json")
                        .header("Authorization", properties.getProperty("token"))
                        .header("X-AUTH-SECRET-KEY", properties.getProperty("X-AUTH-SECRET-KEY"))
                        .body("{\n" +
                                "    \"from_account\":\"SYSTEM\",\n" +
                                "    \"to_account\":\"01686606909\",\n" +
                                "    \"amount\":500\n" +
                                "}")
                .when()
                        .post("/transaction/deposit")
                .then().statusCode(201).extract().response();

        JsonPath responseObj = response.jsonPath();
        String message = responseObj.get("message");
        Assert.assertEquals(message, "Deposit successful");
    }

    public void callingDepositInsufficientBalanceApi() throws IOException {
        properties.load(file);
        RestAssured.baseURI = properties.getProperty("baseURL");

        Response response =
                given()
                        .contentType("application/json")
                        .header("Authorization", properties.getProperty("token"))
                        .header("X-AUTH-SECRET-KEY", properties.getProperty("X-AUTH-SECRET-KEY"))
                        .body("{\n" +
                                "    \"from_account\":\"0191949766\",\n" +
                                "    \"to_account\":\"01686606909\",\n" +
                                "    \"amount\":2000\n" +
                                "}")
                .when()
                        .post("/transaction/deposit")
                .then().statusCode(208).extract().response();

        JsonPath responseObj = response.jsonPath();
        String message = responseObj.get("message");
        Assert.assertEquals(message, "Insufficient balance");
    }

    public void callingSendMoneyApi() throws IOException {
        properties.load(file);
        RestAssured.baseURI = properties.getProperty("baseURL");

        Response response =
                given()
                        .contentType("application/json")
                        .header("Authorization", properties.getProperty("token"))
                        .header("X-AUTH-SECRET-KEY", properties.getProperty("X-AUTH-SECRET-KEY"))
                        .body("{\n" +
                                "    \"from_account\":\"01686606909\",\n" +
                                "    \"to_account\":\"01522828745\",\n" +
                                "    \"amount\":500\n" +
                                "}")
                .when()
                        .post("/transaction/sendmoney")
                .then().statusCode(201).extract().response();

        JsonPath responseObj = response.jsonPath();
        String message = responseObj.get("message");
        Assert.assertEquals(message, "Send money successful");
    }

    public void callingSendMoneyWithInsufficientBalanceApi() throws IOException {
        properties.load(file);
        RestAssured.baseURI = properties.getProperty("baseURL");

        Response response =
                given()
                        .contentType("application/json")
                        .header("Authorization", properties.getProperty("token"))
                        .header("X-AUTH-SECRET-KEY", properties.getProperty("X-AUTH-SECRET-KEY"))
                        .body("{\n" +
                                "    \"from_account\":\"01523475990\",\n" +
                                "    \"to_account\":\"01522828745\",\n" +
                                "    \"amount\":500\n" +
                                "}")
                .when()
                        .post("/transaction/sendmoney")
                .then().statusCode(208).extract().response();

        JsonPath responseObj = response.jsonPath();
        String message = responseObj.get("message");
        Assert.assertEquals(message, "Insufficient balance");
    }

    public void callingSendMoneyFeeVerificationApi() throws IOException {
        properties.load(file);
        RestAssured.baseURI = properties.getProperty("baseURL");

        Response response =
                given()
                        .contentType("application/json")
                        .header("Authorization", properties.getProperty("token"))
                        .header("X-AUTH-SECRET-KEY", properties.getProperty("X-AUTH-SECRET-KEY"))
                        .body("{\n" +
                                "    \"from_account\":\"01686606909\",\n" +
                                "    \"to_account\":\"01522828745\",\n" +
                                "    \"amount\":500\n" +
                                "}")
                .when()
                        .post("/transaction/sendmoney")
                .then().statusCode(201).extract().response();

        JsonPath responseObj = response.jsonPath();
        String message = responseObj.get("fee").toString();
        Assert.assertEquals(message, "5");
    }

    public void callingCashOutApi() throws IOException {
        properties.load(file);
        RestAssured.baseURI = properties.getProperty("baseURL");

        Response response =
                given()
                        .contentType("application/json")
                        .header("Authorization", properties.getProperty("token"))
                        .header("X-AUTH-SECRET-KEY", properties.getProperty("X-AUTH-SECRET-KEY"))
                        .body("{\n" +
                                "    \"from_account\":\"01686606909\",\n" +
                                "    \"to_account\":\"01753851797\",\n" +
                                "    \"amount\":100\n" +
                                "}")
                .when()
                        .post("/transaction/withdraw")
                .then().statusCode(201).extract().response();

        JsonPath responseObj = response.jsonPath();
        String message = responseObj.get("message");
        Assert.assertEquals(message, "Withdraw successful");
    }

    public void callingCashOutMinimumFeeApi() throws IOException {
        properties.load(file);
        RestAssured.baseURI = properties.getProperty("baseURL");

        Response response =
                given()
                        .contentType("application/json")
                        .header("Authorization", properties.getProperty("token"))
                        .header("X-AUTH-SECRET-KEY", properties.getProperty("X-AUTH-SECRET-KEY"))
                        .body("{\n" +
                                "    \"from_account\":\"01686606909\",\n" +
                                "    \"to_account\":\"01753851797\",\n" +
                                "    \"amount\":1000\n" +
                                "}")
                .when()
                        .post("/transaction/withdraw")
                .then().statusCode(201).extract().response();

        JsonPath responseObj = response.jsonPath();
        String message = responseObj.get("fee").toString();
        Assert.assertEquals(message, "10");
    }

    public void callingCashOutFeeApi() throws IOException {
        properties.load(file);
        RestAssured.baseURI = properties.getProperty("baseURL");

        Response response =
                given()
                        .contentType("application/json")
                        .header("Authorization", properties.getProperty("token"))
                        .header("X-AUTH-SECRET-KEY", properties.getProperty("X-AUTH-SECRET-KEY"))
                        .body("{\n" +
                                "    \"from_account\":\"01686606909\",\n" +
                                "    \"to_account\":\"01753851797\",\n" +
                                "    \"amount\":1500\n" +
                                "}")
                .when()
                        .post("/transaction/withdraw")
                .then().statusCode(201).extract().response();

        JsonPath responseObj = response.jsonPath();
        String message = responseObj.get("fee").toString();
        Assert.assertEquals(message, "15");
    }

    public void callingCashInsufficientBalanceApi() throws IOException {
        properties.load(file);
        RestAssured.baseURI = properties.getProperty("baseURL");

        Response response =
                given()
                        .contentType("application/json")
                        .header("Authorization", properties.getProperty("token"))
                        .header("X-AUTH-SECRET-KEY", properties.getProperty("X-AUTH-SECRET-KEY"))
                        .body("{\n" +
                                "    \"from_account\":\"01524196887\",\n" +
                                "    \"to_account\":\"01753851797\",\n" +
                                "    \"amount\":100\n" +
                                "}")
                .when()
                        .post("/transaction/withdraw")
                .then().statusCode(208).extract().response();

        JsonPath responseObj = response.jsonPath();
        String message = responseObj.get("message");
        Assert.assertEquals(message, "Insufficient balance");
    }

    public void callingStatementByAccApi() throws IOException {
        properties.load(file);
        RestAssured.baseURI = properties.getProperty("baseURL");

        Response response =
                given()
                        .contentType("application/json")
                        .header("Authorization", properties.getProperty("token"))
                        .header("X-AUTH-SECRET-KEY", properties.getProperty("X-AUTH-SECRET-KEY"))

                .when()
                        .get("/transaction/statement/01686606909")
                .then().statusCode(200).extract().response();

        JsonPath responseObj = response.jsonPath();
        String message = responseObj.get("message");
        Assert.assertEquals(message, "Transaction list");
    }

    public void callingStatementByTRNXApi() throws IOException {
        properties.load(file);
        RestAssured.baseURI = properties.getProperty("baseURL");

        Response response =
                given()
                        .contentType("application/json")
                        .header("Authorization", properties.getProperty("token"))
                        .header("X-AUTH-SECRET-KEY", properties.getProperty("X-AUTH-SECRET-KEY"))

                        .when()
                        .get("/transaction/search/TXN332117")
                        .then().statusCode(200).extract().response();

        JsonPath responseObj = response.jsonPath();
        String message = responseObj.get("message");
        String count = responseObj.get("count").toString();
        Assert.assertEquals(message, "Transaction list");
        Assert.assertEquals(count, "2");
    }

    public void callingAllTransactionListApi() throws IOException {
        properties.load(file);
        RestAssured.baseURI = properties.getProperty("baseURL");

        Response response =
                given()
                        .contentType("application/json")
                        .header("Authorization", properties.getProperty("token"))
                        .header("X-AUTH-SECRET-KEY", properties.getProperty("X-AUTH-SECRET-KEY"))

                        .when()
                        .get("/transaction/list")
                        .then().statusCode(200).extract().response();

        JsonPath responseObj = response.jsonPath();
        String message = responseObj.get("message");
        Assert.assertEquals(message, "Transaction list");
    }

}
