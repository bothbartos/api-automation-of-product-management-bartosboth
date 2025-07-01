package com.bartosboth.api.data;

import com.bartosboth.api.model.Product;

public class ProductTestDataFactory {
    public static Product createValidProduct() {
        return new Product(
                "Test Product",
                99.99,
                "electronics",
                "A test product created for testing",
                "https://www.placeholder.com/300x300"
        );
    }

    public static Product createProductWithCategory(String category) {
        return new Product(
                "Test Product" + category,
                149.99,
                category,
                "A test product in " + category + " category",
                "https://www.placeholder.com/300x300"
        );
    }

    public static Product createUpdatedProduct() {
        return new Product(
                "Updated Test Product",
                199.99,
                "electronics",
                "A test product updated for testing",
                "https://www.placeholder.com/300x300"
        );
    }
}
