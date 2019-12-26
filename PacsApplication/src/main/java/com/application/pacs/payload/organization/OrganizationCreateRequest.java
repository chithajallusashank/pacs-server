package com.application.pacs.payload.organization;

import javax.validation.constraints.*;

/**
 * Created by rajeevkumarsingh on 02/08/17.
 */


public class OrganizationCreateRequest {
    @NotBlank
    @Size(min = 1, max = 40)
    private String organizationname;

    @NotBlank
    @Size(min = 1, max = 30)
    private String organizationcode;

    @NotBlank
    @Size(max = 40)
    @Email
    private String email;
    
 
    private Long phonenumber;


    public String getOrganizationName() {
        return organizationname;
    }

    public void setorganizationname(String organizationname) {
        this.organizationname = organizationname;
    }

    public String getOrganizationCode() {
        return organizationcode;
    }

    public void setorganizationcode(String organizationcode) {
        this.organizationcode = organizationcode;
    }

    public Long getPhonenumber() {
        return phonenumber;
    }

    public void setphonenumber(Long phonenumber) {
        this.phonenumber = phonenumber;
    }
    
    public String getEmail() {
        return email;
    }

    public void setemail(String email) {
        this.email = email;
    }

   
}
