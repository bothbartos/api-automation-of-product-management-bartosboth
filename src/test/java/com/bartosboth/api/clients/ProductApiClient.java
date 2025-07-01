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

    public Response getProduct(String id) {
        return given()
                .pathParam("id", id)
                .when()
                .get(PRODUCTS_ENDPOINT + "/${id}");
    }

    public Response updateProduct(String id, Product product) {
        return given()
                .pathParam("id", id)
                .body(product)
                .when()
                .put(PRODUCTS_ENDPOINT + "/${id}");
    }

    public Response deleteProduct(String id) {
        return given()
                .pathParam("id", id)
                .when()
                .delete(PRODUCTS_ENDPOINT + "/${id}");
    }

    public Response getProductsByCategory(String category) {
        return given()
                .pathParam("category", category)
                .when()
                .get(PRODUCTS_ENDPOINT + "/category/${category}");
    }

    public Response getProductsWithLimit(int limit) {
        return given()
                .pathParam("limit", limit)
                .when()
                .get(PRODUCTS_ENDPOINT);
    }


}
