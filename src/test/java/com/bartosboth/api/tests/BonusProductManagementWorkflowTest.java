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
        assertThat(products).hasSize(20);

        Product firstProduct = products[0];
        assertThat(firstProduct.id()).isNotNull().isPositive();
        assertThat(firstProduct.title()).isNotNull().isNotEmpty();
        assertThat(firstProduct.price()).isNotNull().isPositive();
        assertThat(firstProduct.description()).isNotNull();
        assertThat(firstProduct.category()).isNotNull().isNotEmpty();
        assertThat(firstProduct.image()).isNotNull().isNotEmpty();

        System.out.println("Schema validation passed for all products");
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

        Product product = response.as(Product.class);
        assertThat(product.id()).isEqualTo(TEST_PRODUCT_ID);
        assertThat(product.title()).isNotNull().isNotEmpty();
        assertThat(product.price()).isNotNull().isPositive();
        assertThat(product.description()).isNotNull();
        assertThat(product.category()).isNotNull().isNotEmpty();
        assertThat(product.image()).isNotNull().isNotEmpty();
        assertThat(product.rating()).isNotNull();
        assertThat(product.rating().rate()).isNotNull().isPositive();
        assertThat(product.rating().count()).isNotNull().isNotNegative();

        System.out.println("Schema validation passed for single product");
    }

    @Test
    @Order(3)
    @DisplayName("Update Product with CSV Test Data")
    public void testUpdateProductWithCsvData() {

        List<Product> updateProducts = ProductTestDataFactory.getUpdateProducts();
        Product updateProduct = updateProducts.getFirst();

        Response response = productClient.updateProduct(TEST_PRODUCT_ID, updateProduct);

        response.then().spec(updateResponseSpec);

        Product updatedProduct = response.as(Product.class);
        assertThat(updatedProduct.id()).isEqualTo(TEST_PRODUCT_ID);
        assertThat(updatedProduct.title()).isEqualTo(updateProduct.title());
        assertThat(updatedProduct.price()).isEqualTo(updateProduct.price());
        assertThat(updatedProduct.category()).isEqualTo(updateProduct.category());
        assertThat(updatedProduct.description()).isEqualTo(updateProduct.description());
        assertThat(updatedProduct.image()).isEqualTo(updateProduct.image());

        System.out.println("Update request processed successfully for product ID: " + TEST_PRODUCT_ID);
    }

    @Test
    @Order(4)
    @DisplayName("Validate CSV Data Loading")
    public void testCsvDataLoading() {

        List<Product> allCsvProducts = ProductTestDataFactory.loadProductsFromCsv("test-data/products.csv");

        assertThat(allCsvProducts).isNotEmpty().hasSize(5);

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

        assertThat(currentCount).isEqualTo(initialProductCount);

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

    @ParameterizedTest
    @MethodSource("provideCreateProductsFromCsv")
    @DisplayName("Create Products from CSV Test Data")
    public void testCreateProductsFromCsv(Product product) {

        Response response = productClient.createProduct(product);

        response.then().spec(createdResponseSpec);

        Product createdProduct = response.as(Product.class);

        assertThat(createdProduct.id()).isNotNull().isPositive();
        assertThat(createdProduct.title()).isEqualTo(product.title());
        assertThat(createdProduct.price()).isEqualTo(product.price());
        assertThat(createdProduct.category()).isEqualTo(product.category());
        assertThat(createdProduct.description()).isEqualTo(product.description());
        assertThat(createdProduct.image()).isEqualTo(product.image());

        System.out.println("Created product from CSV: " + createdProduct.title() +
                " (ID: " + createdProduct.id() + ", Price: $" + createdProduct.price() + ")");
    }
}
