package com.ureca.compliment.user;

import java.time.LocalDateTime;

public class User {
    private String id;
    private String name;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public User(String id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }
}
