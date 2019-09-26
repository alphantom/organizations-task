package com.albina.springproject.view;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class DocumentView {

    @NotNull
    public String docDate;

    @Size(max = 35)
    @NotNull
    public String docNumber;

    @Size(max = 50)
    @NotNull
    public String docName;

    public DocumentView(String name, String number, String date) {
        docDate = date;
        docName = name;
        docNumber = number;
    }

}
