package com.bartosboth.api.tests;

import com.bartosboth.api.clients.ProductApiClient;
import com.bartosboth.api.config.BaseApiConfig;
import com.bartosboth.api.data.ProductTestDataFactory;
import com.bartosboth.api.model.Product;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.lessThan;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BonusProductManagementWorkflowTest extends BaseApiConfig {

    private static ProductApiClient productClient;
    private static int initialProductCount;
    private static final Integer TEST_PRODUCT_ID = 1;

    @BeforeAll
    public static void setupTestClass() {
        productClient = new ProductApiClient();

        // Get and validate initial product count
        Response allProductsResponse = productClient.getProducts();
        allProductsResponse.then().spec(successResponseSpec);

        Product[] allProducts = allProductsResponse.as(Product[].class);
        initialProductCount = allProducts.length;

        System.out.println("Initial product count: " + initialProductCount);
        System.out.println("Loading test data from CSV files...");
    }

    @Test
    @Order(1)
    @DisplayName("Validate All Products with JSON Schema")
    public void testGetAllProductsWithSchemaValidation() {
        Response response = productClient.getProducts();

        response.then().spec(successResponseSpec);

        response.then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("schemas/products-array-schema.json"));

        Product[] products = response.as(Product[].class);
        assertThat(products)
                .as("Should return exactly 20 products from FakeStoreAPI")
                .hasSize(20);

        System.out.println("✅ Schema validation passed for all products");
    }

    @Test
    @Order(2)
    @DisplayName("Validate Single Product with JSON Schema")
    public void testGetProductByIdWithSchemaValidation() {
        Response response = productClient.getProduct(TEST_PRODUCT_ID);

        response.then().spec(successResponseSpec);

        response.then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("schemas/product-schema.json"));

        System.out.println("Schema validation passed for single product");
    }


    @Test
    @Order(3)
    @DisplayName("Update Product with CSV Test Data")
    public void testUpdateProductWithCsvData() {

        List<Product> updateProducts = ProductTestDataFactory.getUpdateProducts();
        Product updateProduct = updateProducts.getFirst();

        Response response = productClient.updateProduct(TEST_PRODUCT_ID, updateProduct);

        response.then().spec(successResponseSpec);

        String responseBody = response.getBody().asString();
        System.out.println("Update response: " + responseBody);

        assertThat(response.jsonPath().getInt("id"))
                .as("Response should contain the updated product ID")
                .isEqualTo(TEST_PRODUCT_ID);

        System.out.println("Update request processed successfully for product ID: " + TEST_PRODUCT_ID);
        System.out.println("Note: FakeStoreAPI only returns ID in update response (documented behavior)");
        System.out.println("Attempted to update: " + updateProduct.title() + " (Price: $" + updateProduct.price() + ")");

    }

    @Test
    @Order(4)
    @DisplayName("Validate CSV Data Loading")
    public void testCsvDataLoading() {
        List<Product> allCsvProducts = ProductTestDataFactory.loadProductsFromCsv("test-data/products.csv");

        assertThat(allCsvProducts)
                .as("Should load products from CSV file")
                .isNotEmpty()
                .hasSize(5);

        allCsvProducts.forEach(product -> {
            assertThat(product.title()).isNotBlank();
            assertThat(product.price()).isPositive();
            assertThat(product.category()).isNotBlank();
            assertThat(product.description()).isNotBlank();
            assertThat(product.image()).isNotBlank();
        });

        System.out.println("Successfully loaded " + allCsvProducts.size() + " products from CSV");
        allCsvProducts.forEach(product ->
                System.out.println("   - " + product.title() + " ($" + product.price() + ")"));
    }

    @Test
    @Order(5)
    @DisplayName("Validate Product Count Consistency")
    public void testProductCountConsistency() {
        Response response = productClient.getProducts();
        response.then().spec(successResponseSpec);

        Product[] currentProducts = response.as(Product[].class);
        int currentCount = currentProducts.length;

        assertThat(currentCount)
                .as("Product count should remain consistent in FakeStoreAPI")
                .isEqualTo(initialProductCount);

        System.out.println("Product count validation: " + currentCount + " products (consistent)");
    }

    @Test
    @Order(6)
    @DisplayName("Contract Validation - Response Time and Headers")
    public void testContractValidation() {
        Response response = productClient.getProducts();

        response.then()
                .time(lessThan(5000L))
                .header("Content-Type", containsString("application/json"))
                .header("Access-Control-Allow-Origin", "*");

        System.out.println("Contract validation passed - Response time: " +
                response.getTime() + "ms");
    }

    static Stream<Product> provideCreateProductsFromCsv() {
        return ProductTestDataFactory.getCreateProducts().stream();
    }

    // Data-driven test using CSV data for creation

    @ParameterizedTest
    @MethodSource("provideCreateProductsFromCsv")
    @DisplayName("Create Products from CSV Test Data")
    public void testCreateProductsFromCsv(Product product) {

        Response response = productClient.createProduct(product);

        response.then().spec(createdResponseSpec);

        String responseBody = response.getBody().asString();
        System.out.println("Create response: " + responseBody);

        assertThat(response.jsonPath().getInt("id"))
                .as("Response should contain the created product ID")
                .isNotNull()
                .isPositive();

        System.out.println("Create request processed successfully");
        System.out.println("Note: FakeStoreAPI only returns ID in create response (documented behavior)");
        System.out.println("Attempted to create: " + product.title() + " (Price: $" + product.price() + ")");
    }

}

