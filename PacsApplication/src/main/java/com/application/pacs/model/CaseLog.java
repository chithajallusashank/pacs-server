package com.application.pacs.model;

import org.hibernate.annotations.NaturalId;

import com.application.pacs.model.audit.DateAudit;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * User details record model.
 */

@Entity
@Table(name = "caselogs")

public class CaseLog extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 30)
    private Long case_id;

      
    @NotBlank
    private String loguser;
    
  
    @NotBlank
    @Size(max = 200)
    private String logtext;
    
    @NotBlank
    @Enumerated(EnumType.STRING)
    private CaseStatus casestatus;
    
 
    public CaseLog() {

    }

    public CaseLog(Long case_id, String loguser, String logtext, CaseStatus casestatus) {
        this.case_id = case_id;
        this.loguser=loguser;
        this.logtext = logtext;
        this.casestatus = casestatus;
        
       
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCase_id() {
        return case_id;
    }

    public void setCase_id(Long case_id) {
        this.case_id = case_id;
    }

   
    public String getLoguser() {
        return loguser;
    }

    public void setLoguser(String loguser) {
        this.loguser = loguser;
    }
    
    public String getLogtext() {
        return logtext;
    }

    public void setLogtext(String logtext) {
        this.logtext = logtext;
    }

    public CaseStatus getCasestatus() {
        return casestatus;
    }

    public void setCasestatus(CaseStatus casestatus) {
        this.casestatus = casestatus;
    }
    
  
 
	
}