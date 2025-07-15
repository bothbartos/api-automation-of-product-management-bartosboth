package com.bartosboth.api.tests;

import com.bartosboth.api.clients.ProductApiClient;
import com.bartosboth.api.config.BaseApiConfig;
import com.bartosboth.api.data.ProductTestDataFactory;
import com.bartosboth.api.model.Product;
import io.restassured.response.Response;
import org.assertj.core.api.AssertionsForClassTypes;
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

        Product[] allProducts = allProductsResponse.as(Product[].class);
        initialProductCount = allProducts.length;

        System.out.println("Initial product count: " + initialProductCount);
        System.out.println("Loading test data from CSV files...");
    }

    @Test
    @DisplayName("Validate All Products with JSON Schema")
    public void testGetAllProductsWithSchemaValidation() {

        Response response = productClient.getProducts();

        response.then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("schemas/products-array-schema.json"));

        Product[] products = response.as(Product[].class);
        assertThat(products).hasSize(20);

        Product firstProduct = products[0];
        assertThat(firstProduct.id()).isEqualTo(TEST_PRODUCT_ID);
        assertThat(firstProduct.title()).isEqualTo("Fjallraven - Foldsack No. 1 Backpack, Fits 15 Laptops");
        assertThat(firstProduct.price()).isEqualTo(109.95);
        assertThat(firstProduct.category()).isEqualTo("men's clothing");
        assertThat(firstProduct.description()).isEqualTo( "Your perfect pack for everyday use and walks in the forest. Stash your laptop (up to 15 inches) in the padded sleeve, your everyday");
        assertThat(firstProduct.image()).isEqualTo( "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_.jpg");
        assertThat(firstProduct.rating()).isEqualTo(new Product.Rating(3.9, 120));

        System.out.println("Schema validation passed for all products");
    }

    @Test
    @DisplayName("Validate Single Product with JSON Schema")
    public void testGetProductByIdWithSchemaValidation() {

        Response response = productClient.getProduct(TEST_PRODUCT_ID);


        response.then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("schemas/product-schema.json"));

        Product product = response.as(Product.class);
        assertThat(product.id()).isEqualTo(TEST_PRODUCT_ID);
        assertThat(product.title()).isEqualTo("Fjallraven - Foldsack No. 1 Backpack, Fits 15 Laptops");
        assertThat(product.price()).isEqualTo(109.95);
        assertThat(product.category()).isEqualTo("men's clothing");
        assertThat(product.description()).isEqualTo( "Your perfect pack for everyday use and walks in the forest. Stash your laptop (up to 15 inches) in the padded sleeve, your everyday");
        assertThat(product.image()).isEqualTo( "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_.jpg");
        assertThat(product.rating()).isEqualTo(new Product.Rating(3.9, 120));


        System.out.println("Schema validation passed for single product");
    }

    @Test
    @DisplayName("Update Product with CSV Test Data")
    public void testUpdateProductWithCsvData() {

        List<Product> updateProducts = ProductTestDataFactory.getUpdateProducts();
        Product updateProduct = updateProducts.getFirst();

        Response response = productClient.updateProduct(TEST_PRODUCT_ID, updateProduct);


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
    @DisplayName("Validate Product Count Consistency")
    public void testProductCountConsistency() {

        Response response = productClient.getProducts();

        Product[] currentProducts = response.as(Product[].class);
        int currentCount = currentProducts.length;

        assertThat(currentCount).isEqualTo(initialProductCount);

        System.out.println("Product count validation: " + currentCount + " products (consistent)");
    }

    @Test
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


        Product createdProduct = response.as(Product.class);

        assertThat(createdProduct.id()).isEqualTo(21);
        assertThat(createdProduct.title()).isEqualTo(product.title());
        assertThat(createdProduct.price()).isEqualTo(product.price());
        assertThat(createdProduct.category()).isEqualTo(product.category());
        assertThat(createdProduct.description()).isEqualTo(product.description());
        assertThat(createdProduct.image()).isEqualTo(product.image());

        System.out.println("Created product from CSV: " + createdProduct.title() +
                " (ID: " + createdProduct.id() + ", Price: $" + createdProduct.price() + ")");
    }
}
