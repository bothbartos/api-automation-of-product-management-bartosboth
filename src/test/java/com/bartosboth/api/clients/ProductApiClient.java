package com.bartosboth.api.clients;
import com.bartosboth.api.model.Product;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class ProductApiClient {

    private static final String PRODUCTS_ENDPOINT = "/products";

    public Response createProduct(Product product) {
        return given()
                .body(product)
                .when()
                .post(PRODUCTS_ENDPOINT);
    }

    public Response getProducts() {
        return given()
                .when()
                .get(PRODUCTS_ENDPOINT);
    }

    public Response getProduct(int id) {
        return given()
                .when()
                .get(PRODUCTS_ENDPOINT + "/{id}", id);
    }

    public Response updateProduct(int id, Product product) {
        return given()
                .body(product)
                .when()
                .put(PRODUCTS_ENDPOINT + "/{id}", id);
    }

    public Response deleteProduct(int id) {
        return given()
                .when()
                .delete(PRODUCTS_ENDPOINT + "/{id}", id);
    }

}
