package com.example.assignment_prm_su25.model;

public class ProductVariant {
    private int id;
    private int productId;
    private String color;
    private String size;
    private int stock;

    public ProductVariant() {}

    public ProductVariant(int id, int productId, String color, String size, int stock) {
        this.id = id;
        this.productId = productId;
        this.color = color;
        this.size = size;
        this.stock = stock;
    }

    public ProductVariant(int productId, String color, String size, int stock) {
        this.productId = productId;
        this.color = color;
        this.size = size;
        this.stock = stock;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    @Override
    public String toString() {
        return "ProductVariant{" +
                "id=" + id +
                ", productId=" + productId +
                ", color='" + color + '\'' +
                ", size='" + size + '\'' +
                ", stock=" + stock +
                '}';
    }
} 