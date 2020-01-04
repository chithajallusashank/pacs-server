package com.application.pacs.payload.cases;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.application.pacs.model.CaseType;







public class AddCaseResponse {

	public AddCaseResponse(){
		
	}
	    @NotBlank
	    private Long caseid;
	    
	 
	    private Boolean error;
	    private String errormessage;
	   

	    public Long getCaseid() {
	        return caseid;
	    }

	    public void setCaseid(Long caseid) {
	        this.caseid = caseid;
	    }
	    
	    public Boolean getError() {
	        return error;
	    }

	    public void setError(Boolean error) {
	        this.error = error;
	    }
	    
	    public String getErrormessage() {
	        return errormessage;
	    }

	    public void setErrormessage(String errormessage) {
	        this.errormessage = errormessage;
	    }

	    
	    
	    
}
