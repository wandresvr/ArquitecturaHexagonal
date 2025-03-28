package com.wilson.order.domain.model;

public class Product {

    private String sku;
    private String name;
    private Float price;
    private String imageUrl;
    private String description;
    private String category;
    private String brand;
    private String status;
    private boolean stockAvailability;
    private int stockQuantity;

    public Product(String sku, String name, Float price, String imageUrl, String description, String category,
            String brand, String status, boolean stockAvailability, int stockQuantity) {
        this.sku = sku;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.description = description;
        this.category = category;
        this.brand = brand;
        this.status = status;
        this.stockAvailability = stockAvailability;
        this.stockQuantity = stockQuantity;
    }
    public String getSku() {
        return sku;
    }
    public void setSku(String sku) {
        this.sku = sku;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Float getPrice() {
        return price;
    }
    public void setPrice(Float price) {
        this.price = price;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getBrand() {
        return brand;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public boolean isStockAvailability() {
        return stockAvailability;
    }
    public void setStockAvailability(boolean stockAvailability) {
        this.stockAvailability = stockAvailability;
    }
    public int getStockQuantity() {
        return stockQuantity;
    }
    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    @Override
    public String toString() {
        return "Product{" +
                "sku='" + sku + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", imageUrl='" + imageUrl + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", brand='" + brand + '\'' +
                ", status='" + status + '\'' +
                ", stockAvailability=" + stockAvailability +
                ", stockQuantity=" + stockQuantity +
                '}';
    }


}
