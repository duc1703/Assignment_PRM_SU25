// Rating.java
package com.example.assignment_prm_su25.model;

public class Rating {
    private int id;
    private int productId;
    private int userId;
    private float ratingValue;
    private String reviewComment;
    private long timestamp; // When the review was submitted

    public Rating() {}

    public Rating(int id, int productId, int userId, float ratingValue, String reviewComment, long timestamp) {
        this.id = id;
        this.productId = productId;
        this.userId = userId;
        this.ratingValue = ratingValue;
        this.reviewComment = reviewComment;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public float getRatingValue() { return ratingValue; }
    public void setRatingValue(float ratingValue) { this.ratingValue = ratingValue; }
    public String getReviewComment() { return reviewComment; }
    public void setReviewComment(String reviewComment) { this.reviewComment = reviewComment; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    @Override
    public String toString() {
        return "Rating{" +
                "id=" + id +
                ", productId=" + productId +
                ", userId=" + userId +
                ", ratingValue=" + ratingValue +
                ", reviewComment='" + reviewComment + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}