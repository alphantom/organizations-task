package com.albina.springproject.view.office;

import com.albina.springproject.models.Office;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class OfficeView {
    public Long id;

    @NotEmpty(message = "Office's name can't be null")
    @Size(max = 60)
    public String name;

    @Size(max = 15)
    public String phone;

    public Boolean isActive;

    public OfficeView() {}

    public OfficeView(Office office) {
        id = office.getId();
        name = office.getName();
        phone = office.getPhone();
        isActive = office.isActive();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Office{");
        sb.append("id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", phone=").append(phone);
        sb.append(", isActive=").append(isActive);
        sb.append('}');
        return sb.toString();
    }
}
