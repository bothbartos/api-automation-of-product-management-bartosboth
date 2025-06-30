package com.bartosboth.api.model;

public class Product {

    private Integer id;
    private String title;
    private Double price;
    private String description;
    private String category;
    private String image;
    private Rating rating;

    public Product() {}

    public Product(String title, Double price, String category, String description, String image) {
        this.title = title;
        this.price = price;
        this.category = category;
        this.description = description;
        this.image = image;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public Rating getRating() { return rating; }
    public void setRating(Rating rating) { this.rating = rating; }

    public static class Rating {
        private Double rate;
        private Integer count;

        public Double getRate() { return rate; }
        public void setRate(Double rate) { this.rate = rate; }

        public Integer getCount() { return count; }
        public void setCount(Integer count) { this.count = count; }
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", category='" + category + '\'' +
                '}';
    }
}

