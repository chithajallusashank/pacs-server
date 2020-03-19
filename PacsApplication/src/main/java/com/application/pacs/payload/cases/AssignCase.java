package com.application.pacs.payload.cases;

import java.util.List;

import com.application.pacs.model.CaseType;
import com.application.pacs.model.User;

public class AssignCase {
	
	
	 private Long caseid;
	  private String assignee;

  
	  public String getAssignee() {
	    	
	        return assignee;
	    }

	    public void setAssignee(String assignee) {
	    
	        this.assignee = assignee;
	    }
    

	    public Long getCaseid() {
	    	
	        return caseid;
	    }

	    public void setCaseid(Long caseid) {
	    
	        this.caseid = caseid;
	    }
}
