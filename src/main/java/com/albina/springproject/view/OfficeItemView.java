package com.albina.springproject.view;

import com.albina.springproject.models.Office;
import com.albina.springproject.models.Organization;

import javax.validation.constraints.NotEmpty;
import java.util.Objects;

public class OfficeItemView extends OfficeView {

    @NotEmpty(message = "Office's address can't be null")
    public String address;

    public OfficeItemView() {
    }

    public OfficeItemView(Office office) {
        super(office);
        address = office.getAddress();
    }

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
