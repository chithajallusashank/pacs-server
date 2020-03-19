package com.application.pacs.payload.users;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class SupervisorListResponse {
  
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
   
}
