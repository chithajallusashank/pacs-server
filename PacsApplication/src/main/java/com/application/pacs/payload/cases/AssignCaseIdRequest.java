package com.application.pacs.payload.cases;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.application.pacs.service.CaseService;

public class AssignCaseIdRequest {
	
	 private static final Logger logger = LoggerFactory.getLogger(CaseService.class);
  
    private Long caseid;

    public Long getCaseId() {
        return caseid;
    }

    public void setCaseId(Long caseid) {
    	logger.info("Setting case id");
        this.caseid = caseid;
    }
}
