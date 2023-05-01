package com.library.steps;


import com.library.pages.BookPage;
import com.library.pages.LoginPage;
import com.library.utility.ConfigurationReader;
import com.library.utility.DB_Util;
import com.library.utility.Driver;
import com.library.utility.LibraryAPI_Util;


import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;


import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;




public class APIStepDefs{

    /**
     * US 01 RELATED STEPS
     *
     */
    RequestSpecification givenPart;

    Response response;
    ValidatableResponse thenPart;



    public static String pathId;

    BookPage bookPage = new BookPage();

    @Given("I logged Library api as a {string}")
    public void i_logged_library_api_as_a(String userType) {

        givenPart = given().log().uri()
                .header("x-library-token", LibraryAPI_Util.getToken(userType));
    }
    @Given("Accept header is {string}")
    public void accept_header_is(String contentType) {
        givenPart.accept(contentType);
    }

    @When("I send GET request to {string} endpoint")
    public void i_send_get_request_to_endpoint(String endpoint) {
        response = givenPart.when().get(ConfigurationReader.getProperty("library.baseUri") + endpoint);
        thenPart = response.then();
    }
    @Then("status code should be {int}")
    public void status_code_should_be(int statusCode) {
      thenPart.statusCode(statusCode);
    }
    @Then("Response Content type is {string}")
    public void response_content_type_is(String responseContentType) {
        thenPart.contentType(responseContentType);
    }
    @Then("{string} field should not be null")
    public void field_should_not_be_null(String path) {
        thenPart.body(path, is(notNullValue()));
    }

    @Given("Path param is {string}")
    public void path_param_is(String param) {
        pathId = param;
        givenPart.pathParam("id",param);

    }
    @Then("{string} field should be same with path param")
    public void field_should_be_same_with_path_param(String field) {
        thenPart.body(field,is(pathId));
    }
    @Then("following fields should not be null")
    public void following_fields_should_not_be_null(List<String> fields) {
        for (String each : fields) {
            assertThat(each, is(notNullValue()));
        }
    }

    @Given("Request Content Type header is {string}")
    public void request_content_type_header_is(String requestContentType) {

        givenPart.formParam("Content-Type",requestContentType);

    }

    Map<String, Object> randomDataMap;
    @Given("I create a random {string} as request body")
    public void i_create_a_random_as_request_body(String randomData) {
        Map<String,Object> requestBody = new LinkedHashMap<>();

        switch(randomData) {
            case "user":
                requestBody = LibraryAPI_Util.getRandomUserMap();
                break;
            case "book":
                requestBody = LibraryAPI_Util.getRandomBookMap();
                break;
        }
            randomDataMap = requestBody;
            givenPart.formParams(requestBody);


    }
    @When("I send POST request to {string} endpoint")
    public void i_send_post_request_to_endpoint(String endpoint) {
        response = givenPart.when().post(ConfigurationReader.getProperty("library.baseUri") + endpoint);
        thenPart = response.then();
    }
    @Then("the field value for {string} path should be equal to {string}")
    public void the_field_value_for_path_should_be_equal_to(String field, String valueOfField) {
          thenPart.body(field,is(valueOfField));
    }


    @And("UI, Database and API created book information must match")
    public void uiDatabaseAndAPICreatedBookInformationMustMatch() {

        Response apiData = given().header("x-library-token", LibraryAPI_Util.getToken("librarian"))
                .pathParam("id", response.path("book_id"))
                .when().get(ConfigurationReader.getProperty("library.baseUri") + "/get_book_by_id/{id}").prettyPeek();


        JsonPath jsonPath = apiData.jsonPath();
        String bookId = jsonPath.getString("id");
        System.out.println("bookId = " + bookId);


        Map<String,Object> apiBook = new LinkedHashMap<>();

        apiBook.put("name", jsonPath.getString("name"));
        apiBook.put("isbn", jsonPath.getString("isbn"));
        apiBook.put("year", jsonPath.getString("year"));
        apiBook.put("author", jsonPath.getString("author"));
        apiBook.put("book_category_id", jsonPath.getString("book_category_id"));
        apiBook.put("description", jsonPath.getString("description"));


        String bookID = jsonPath.getString("id");

        DB_Util.runQuery("select * from books where id='"+bookID+"'");
        Map<String,Object> dbBookInfo = DB_Util.getRowMap(1);

        dbBookInfo.remove("added_date");
        dbBookInfo.remove("id");

        System.out.println("apiBook = " + apiBook);
        System.out.println("dbBookInfo = " + dbBookInfo);



    }




}
