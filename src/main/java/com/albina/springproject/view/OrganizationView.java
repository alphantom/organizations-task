package com.albina.springproject.view;

import javax.validation.constraints.NotEmpty;

public class OrganizationView {

    public Long id;

    @NotEmpty(message = "Organization name can't be null")
    public String name;

    @NotEmpty(message = "Organization full name can't be null")
    public String fullName;

    @NotEmpty(message = "Organization inn can't be null")
    public String inn;

    @NotEmpty(message = "Organization kpp can't be null")
    public String kpp;

    @NotEmpty(message = "Organization address can't be null")
    public String address;

    public String phone;

    public Boolean isActive;

    @Override
    public String toString() {

        return "";
    }
}
