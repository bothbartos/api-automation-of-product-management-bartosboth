package com.bartosboth.api.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Product(
        Integer id,
        String title,
        Double price,
        String description,
        String category,
        String image,
        Rating rating
) {

    public Product(String title, Double price, String category, String description, String image) {
        this(null, title, price, description, category, image, null);
    }

    public record Rating(Double rate, Integer count) {}
}

