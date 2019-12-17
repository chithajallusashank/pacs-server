package com.application.pacs.payload.users;

public class enabledisableUsername {
    
    private String username;
    private Boolean enable;

    public enabledisableUsername(String username,Boolean enable) {
      
        this.username = username;
        this.enable = enable;
    }

    

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }
}
