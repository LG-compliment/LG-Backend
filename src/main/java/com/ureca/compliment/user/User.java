package com.ureca.compliment.user;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public class User {
    private String id;
    private String slackId;
    private String name;
    private String password;
    private Date createdAt;
    private Date updatedAt;

    public User(String id, String name, String password, Date createdAt, Date updatedAt) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public User(String id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    // UUID를 생성하여 User 객체 생성
    public User(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.password = ""; // OAuth 사용 시 패스워드는 빈 값 또는 기본값 설정
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getSlackId() {
        return slackId;
    }

    public void setSlackId(String slackId) {
        this.slackId = slackId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
