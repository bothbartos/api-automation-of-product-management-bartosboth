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

        assertThat(response.statusCode()).isIn(200, 201);

        Product createdProduct = response.getBody().as(Product.class);

        assertThat(createdProduct.id()).isNotNull().isPositive();
        assertThat(createdProduct.title()).isEqualTo(testProduct.title());
        assertThat(createdProduct.price()).isEqualTo(testProduct.price());
        assertThat(createdProduct.category()).isEqualTo(testProduct.category());
        assertThat(createdProduct.description()).isEqualTo(testProduct.description());
        assertThat(createdProduct.image()).isEqualTo(testProduct.image());

        System.out.println("Product created successfully with ID: " + createdProduct.id());
        System.out.println("Note: FakeStoreAPI simulates creation but doesn't persist data");
    }

    @Test
    @Order(2)
    @DisplayName("2. Get Product by ID - GET /products/{id}")
    public void testGetProductById() {

        Response response = productApiClient.getProduct(testProductId);

        assertThat(response.statusCode()).isEqualTo(200);

        Product product = response.getBody().as(Product.class);

        assertThat(product.id()).isEqualTo(testProductId);
        assertThat(product.title()).isNotNull().isNotEmpty();
        assertThat(product.price()).isNotNull().isPositive();
        assertThat(product.category()).isNotNull().isNotEmpty();
        assertThat(product.description()).isNotNull();
        assertThat(product.image()).isNotNull().isNotEmpty();
        assertThat(product.rating()).isNotNull();
        assertThat(product.rating().rate()).isNotNull().isPositive();
        assertThat(product.rating().count()).isNotNull().isNotNegative();

        System.out.println("Product retrieved successfully: " + product.title());
    }

    @Test
    @Order(3)
    @DisplayName("Get All Products - GET /products")
    public void testGetAllProducts() {

        Response response = productApiClient.getProducts();

        assertThat(response.statusCode()).isEqualTo(200);

        Product[] products = response.getBody().as(Product[].class);

        assertThat(products).isNotNull().isNotEmpty();
        assertThat(products.length).isGreaterThanOrEqualTo(initialProductCount);

        Product product = products[0];

        assertThat(product.id()).isNotNull().isPositive();
        assertThat(product.title()).isNotNull().isNotEmpty();
        assertThat(product.price()).isNotNull().isPositive();
        assertThat(product.category()).isNotNull().isNotEmpty();
        assertThat(product.description()).isNotNull();
        assertThat(product.image()).isNotNull().isNotEmpty();
        assertThat(product.rating()).isNotNull();

        System.out.println("Retrieved " + products.length + " products successfully");
    }

    @Test
    @Order(4)
    @DisplayName("4. Update Product - PUT /products/{id}")
    public void testUpdateProduct() {

        Product updatedProduct = ProductTestDataFactory.createValidProduct();

        Response response = productApiClient.updateProduct(testProductId, updatedProduct);

        assertThat(response.statusCode()).isEqualTo(200);

        Product product = response.getBody().as(Product.class);

        assertThat(product.id()).isEqualTo(testProductId);
        assertThat(product.title()).isEqualTo(updatedProduct.title());
        assertThat(product.price()).isEqualTo(updatedProduct.price());
        assertThat(product.category()).isEqualTo(updatedProduct.category());
        assertThat(product.description()).isEqualTo(updatedProduct.description());
        assertThat(product.image()).isEqualTo(updatedProduct.image());

        System.out.println("Product updated successfully: " + product.title());
        System.out.println("Note: FakeStoreAPI simulates update but doesn't persist changes");
    }

    @Test
    @Order(5)
    @DisplayName("5. Delete Product - DELETE /products/{id}")
    public void testDeleteProduct() {

        assertThat(testProductId).isNotNull();

        Response response = productApiClient.deleteProduct(testProductId);

        assertThat(response.statusCode()).isEqualTo(200);

        Product deletedProduct = response.body().as(Product.class);

        assertThat(deletedProduct.id()).isEqualTo(testProductId);
        assertThat(deletedProduct.title()).isNotNull();
        assertThat(deletedProduct.price()).isNotNull();
        assertThat(deletedProduct.category()).isNotNull();

        System.out.println("Product deleted successfully with ID: " + testProductId);
        System.out.println("Note: FakeStoreAPI simulates deletion but doesn't actually remove data");
    }
}
