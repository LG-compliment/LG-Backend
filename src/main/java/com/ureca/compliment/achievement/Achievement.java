package com.ureca.compliment.achievement;

public class Achievement {
    private String userId;
    private String userName;
    private Integer complimentsCount;

    public Integer getComplimentsCount() {
        return complimentsCount;
    }

    public void setComplimentsCount(Integer complimentsCount) {
        this.complimentsCount = complimentsCount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Achievement(String userId, String userName, Integer complimentsCount) {
        this.userId = userId;
        this.userName = userName;
        this.complimentsCount = complimentsCount;
    }


}
