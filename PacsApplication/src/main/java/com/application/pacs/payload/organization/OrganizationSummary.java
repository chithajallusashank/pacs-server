package com.application.pacs.payload.organization;

public class OrganizationSummary {
    private Long id;
    private String organizationcode;
    private String organizationname;

    public OrganizationSummary(Long id, String organizationcode, String organizationname) {
        this.id = id;
        this.organizationcode = organizationcode;
        this.organizationname = organizationname;
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

    public String getOrganizationname() {
        return organizationname;
    }

    public void setOrganizationname(String organizationname) {
        this.organizationname = organizationname;
    }
}
