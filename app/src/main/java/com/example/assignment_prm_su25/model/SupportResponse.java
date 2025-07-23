package com.example.assignment_prm_su25.model;

public class SupportResponse {
    private int id;
    private int messageId; // Liên kết với SupportMessage
    private int adminId;   // ID của admin đã trả lời
    private String responseContent;
    private long timestamp;

    public SupportResponse() {}

    public SupportResponse(int id, int messageId, int adminId, String responseContent, long timestamp) {
        this.id = id;
        this.messageId = messageId;
        this.adminId = adminId;
        this.responseContent = responseContent;
        this.timestamp = timestamp;
    }

    // --- Getters và Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getMessageId() { return messageId; }
    public void setMessageId(int messageId) { this.messageId = messageId; }
    public int getAdminId() { return adminId; }
    public void setAdminId(int adminId) { this.adminId = adminId; }
    public String getResponseContent() { return responseContent; }
    public void setResponseContent(String responseContent) { this.responseContent = responseContent; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    @Override
    public String toString() {
        return "SupportResponse{" +
                "id=" + id +
                ", messageId=" + messageId +
                ", adminId=" + adminId +
                ", responseContent='" + responseContent + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}