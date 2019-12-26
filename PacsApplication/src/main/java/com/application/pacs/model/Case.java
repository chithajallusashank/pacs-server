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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    private String filename;

    @NotBlank
    @Size(max = 15)
    private String fileuri;
    
    @NotBlank
    @Size(max = 15)
    private CaseType casetype;
    
    @Lob
    private String filereport;
    
  
    
    @Size(max = 40)
    private String signatureuri;
    
    @NotBlank
    @Enumerated(EnumType.STRING)
    private CaseStatus casestatus;
    
    @NotBlank
    private Boolean emergency;
   
    
    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "case_id")
    private Set<CaseLog> caselog = new HashSet<>();
    

    public Case() {

    }

    public Case(String filename, String fileuri,String filereport, String signatureuri, CaseStatus casestatus,Boolean emergency) {
        this.filename = filename;
        this.fileuri = fileuri;
        this.filereport=filereport;
        this.casetype=casetype;
        this.signatureuri = signatureuri;
        this.casestatus = casestatus;
        this.emergency=emergency;
       
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
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