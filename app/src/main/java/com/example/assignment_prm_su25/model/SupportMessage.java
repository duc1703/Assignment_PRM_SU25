package com.example.assignment_prm_su25.model;

public class SupportMessage {
    private int id;
    private int userId;
    private int orderId; // Optional: Link to a specific order if applicable
    private String subject;
    private String message;
    private long timestamp;
    private String status; // e.g., "New", "Read", "Resolved"

    public SupportMessage() {}

    public SupportMessage(int id, int userId, int orderId, String subject, String message, long timestamp, String status) {
        this.id = id;
        this.userId = userId;
        this.orderId = orderId;
        this.subject = subject;
        this.message = message;
        this.timestamp = timestamp;
        this.status = status;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "SupportMessage{" +
                "id=" + id +
                ", userId=" + userId +
                ", orderId=" + orderId +
                ", subject='" + subject + '\'' +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                ", status='" + status + '\'' +
                '}';
    }
}