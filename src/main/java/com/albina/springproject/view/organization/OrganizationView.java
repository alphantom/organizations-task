package com.albina.springproject.view.organization;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class OrganizationView {

    public Long id;

    @NotEmpty(message = "Organization's name can't be null")
    @Size(max = 50)
    public String name;

    public Boolean isActive;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Organization{");
        sb.append("id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", isActive=").append(isActive);
        sb.append('}');
        return sb.toString();
    }
}
