package com.albina.springproject.view.Catalog;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class DocumentView {

    @NotEmpty(message = "Document's code can't be null")
    public byte code;

    @NotEmpty(message = "Document's name can't be null")
    @Size(max = 50)
    public String name;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Document{");
        sb.append("code=").append(code);
        sb.append(", name=").append(name);
        sb.append('}');
        return sb.toString();
    }
}
