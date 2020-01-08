package com.application.pacs.payload.cases;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.application.pacs.model.BodyPartType;
import com.application.pacs.model.CaseStatus;
import com.application.pacs.model.CaseType;







public class CaseResponse {
	 
	    private String patientname;
	    
	    public CaseResponse() {
		}

	   
	    private String fileuri;

	   
	    private CaseType casetype;
	    
	    private BodyPartType bodyparttype;
	    
	    private String assignedto;
	    private String patientid;
	    private String patienthistory;
	    private Boolean emergency;
	    private CaseStatus casestatus;

	    public String getAssignedto() {
	        return assignedto;
	    }

	    public void setAssignedto(String assignedto) {
	        this.assignedto = assignedto;
	    } 
	    
	    
	    public CaseStatus getCasestatus() {
	        return casestatus;
	    }

	    public void setCasestatus(CaseStatus casestatus) {
	        this.casestatus = casestatus;
	    } 
	    
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
	    
	    public BodyPartType getBodyparttype() {
	        return bodyparttype;
	    }

	    public void setBodyparttype(BodyPartType bodyparttype) {
	        this.bodyparttype = bodyparttype;
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
