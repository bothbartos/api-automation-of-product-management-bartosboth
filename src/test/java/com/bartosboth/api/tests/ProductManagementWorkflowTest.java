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
    private static final Integer TEST_PRODUCT_ID = 1;
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
    @DisplayName("Create Product - POST /products")
    public void testCreateProduct() {

        Response response = productApiClient.createProduct(testProduct);

        assertThat(response.statusCode()).isIn(200, 201);

        Product createdProduct = response.getBody().as(Product.class);

        assertThat(createdProduct.id()).isEqualTo(21);
        assertThat(createdProduct.title()).isEqualTo(testProduct.title());
        assertThat(createdProduct.price()).isEqualTo(testProduct.price());
        assertThat(createdProduct.category()).isEqualTo(testProduct.category());
        assertThat(createdProduct.description()).isEqualTo(testProduct.description());
        assertThat(createdProduct.image()).isEqualTo(testProduct.image());

        System.out.println("Product created successfully with ID: " + createdProduct.id());
        System.out.println("Note: FakeStoreAPI simulates creation but doesn't persist data");
    }

    @Test
    @DisplayName("Get Product by ID - GET /products/{id}")
    public void testGetProductById() {

        Response response = productApiClient.getProduct(TEST_PRODUCT_ID);

        assertThat(response.statusCode()).isEqualTo(200);

        Product product = response.getBody().as(Product.class);

        assertThat(product.id()).isEqualTo(TEST_PRODUCT_ID);
        assertThat(product.title()).isEqualTo("Fjallraven - Foldsack No. 1 Backpack, Fits 15 Laptops");
        assertThat(product.price()).isEqualTo(109.95);
        assertThat(product.category()).isEqualTo("men's clothing");
        assertThat(product.description()).isEqualTo( "Your perfect pack for everyday use and walks in the forest. Stash your laptop (up to 15 inches) in the padded sleeve, your everyday");
        assertThat(product.image()).isEqualTo( "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_.jpg");
        assertThat(product.rating()).isEqualTo(new Product.Rating(3.9, 120));

        System.out.println("Product retrieved successfully: " + product.title());
    }

    @Test
    @DisplayName("Get All Products - GET /products")
    public void testGetAllProducts() {

        Response response = productApiClient.getProducts();

        assertThat(response.statusCode()).isEqualTo(200);

        Product[] products = response.getBody().as(Product[].class);

        assertThat(products).isNotNull().isNotEmpty();
        assertThat(products.length).isGreaterThanOrEqualTo(initialProductCount);

        Product product = products[0];

        assertThat(product.id()).isEqualTo(TEST_PRODUCT_ID);
        assertThat(product.title()).isEqualTo("Fjallraven - Foldsack No. 1 Backpack, Fits 15 Laptops");
        assertThat(product.price()).isEqualTo(109.95);
        assertThat(product.category()).isEqualTo("men's clothing");
        assertThat(product.description()).isEqualTo( "Your perfect pack for everyday use and walks in the forest. Stash your laptop (up to 15 inches) in the padded sleeve, your everyday");
        assertThat(product.image()).isEqualTo( "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_.jpg");
        assertThat(product.rating()).isEqualTo(new Product.Rating(3.9, 120));

        System.out.println("Retrieved " + products.length + " products successfully");
    }

    @Test
    @DisplayName("Update Product - PUT /products/{id}")
    public void testUpdateProduct() {

        Product updatedProduct = ProductTestDataFactory.createValidProduct();

        Response response = productApiClient.updateProduct(TEST_PRODUCT_ID, updatedProduct);

        assertThat(response.statusCode()).isEqualTo(200);

        Product product = response.getBody().as(Product.class);

        assertThat(product.id()).isEqualTo(TEST_PRODUCT_ID);
        assertThat(product.title()).isEqualTo(updatedProduct.title());
        assertThat(product.price()).isEqualTo(updatedProduct.price());
        assertThat(product.category()).isEqualTo(updatedProduct.category());
        assertThat(product.description()).isEqualTo(updatedProduct.description());
        assertThat(product.image()).isEqualTo(updatedProduct.image());

        System.out.println("Product updated successfully: " + product.title());
        System.out.println("Note: FakeStoreAPI simulates update but doesn't persist changes");
    }

    @Test
    @DisplayName("Delete Product - DELETE /products/{id}")
    public void testDeleteProduct() {

        assertThat(TEST_PRODUCT_ID).isNotNull();

        Response response = productApiClient.deleteProduct(TEST_PRODUCT_ID);

        assertThat(response.statusCode()).isEqualTo(200);

        Product deletedProduct = response.body().as(Product.class);

        assertThat(deletedProduct.id()).isEqualTo(TEST_PRODUCT_ID);
        assertThat(deletedProduct.title()).isNotNull();
        assertThat(deletedProduct.price()).isNotNull();
        assertThat(deletedProduct.category()).isNotNull();

        System.out.println("Product deleted successfully with ID: " + TEST_PRODUCT_ID);
        System.out.println("Note: FakeStoreAPI simulates deletion but doesn't actually remove data");
    }
}
