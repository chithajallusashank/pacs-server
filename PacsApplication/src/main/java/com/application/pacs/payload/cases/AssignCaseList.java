package com.application.pacs.payload.cases;

import java.util.List;

import com.application.pacs.model.CaseType;
import com.application.pacs.model.User;

public class AssignCaseList {
	
	
	private List<AssignCase> assigncase;

    /**
     * @return the persons
     */
    public List<AssignCase> getAssigncase() {
        return assigncase;
    }

    /**
     * @param persons the persons to set
     */
    public void setAssigncase(List<AssignCase> assigncase) {
        this.assigncase = assigncase;
    }
}
