package com.application.pacs.payload.cases;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.application.pacs.model.CaseType;







public class AddCase {
	 @NotBlank
	    @Size(min = 1, max = 200)
	    private String patientname;

	    @NotBlank
	    @Size(min = 1, max = 500)
	    private String fileuri;

	   
	    private CaseType casetype;
	    
	 
	    private String patientid;
	    private String patienthistory;
	    private Boolean emergency;

	    public String getPatienthistory() {
	        return patienthistory;
	    }

	    public void setPatienthistory(String patienthistory) {
	        this.patienthistory = patienthistory;
	    }
	    
	    
	    public String getPatientname() {
	        return patientname;
	    }

	    public void setPatientname(String patientname) {
	        this.patientname = patientname;
	    }

	    public String getFileuri() {
	        return fileuri;
	    }

	    public void setFileuri(String fileuri) {
	        this.fileuri = fileuri;
	    }

	    public CaseType getCasetype() {
	        return casetype;
	    }

	    public void setCasetype(CaseType casetype) {
	        this.casetype = casetype;
	    }
	    
	    public String getPatientid() {
	        return patientid;
	    }

	    public void setPatientid(String patientid) {
	        this.patientid = patientid;
	    }
	    public Boolean getEmergency() {
	        return emergency;
	    }

	    public void setEmergency(Boolean emergency) {
	        this.emergency = emergency;
	    }
	    
	    
}
