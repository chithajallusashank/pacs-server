package com.application.pacs.payload.organization;

public class EnableDisableOrganizationcode {
    
    private String organizationcode;
    private Boolean enable;

    public EnableDisableOrganizationcode(String organizationcode,Boolean enable) {
      
        this.organizationcode = organizationcode;
        this.enable = enable;
    }

    

    public String getOrganizationcode() {
        return organizationcode;
    }

    public void setOrganizationcode(String organizationcode) {
        this.organizationcode = organizationcode;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }
}
