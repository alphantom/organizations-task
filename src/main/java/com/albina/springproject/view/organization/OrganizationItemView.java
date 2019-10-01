package com.albina.springproject.view.organization;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class OrganizationItemView extends OrganizationView {

    @NotEmpty(message = "Organization's fullName can't be null")
    public String fullName;

    @NotEmpty(message = "Organization's inn can't be null")
    @Size(max = 12)
    public String inn;

    @NotEmpty(message = "Organization's kpp can't be null")
    @Size(max = 9)
    public String kpp;

    @NotEmpty(message = "Organization's address can't be null")
    public String address;

    @Size(max = 15)
    public String phone;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Organization{");
        sb.append("id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", fullName=").append(fullName);
        sb.append(", inn=").append(inn);
        sb.append(", kpp=").append(address);
        sb.append(", address=").append(address);
        sb.append(", phone=").append(phone);
        sb.append(", isActive=").append(isActive);
        sb.append('}');
        return sb.toString();
    }

}
