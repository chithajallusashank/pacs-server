package com.application.pacs.model;

import org.hibernate.annotations.NaturalId;

import com.application.pacs.model.audit.DateAudit;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * User details record model.
 */

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
            "username"
        }),
        @UniqueConstraint(columnNames = {
            "email"
        }),
        @UniqueConstraint(columnNames = {
                "phonenumber"
            })
})

public class User extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 40)
    private String name;

    @NotBlank
    @Size(max = 15)
    private String username;
    
    @NotBlank
    @Size(max = 15)
    private String organizationcode;
    
    //@NotBlank
    @Size(max = 50)
    private String signature;// need to add signature to payload and other areas to capture and save it
    
    @NaturalId
    @NotBlank
    @Size(max = 40)
    @Email
    private String email;
    
    private Long phonenumber;
    
    
   
    private Boolean userenabled=true;
    
    @NotBlank
    @Size(max = 100)
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "user_organizations",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "organization_id"))
    private Organization organization = new Organization();
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "supervisor", 
            joinColumns =  @JoinColumn(name = "subordinate_id", referencedColumnName = "id") , 
            inverseJoinColumns =  @JoinColumn(name = "supervisor_id", referencedColumnName = "id") )
    private Set<User> supervisors=new HashSet<User>();

    @ManyToMany (mappedBy = "supervisors" )
    private Set<User> subordinates=new HashSet<User>();

    public User() {

    }

    public User(String name, String username,String organizationcode, String email, String password,Long phonenumber,Boolean userenabled) {
        this.name = name;
        this.username = username;
        this.organizationcode=organizationcode;
        this.email = email;
        this.password = password;
        this.userenabled=userenabled;
        this.phonenumber = phonenumber;
       
        
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOrganizationcode() {
        return organizationcode;
    }

    public void setOrganizationcode(String organizationcode) {
        this.organizationcode = organizationcode;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public Boolean getUserenabled() {
        return userenabled;
    }

    public void setUserenabled(Boolean userenabled) {
        this.userenabled = userenabled;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
    
    public Set<User> getSupervisors() {
        return supervisors;
    }

    public void setSupervisors(Set<User> supervisors) {
        this.supervisors = supervisors;
    }
    
    public Set<User> getSubordinates() {
        return subordinates;
    }

    public void setSubordinates(Set<User> subordinates) {
        this.subordinates = subordinates;
    }
    
    
    
    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }
	
}