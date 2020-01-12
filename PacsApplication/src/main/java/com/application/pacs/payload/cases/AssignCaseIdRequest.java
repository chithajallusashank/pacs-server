package com.application.pacs.payload.cases;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class AssignCaseIdRequest {
  
    private Long caseid;

    public Long getCaseId() {
        return caseid;
    }

    public void setCaseId(Long caseid) {
        this.caseid = caseid;
    }
}
