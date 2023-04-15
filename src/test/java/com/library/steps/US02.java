package com.library.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

import static org.hamcrest.MatcherAssert.assertThat;


public class US02 {


   String pathId;
    @Given("Path param is {string}")
    public void path_param_is(String param) {
        pathId = param;
        US01APIStepDefs.givenPart.pathParam("id",param);



    }
    @Then("{string} field should be same with path param")
    public void field_should_be_same_with_path_param(String field) {
        US01APIStepDefs.thenPart.body(field,is(pathId));
    }
    @Then("following fields should not be null")
    public void following_fields_should_not_be_null(List<String> fields) {
        for (String each : fields) {
            assertThat(each, is(notNullValue()));
        }
    }
}
