package com.example.assignment_prm_su25.model;

public class Product {
    private int id;
    private String name;
    private String description;
    private double price;
    private String imageUrl;
    private float rating;
    private int categoryId;
    private String brand;
    private String category;
    private String size;
    private String color;
    private int stock;
    private double discount; // Discount percentage (0-100)
    private boolean isAvailable;
    private String material;
    private String gender; // "Nam", "Nữ", "Unisex", "Trẻ em"

    public Product() {}

    public Product(int id, String name, String description, double price, String imageUrl, float rating, int categoryId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.rating = rating;
        this.categoryId = categoryId;
        this.brand = "";
        this.category = "";
        this.size = "";
        this.color = "";
        this.stock = 0;
        this.discount = 0.0;
        this.isAvailable = true;
        this.material = "";
        this.gender = "";
    }

    // Constructor with all fields
    public Product(int id, String name, String description, double price, String imageUrl, float rating,
                   int categoryId, String brand, String category, String size, String color,
                   int stock, double discount, boolean isAvailable, String material, String gender) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.rating = rating;
        this.categoryId = categoryId;
        this.brand = brand;
        this.category = category;
        this.size = size;
        this.color = color;
        this.stock = stock;
        this.discount = discount;
        this.isAvailable = isAvailable;
        this.material = material;
        this.gender = gender;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public float getRating() { return rating; }
    public void setRating(float rating) { this.rating = rating; }
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }
    
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    
    public double getDiscount() { return discount; }
    public void setDiscount(double discount) { this.discount = discount; }
    
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
    
    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }
    
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    
    // Helper method to get discounted price
    public double getDiscountedPrice() {
        if (discount > 0) {
            return price * (1 - discount / 100);
        }
        return price;
    }
    
    // Helper method to check if product has discount
    public boolean hasDiscount() {
        return discount > 0;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", imageUrl='" + imageUrl + '\'' +
                ", rating=" + rating +
                ", categoryId=" + categoryId +
                ", brand='" + brand + '\'' +
                ", category='" + category + '\'' +
                ", size='" + size + '\'' +
                ", color='" + color + '\'' +
                ", stock=" + stock +
                ", discount=" + discount +
                ", isAvailable=" + isAvailable +
                ", material='" + material + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}