package com.application.pacs.payload.organization;

public class OrganizationIdentityAvailability {
    private Boolean available;

    public OrganizationIdentityAvailability(Boolean available) {
        this.available = available;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
}
