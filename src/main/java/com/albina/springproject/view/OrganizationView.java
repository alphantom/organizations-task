package com.albina.springproject.view;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class OrganizationView {

    public Long id;

    @NotEmpty(message = "Organization name can't be null")
    @Size(max = 50)
    public String name;

    @NotEmpty(message = "Organization full name can't be null")
    public String fullName;

    @NotEmpty(message = "Organization inn can't be null")
    @Size(max = 12)
    public String inn;

    @NotEmpty(message = "Organization kpp can't be null")
    @Size(max = 9)
    public String kpp;

    @NotEmpty(message = "Organization address can't be null")
    public String address;

    @Size(max = 15)
    public String phone;

    public Boolean isActive;

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
