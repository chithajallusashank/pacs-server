package com.application.pacs.payload.cases;

import java.util.List;

import com.application.pacs.model.CaseType;
import com.application.pacs.model.User;

public class AssignCase {
	
	
private List<AssignCaseIdRequest> caseid;
private List<AssignUserRequest> user;

    

    

    public List<AssignCaseIdRequest> getCaseIds() {
        return caseid;
    }

    public void setCaseIds(List<AssignCaseIdRequest> caseid) {
        this.caseid = caseid;
    }
    
    public List<AssignUserRequest> getUser() {
		return user;
    	
    }

    public void setUser(List<AssignUserRequest> user) {
		this.user=user;
    	
    }
}
