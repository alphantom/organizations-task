package com.albina.springproject.view.person;

import com.albina.springproject.models.Person;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PersonView {

    public Long id;

    @NotNull(message = "Person's first name can't be null")
    public String firstName;

    @Size(max = 50)
    public String lastName;

    @Size(max = 50)
    public String middleName;

    @NotNull(message = "Person's position can't be null")
    @Size(max = 50)
    public String position;

    public PersonView() {}

    public PersonView(Person person) {
        id = person.getId();
        firstName = person.getFirstName();
        lastName = person.getLastName();
        middleName = person.getMiddleName();
        position = person.getPosition();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Person{");
        sb.append("id=").append(id);
        sb.append(", firstName=").append(firstName);
        sb.append(", lastName=").append(lastName);
        sb.append(", middleName=").append(middleName);
        sb.append(", position=").append(position);
        sb.append('}');
        return sb.toString();
    }
}
