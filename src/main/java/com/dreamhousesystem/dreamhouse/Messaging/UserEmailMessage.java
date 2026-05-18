package com.dreamhousesystem.dreamhouse.Messaging;

public class UserEmailMessage {
    private Long userId;
    private String email;
    private String region ;
    private String region_display;

    public UserEmailMessage() {}

    public UserEmailMessage(Long userId, String email,String region,String region_display) {
        this.userId = userId;
        this.email = email;
        this.region=region;
        this.region_display=region_display;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRegion(){
        return region;
    }

    public String getRegion_display() {
        return region_display;
    }
    public void setRegion(String region){
        this.region=region;
    }

    public void setRegion_display(String region_display) {
        this.region_display = region_display;
    }
}
