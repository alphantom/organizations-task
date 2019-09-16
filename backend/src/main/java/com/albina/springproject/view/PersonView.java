package com.albina.springproject.view;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;

public class PersonView {

    public Long id;

    @NotEmpty(message = "Person's first name can't be null")
    public String firstName;

    @Size(max = 50)
    public String secondName;

    @Size(max = 50)
    public String middleName;

    @NotEmpty(message = "Person's position can't be null")
    @Size(max = 50)
    public String position;

    @Size(max = 15)
    public String phone;

    public Date docDate;

    @Size(max = 35)
    public String docNumber;

    @NotEmpty(message = "Person must be identified or not")
    public Boolean identified;

    @NotEmpty(message = "Person's office id can't be null")
    public Long officeId;

    public Byte documentId;

    public Short countryId;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Person{");
        sb.append("id=").append(id);
        sb.append(", firstName=").append(firstName);
        sb.append(", secondName=").append(secondName);
        sb.append(", middleName=").append(middleName);
        sb.append(", position=").append(position);
        sb.append(", phone=").append(phone);
        sb.append(", docDate=").append(docDate);
        sb.append(", docNumber=").append(docNumber);
        sb.append(", identified=").append(identified);
        sb.append(", officeId=").append(officeId);
        sb.append(", documentId=").append(documentId);
        sb.append(", countryId=").append(countryId);
        sb.append('}');
        return sb.toString();
    }
}
