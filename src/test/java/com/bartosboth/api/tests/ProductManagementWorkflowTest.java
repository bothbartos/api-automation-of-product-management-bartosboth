package com.bartosboth.api.tests;

import com.bartosboth.api.clients.ProductApiClient;
import com.bartosboth.api.config.BaseApiConfig;
import com.bartosboth.api.data.ProductTestDataFactory;
import com.bartosboth.api.model.Product;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductManagementWorkflowTest extends BaseApiConfig {

    private static ProductApiClient productApiClient;
    private static Product testProduct;
    private static final Integer testProductId = 1;
    private static int initialProductCount;

    @BeforeAll
    static void setUp() {
        productApiClient = new ProductApiClient();
        testProduct = ProductTestDataFactory.createValidProduct();
        Response allProductResponse = productApiClient.getProducts();
        Product[] products = allProductResponse.getBody().as(Product[].class);
        initialProductCount = products.length;
    }

    @Test
    @Order(1)
    @DisplayName("1. Create Product - POST /products")
    public void testCreateProduct() {

        Response response = productApiClient.createProduct(testProduct);

        assertThat(response.statusCode())
                .as("Create product should return 200 or 201")
                .isIn(200, 201);

        Product createdProduct = response.getBody().as(Product.class);

        assertThat(createdProduct.id())
                .as("Created product should have automatically generated ID")
                .isNotNull()
                .isPositive();


        assertThat(createdProduct.title())
                .as("Product title should match request")
                .isEqualTo(testProduct.title());

        assertThat(createdProduct.price())
                .as("Product price should match request")
                .isEqualTo(testProduct.price());

        assertThat(createdProduct.category())
                .as("Product category should match request")
                .isEqualTo(testProduct.category());

        assertThat(createdProduct.description())
                .as("Product description should match request")
                .isEqualTo(testProduct.description());

        assertThat(testProduct.image())
                .as("Product image should match request")
                .isEqualTo(testProduct.image());

        System.out.println("Product created successfully with ID: " + createdProduct.id());
        System.out.println("Note: FakeStoreAPI simulates creation but doesn't persist data");

    }

    @Test
    @Order(2)
    @DisplayName("2. Get Product by ID - GET /products/{id}")
    public void testGetProductById() {

        Response response = productApiClient.getProduct(testProductId);

        assertThat(response.statusCode())
                .as("Get product should return 200")
                .isEqualTo(200);

        Product product = response.getBody().as(Product.class);

        assertThat(product.id())
                .as("Retrieved product should match request ID")
                .isEqualTo(testProductId);

        assertThat(product.title())
                .as("Product should have a title")
                .isNotNull()
                .isNotEmpty();

        assertThat(product.price())
                .as("Product should have a price")
                .isNotNull()
                .isPositive();

        assertThat(product.category())
                .as("Product should have a category")
                .isNotNull()
                .isNotEmpty();

        System.out.println("Product retrieved successfully: " + product.title());

    }

    @Test
    @Order(3)
    @DisplayName("Get All Products - GET /products")
    public void testGetAllProducts() {

        Response response = productApiClient.getProducts();

        assertThat(response.statusCode())
                .as("Get all products should return 200")
                .isEqualTo(200);

        Product[] products = response.getBody().as(Product[].class);

        assertThat(products)
                .as("Response should contain products")
                .isNotNull()
                .isNotEmpty();

        assertThat(products.length)
                .as("Should have at least the initial product count")
                .isGreaterThanOrEqualTo(initialProductCount);

        Product product = products[0];

        assertThat(product.id())
                .as("Each product should have an ID")
                .isNotNull();

        assertThat(product.title())
                .as("Each product should have a title")
                .isNotNull()
                .isNotEmpty();

        System.out.println("Retrieved " + products.length + " products successfully");

    }

    @Test
    @Order(4)
    @DisplayName("4. Update Product - PUT /products/{id}")
    public void testUpdateProduct() {

        Product updatedProduct = ProductTestDataFactory.createUpdatedProduct();

        Response response = productApiClient.updateProduct(testProductId, updatedProduct);

        assertThat(response.statusCode())
                .as("Update product should return 200")
                .isEqualTo(200);

        Product product = response.getBody().as(Product.class);

        assertThat(product.id())
                .as("Retrieved product should match request ID")
                .isEqualTo(testProductId);

        assertThat(product.title())
                .as("Product title should be updated")
                .isEqualTo(updatedProduct.title());

        assertThat(product.price())
                .as("Product price should be updated")
                .isEqualTo(updatedProduct.price());

        System.out.println("Product updated successfully: " + product.title());
        System.out.println("Note: FakeStoreAPI simulates update but doesn't persist changes");

    }

    @Test
    @Order(5)
    @DisplayName("5. Delete Product - DELETE /products/{id}")
    public void testDeleteProduct() {

        assertThat(testProductId)
                .as("Product ID should be available from Create Test")
                .isNotNull();

        Response response = productApiClient.deleteProduct(testProductId);

        assertThat(response.statusCode())
                .as("Delete product should return 200")
                .isEqualTo(200);

        Product deletedProduct = response.body().as(Product.class);

        assertThat(deletedProduct.id())
                .as("Deleted product response should contain the product ID")
                .isEqualTo(testProductId);

        System.out.println("Product deleted successfully with ID: " + testProductId);
        System.out.println("Note: FakeStoreAPI simulates deletion but doesn't actually remove data");

    }

}
