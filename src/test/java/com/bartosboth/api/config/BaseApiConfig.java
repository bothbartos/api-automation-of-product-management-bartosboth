package com.bartosboth.api.config;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeAll;

import static org.hamcrest.Matchers.*;

public class BaseApiConfig {

    public static final String BASE_URL = "https://fakestoreapi.com";

    protected static RequestSpecification requestSpec;
    protected static RequestSpecification createRequestSpec;
    protected static RequestSpecification updateRequestSpec;
    protected static ResponseSpecification successResponseSpec;
    protected static ResponseSpecification createdResponseSpec;
    protected static ResponseSpecification notFoundResponseSpec;
    protected static ResponseSpecification updateResponseSpec;

    @BeforeAll
    public static void setupRestAssured() {
        RestAssured.baseURI = BASE_URL;

        requestSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addHeader("User-Agent", "RestAssured-AutomationTest/1.0")
                .build();

        createRequestSpec = new RequestSpecBuilder()
                .addRequestSpecification(requestSpec)
                .setContentType(ContentType.JSON)
                .build();

        updateRequestSpec = new RequestSpecBuilder()
                .addRequestSpecification(requestSpec)
                .setContentType(ContentType.JSON)
                .build();

        successResponseSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectHeader("Content-Type", containsString("application/json"))
                .expectResponseTime(lessThan(5000L))
                .build();

        createdResponseSpec = new ResponseSpecBuilder()
                .expectStatusCode(anyOf(equalTo(200), equalTo(201)))
                .expectHeader("Content-Type", containsString("application/json"))
                .expectResponseTime(lessThan(5000L))
                .expectBody("id", notNullValue())
                .build();


        notFoundResponseSpec = new ResponseSpecBuilder()
                .expectStatusCode(404)
                .expectResponseTime(lessThan(5000L))
                .build();

        updateResponseSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectHeader("Content-Type", containsString("application/json"))
                .expectResponseTime(lessThan(5000L))
                .expectBody("id", notNullValue())
                .build();

    }
}