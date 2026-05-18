package com.dreamhousesystem.dreamhouse.Messaging;

public class UserEmailMessage {
    private Long user_auth_id;
    private String email;
    private String region ;
    private String region_display;

    public UserEmailMessage() {}

    public UserEmailMessage(Long userId, String email,String region,String region_display) {
        this.user_auth_id = userId;
        this.email = email;
        this.region=region;
        this.region_display=region_display;
    }

    public Long getUserId() { return user_auth_id; }
    public void setUserId(Long userId) { this.user_auth_id = userId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Long getUser_auth_id(){return user_auth_id;}
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
