package com.library.utility;

import io.cucumber.java.BeforeAll;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.expect;
import static io.restassured.RestAssured.given;

public class TestBase {

    public static RequestSpecification reqSpec;
    public static RequestSpecification reqUserSpec;
    public static ResponseSpecification resSpec;
    @BeforeAll
    public static void init(){
        RestAssured.baseURI = "https://library2.cydeo.com/rest/v1";

        reqSpec = given().log().uri();
/*
        reqUserSpec = given().log().all()
                .accept(ContentType.JSON)
                .auth().basic("user","user");

 */

        resSpec = expect();

    }

    //create dynamic method which is accepting username and password as a parameter and returning
    //request specification dynamicReqSpec()\
    public static RequestSpecification dynamicReqSpec(String username,String password){
        return given().log().all()
                .accept(ContentType.JSON)
                .auth().basic(username ,password);
    }

    //Create dynamic method which has parameter for status code, and returning ResponseSpecification
    //dynamicResSpec()

    public static ResponseSpecification dynamicStatusCodeResponse(int statusCode){
        return expect().statusCode(statusCode)
                .contentType(ContentType.JSON);
    }
}
