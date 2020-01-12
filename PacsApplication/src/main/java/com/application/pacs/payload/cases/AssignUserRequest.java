package com.application.pacs.payload.cases;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class AssignUserRequest {
  
    private String username;

    public String getUserName() {
        return username;
    }

    public void setUserName(String username) {
        this.username = username;
    }
}
