package com.application.pacs.model;

import org.hibernate.annotations.NaturalId;

import com.application.pacs.model.audit.DateAudit;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User details record model.
 */

@Entity
@Table(name = "cases")

public class Case extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Size(max = 150)
    private String patientname;

    @NotBlank
    @Size(max = 200)
    private String fileuri;
    
    
    @Enumerated(EnumType.STRING)
    private CaseType casetype;
    
    @Lob
    private String filereport;
    
  private String patienthistory;
  
  private String patientid;
    
    @Size(max = 40)
    private String signatureuri;
    
   
    @Enumerated(EnumType.STRING)
    private CaseStatus casestatus;
    

    private Boolean emergency;
   
    
    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "case_id")
    private Set<CaseLog> caselog = new HashSet<>();
    

    public Case() {

    }

    public Case(String patientname, String fileuri,CaseType casetype, String patienthistory, String patientid,Boolean emergency) {
        this.patientname = patientname;
        this.fileuri = fileuri;
        this.casetype=casetype;
        this.patienthistory = patienthistory;
        this.patientid = patientid;
        this.emergency=emergency;
        this.casestatus=CaseStatus.CASESTATUS_OPEN;
       
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPatientname() {
        return patientname;
    }

    public void setPatientname(String patientname) {
        this.patientname = patientname;
    }
    
    public String getPatientid() {
        return patientid;
    }

    public void setPatientid(String patientid) {
        this.patientid = patientid;
    }

    public String getFileuri() {
        return fileuri;
    }

    public void setFileuri(String fileuri) {
        this.fileuri = fileuri;
    }
    
    public String getPatienthistory() {
        return patienthistory;
    }

    public void setPatienthistory(String patienthistory) {
        this.patienthistory = patienthistory;
    }
    
    public CaseType getCasetype() {
        return casetype;
    }

    public void setCasetype(CaseType casetype) {
        this.casetype = casetype;
    }
    
    public String getFilereport() {
        return filereport;
    }
    
 

    public void setFilereport(String filereport) {
        this.filereport = filereport;
    }
    
    public String getSignatureuri() {
        return signatureuri;
    }

    public void setSignatureuri(String signatureuri) {
        this.signatureuri = signatureuri;
    }

    public CaseStatus getCasestatus() {
        return casestatus;
    }

    public void setCasestatus(CaseStatus casestatus) {
        this.casestatus = casestatus;
    }
    
    public Boolean getEmergency() {
        return emergency;
    }

    public void setEmergency(Boolean emergency) {
        this.emergency = emergency;
    }
    
    public void setCaselogs(Set<CaseLog> caselog) {
    	this.caselog=caselog;
    	
    }
    
    
    public Set<CaseLog> getCaselogs() {
        return caselog;
    }

  
	
}