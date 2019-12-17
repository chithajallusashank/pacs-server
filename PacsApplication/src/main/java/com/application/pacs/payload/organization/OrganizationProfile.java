package com.application.pacs.payload.organization;

import java.time.Instant;

public class OrganizationProfile {
    private Long id;
    private String organizationcode;
    private String organizationname;
    private Instant joinedAt;
   

    public OrganizationProfile(Long id, String organizationcode, String organizationname, Instant joinedAt) {
        this.id = id;
        this.organizationcode = organizationcode;
        this.organizationname = organizationname;
        this.joinedAt = joinedAt;
       
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrganizationcode() {
        return organizationcode;
    }

    public void setOrganizationcode(String organizationcode) {
        this.organizationcode = organizationcode;
    }

    public String Organizationname() {
        return organizationname;
    }

    public void setOrganizationname(String organizationname) {
        this.organizationname = organizationname;
    }

    public Instant getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(Instant joinedAt) {
        this.joinedAt = joinedAt;
    }

    

    
}
