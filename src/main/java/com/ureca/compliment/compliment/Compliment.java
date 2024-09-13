package com.ureca.compliment.compliment;

import java.time.LocalDateTime;

public class Compliment {
    private String id;
    private String senderId;
    private String receiverId;
    private String content;
    private boolean isAnonymous;
    private LocalDateTime createdAt;

    public Compliment(String id, String senderId, String receiverId, String content, boolean isAnonymous) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.isAnonymous = isAnonymous;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isAnonymous() {
        return isAnonymous;
    }

    public void setAnonymous(boolean anonymous) {
        isAnonymous = anonymous;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
