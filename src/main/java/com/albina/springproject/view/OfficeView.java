package com.albina.springproject.view;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class OfficeView {
    public Long id;

    @NotEmpty(message = "Office name can't be null")
    @Size(max = 60)
    public String name;

    @NotEmpty(message = "Organization name can't be null")
    public String address;

    @Size(max = 15)
    public String phone;

    public Boolean isActive;

    @NotEmpty(message = "Organization name can't be null")
    public Long organizationId;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Office{");
        sb.append("id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", address=").append(address);
        sb.append(", phone=").append(phone);
        sb.append(", organizationId=").append(organizationId);
        sb.append(", isActive=").append(isActive);
        sb.append('}');
        return sb.toString();
    }
}
