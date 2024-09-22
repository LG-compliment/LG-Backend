package com.ureca.compliment.compliment.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ureca.compliment.user.dto.UserDTO;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ComplimentDTO {
    private String id;
    private String content;
    private boolean isAnonymous;
    private Date createAt;

    private String senderId;
    private String receiverId;
    private UserDTO sender;
    private UserDTO receiver;


    public ComplimentDTO(String id, String content, boolean isAnonymous, Date createAt) {
        this.id = id;
        this.content = content;
        this.isAnonymous = isAnonymous;
        this.createAt = createAt;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public UserDTO getSender() {
        return sender;
    }

    public void setSender(UserDTO sender) {
        this.sender = sender;
    }

    public UserDTO getReceiver() {
        return receiver;
    }

    public void setReceiver(UserDTO receiver) {
        this.receiver = receiver;
    }


}
