package com.albina.springproject.view;

import com.albina.springproject.models.Office;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class OfficeView {
    public Long id;

    @NotEmpty(message = "Office's name can't be null")
    @Size(max = 60)
    public String name;

    @Size(max = 15)
    public String phone;

    public Boolean isActive;

    @NotEmpty(message = "Office's organization id can't be null")
    public Long organizationId;

    public OfficeView() {}

    public OfficeView(Office office) {

        id = office.getId();
        phone = office.getPhone();
        isActive = office.isActive();
        organizationId = office.getOrganizations().iterator().next().getId();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Office{");
        sb.append("id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", phone=").append(phone);
        sb.append(", organizationId=").append(organizationId);
        sb.append(", isActive=").append(isActive);
        sb.append('}');
        return sb.toString();
    }
}
