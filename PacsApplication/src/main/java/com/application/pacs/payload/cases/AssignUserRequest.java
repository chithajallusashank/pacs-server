package com.application.pacs.payload.cases;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.application.pacs.service.CaseService;

public class AssignUserRequest {
  
	 private static final Logger logger = LoggerFactory.getLogger(CaseService.class);
    private String assignee;

    public String getUserName() {
    	
        return assignee;
    }

    public void setUserName(String assignee) {
    	logger.info("setting assignee");
        this.assignee = assignee;
    }
 
}
