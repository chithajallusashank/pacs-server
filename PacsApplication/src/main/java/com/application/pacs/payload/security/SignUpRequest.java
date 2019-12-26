package com.application.pacs.payload.security;

import javax.validation.constraints.*;

/**
 * Created by rajeevkumarsingh on 02/08/17.
 */

public class SignUpRequest {
    @NotBlank
    @Size(min = 4, max = 40)
    private String name;

    @NotBlank
    @Size(min = 3, max = 15)
    private String username;

    @NotBlank
    @Size(min = 3, max = 15)
    private String organizationcode;
    
    @NotBlank
    @Size(max = 40)
    @Email
    private String email;
    
 
    private Long phonenumber;

    @NotBlank
    @Size(min = 6, max = 20)
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
    public Long getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(Long phonenumber) {
        this.phonenumber = phonenumber;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
