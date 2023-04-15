package com.library.steps;

import com.library.utility.ConfigurationReader;
import com.library.utility.LibraryAPI_Util;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

public class US03 {
    @Given("Request Content Type header is {string}")
    public void request_content_type_header_is(String contentType) {
    US01APIStepDefs.givenPart.contentType(ContentType.JSON);
    }
    @Given("I create a random {string} as request body")
    public void i_create_a_random_as_request_body(String string) {
        US01APIStepDefs.givenPart.body(LibraryAPI_Util.getRandomBookMap());
    }
    @When("I send POST request to {string} endpoint")
    public void i_send_post_request_to_endpoint(String endpoint) {
        when().post(ConfigurationReader.getProperty("library.baseUri") + endpoint);
    }
    @Then("the field value for {string} path should be equal to {string}")
    public void the_field_value_for_path_should_be_equal_to(String attribute, String expectedValue) {

    }

}
