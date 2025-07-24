package com.example.assignment_prm_su25.model;

public class Notification {
    private int id;
    private int userId;         // ID của người dùng nhận thông báo
    private String title;       // Tiêu đề thông báo (ví dụ: "Phản hồi từ Admin")
    private String message;     // Nội dung thông báo (ví dụ: "Admin đã trả lời yêu cầu hỗ trợ của bạn.")
    private long timestamp;     // Thời gian thông báo được tạo
    private boolean isRead;     // Trạng thái đã đọc/chưa đọc
    private String type;        // Loại thông báo (ví dụ: "admin_reply", "order_status_update")
    private int relatedId;      // ID liên quan (ví dụ: messageId nếu là phản hồi admin, orderId nếu là cập nhật trạng thái đơn hàng)

    public Notification() {}

    public Notification(int id, int userId, String title, String message, long timestamp, boolean isRead, String type, int relatedId) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.message = message;
        this.timestamp = timestamp;
        this.isRead = isRead;
        this.type = type;
        this.relatedId = relatedId;
    }

    // --- Getters và Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public int getRelatedId() { return relatedId; }
    public void setRelatedId(int relatedId) { this.relatedId = relatedId; }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                ", isRead=" + isRead +
                ", type='" + type + '\'' +
                ", relatedId=" + relatedId +
                '}';
    }
}