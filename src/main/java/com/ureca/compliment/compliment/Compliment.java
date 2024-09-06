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
}
