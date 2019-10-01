package com.albina.springproject.view.person;

import com.albina.springproject.models.Person;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.text.SimpleDateFormat;

public class PersonItemView extends PersonView {

    @Size(max = 15)
    public String phone;

    public String docDate;

    @Size(max = 35)
    public String docNumber;

    @Size(max = 50)
    public String docName;

    @NotNull(message = "Person must be identified or not")
    public Boolean identified;

    @NotNull(message = "Person's office id can't be null")
    public Long officeId;

    public Short citizenshipCode;

    @Size(max = 70)
    public String citizenshipName;

    @Valid
    private DocumentView document;

    public PersonItemView() {
        super();
        if (null != docNumber) {
            document = new DocumentView(docName, docNumber, docDate);
        }
    }

    public PersonItemView(Person person) {
        super(person);
        phone = person.getPhone();
        identified = person.getIdentified();
        officeId = person.getOfficeId();

        if (null != person.getDocument()) {
            docDate = new SimpleDateFormat("MM-dd-yyyy").format(person.getDocument().getDate());
            docNumber = person.getDocument().getNumber();
            docName = person.getDocument().getDocumentType().getName();
            document = new DocumentView(docName, docNumber, docDate);
        }
        if (null != person.getCountry()) {
            citizenshipName = person.getCountry().getName();
            citizenshipCode = person.getCountryId();
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("id=").append(id);
        sb.append(", firstName=").append(firstName);
        sb.append(", lastName=").append(lastName);
        sb.append(", middleName=").append(middleName);
        sb.append(", position=").append(position);
        sb.append(", phone=").append(phone);
        sb.append(", docDate=").append(docDate);
        sb.append(", docNumber=").append(docNumber);
        sb.append(", identified=").append(identified);
        sb.append(", officeId=").append(officeId);
        sb.append(", docName=").append(docName);
        sb.append(", citizenshipCode=").append(citizenshipCode);
        sb.append(", citizenshipName=").append(citizenshipName);
        sb.append('}');
        return sb.toString();
    }
}
