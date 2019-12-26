package com.application.pacs.model;

import org.hibernate.annotations.NaturalId;

import com.application.pacs.model.audit.DateAudit;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

/**
 * User details record model.
 */

@Entity
@Table(name = "organizations", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
            "organizationcode"
        }),
        @UniqueConstraint(columnNames = {
            "email"
        }),
        @UniqueConstraint(columnNames = {
                "phonenumber"
            })
})

public class Organization extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 40)
    private String organizationname;

    @NotBlank
    @Size(max = 15)
    private String organizationcode;
    
    @NaturalId
    @NotBlank
    @Size(max = 40)
    @Email
    private String email;
    
    private Long phonenumber;
    
    
   
    private Boolean organizationenabled=true;
    


  
    public Organization() {

    }

    public Organization(String organizationname, String organizationcode, String email, Long phonenumber,Boolean organizationenabled) {
        this.organizationname = organizationname;
        this.organizationcode = organizationcode;
        this.email = email;
        this.organizationenabled=organizationenabled;
        this.phonenumber = phonenumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrganizationname() {
        return organizationname;
    }

    public void setOrganizationname(String organizationname) {
        this.organizationname = organizationname;
    }

    public String getOrganizationcode() {
        return organizationcode;
    }

    public void setOrganizationcode(String organizationcode) {
        this.organizationcode = organizationcode;
    }
    
    public Boolean getOrganizationenabled() {
        return organizationenabled;
    }

    public void setOrganizationenabled(Boolean organizationenabled) {
        this.organizationenabled = organizationenabled;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public Long getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(Long phonenumber) {
        this.phonenumber = phonenumber;
    }

}