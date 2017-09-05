package com.melodicmusic.mobileapp.model;

/**
 * Created by Nelson on 3/9/2017.
 */

public class Product {
    private String id;
    private String name;
    private double price;
    private String category;
    private String brand;
    private String description;
    private String imageUrl;

    public Product(String id, String name, double price, String category, String brand, String description, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.brand = brand;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", brand='" + brand + '\'' +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
