package com.ureca.compliment.util.auth.service;

public interface TokenService {
    String generateToken(String id, String password);
}
