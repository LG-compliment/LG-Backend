package com.ureca.compliment.user;

import java.time.LocalDateTime;
import java.util.Date;

public class User {
    private String id;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
