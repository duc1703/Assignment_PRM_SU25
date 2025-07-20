package com.example.assignment_prm_su25.model;

public class Product {
    private int id;
    private String name;
    private String description;
    private double price;
    private String image;
    private int categoryId;
    private int rate; // 1-5

    public Product() {}

    public Product(int id, String name, String description, double price, String image, int categoryId, int rate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
        this.categoryId = categoryId;
        this.rate = rate;
    }

    public Product(String name, String description, double price, String image, int categoryId, int rate) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
        this.categoryId = categoryId;
        this.rate = rate;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    public int getRate() { return rate; }
    public void setRate(int rate) { this.rate = rate; }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", image='" + image + '\'' +
                ", categoryId=" + categoryId +
                ", rate=" + rate +
                '}';
    }
} 