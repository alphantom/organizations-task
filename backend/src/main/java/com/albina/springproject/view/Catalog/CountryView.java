package com.albina.springproject.view.Catalog;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class CountryView {

    @NotEmpty(message = "Country's code can't be null")
    private short code;

    @NotEmpty(message = "Country's name can't be null")
    @Size(max = 70)
    private String name;
}
