package com.application.pacs.payload.cases;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class AssigneeResponse {
  
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
    
    private int assignedcasescount;

    public int getAssignedcasescount() {
        return assignedcasescount;
    }

    public void setAssignedcasescount(int i) {
        this.assignedcasescount = i;
    }
}
