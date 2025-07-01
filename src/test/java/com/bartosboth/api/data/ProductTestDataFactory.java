package com.bartosboth.api.data;

import com.bartosboth.api.model.Product;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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

    public static List<Product> loadProductsFromCsv(String csvPath) {
        List<Product> products = new ArrayList<>();
        try (InputStream inputStream = ProductTestDataFactory.class
                .getClassLoader().getResourceAsStream(csvPath);
             CSVReader csvReader = new CSVReader(new InputStreamReader(inputStream))) {
            List<String[]> records = csvReader.readAll();

            for (int i = 1; i < records.size(); i++) {
                String[] record = records.get(i);
                if (record.length >= 5) {
                    Product product = new Product(
                            record[0].trim(), // title
                            Double.parseDouble(record[1].trim()), // price
                            record[2].trim(), // category
                            record[3].trim(), // description
                            record[4].trim()  // image
                    );
                    products.add(product);
                }
            }
        } catch (IOException | CsvException | NumberFormatException e) {
            throw new RuntimeException("Failed to load products from CSV: " + csvPath, e);
        }
        return products;
    }

    public static List<Product> getCreateProducts() {
        List<Product> allProducts = loadProductsFromCsv("test-data/products.csv");
        return allProducts.subList(0, Math.min(3, allProducts.size()));
    }

    public static List<Product> getUpdateProducts() {
        List<Product> allProducts = loadProductsFromCsv("test-data/products.csv");
        int size = allProducts.size();
        if (size > 3) {
            return allProducts.subList(3, size);
        }
        return List.of(createValidProduct());
    }

    public static Product getProductByIndex(int index) {
        List<Product> products = loadProductsFromCsv("test-data/products.csv");
        if (index >= 0 && index < products.size()) {
            return products.get(index);
        }
        return createValidProduct();
    }
}
