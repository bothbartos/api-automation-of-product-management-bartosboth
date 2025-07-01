package com.bartosboth.api.clients;
import com.bartosboth.api.config.BaseApiConfig;
import com.bartosboth.api.model.Product;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class ProductApiClient extends BaseApiConfig {

    private static final String PRODUCTS_ENDPOINT = "/products";

    public Response createProduct(Product product) {
        return given()
                .spec(createRequestSpec)
                .body(product)
                .when()
                .post(PRODUCTS_ENDPOINT)
                .then()
                .spec(createdResponseSpec)
                .extract().response();
    }

    public Response getProducts() {
        return given()
                .spec(requestSpec)
                .when()
                .get(PRODUCTS_ENDPOINT)
                .then()
                .spec(successResponseSpec)
                .extract().response();
    }

    public Response getProduct(int id) {
        return given()
                .spec(requestSpec)
                .when()
                .get(PRODUCTS_ENDPOINT + "/{id}", id)
                .then()
                .spec(successResponseSpec)
                .extract().response();
    }

    public Response updateProduct(int id, Product product) {
        return given()
                .spec(updateRequestSpec)
                .body(product)
                .when()
                .put(PRODUCTS_ENDPOINT + "/{id}", id)
                .then()
                .spec(updateResponseSpec)
                .extract().response();
    }

    public Response deleteProduct(int id) {
        return given()
                .spec(requestSpec)
                .when()
                .delete(PRODUCTS_ENDPOINT + "/{id}", id)
                .then()
                .spec(successResponseSpec)
                .extract().response();
    }

}
