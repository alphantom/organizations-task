package com.albina.springproject.models;

import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Version;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "office")
public class Office {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(length = 60, nullable = false)
    private String name;

    @Column(length = 255, nullable = false)
    private String address;

    @Column(length = 15)
    private String phone;

    @Column(name = "active", nullable = false)
    private boolean isActive;

    @ManyToMany(mappedBy="offices", cascade = {
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH }, fetch = FetchType.LAZY)
    private Set<Organization> organizations = new HashSet<>();

    @OneToMany(mappedBy="office", cascade = {
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH }, fetch = FetchType.LAZY)
    private Set<Person> persons = new HashSet<>();

    @Version
    private Integer version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean active) {
        isActive = active;
    }

    public Set<Organization> getOrganizations() {
        return organizations;
    }

    public void addOrganization(Organization organization) {
        if (null != organizations)
            organizations.add(organization);
    }

    public void removeOrganization(Organization organization) {
        organizations.remove(organization);
    }

    public void removeOrganizations(Set<Organization> organizations) {
        this.organizations.removeAll(organizations);
    }

    public Set<Person> getPersons() {
        return persons;
    }

    public void addPerson(Person person) {
        if (null != persons)
            persons.add(person);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Office)) return false;
        Office that = (Office) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(address, that.address) &&
                Objects.equals(organizations, that.organizations) &&
                Objects.equals(phone, that.phone) &&
                Objects.equals(isActive, that.isActive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, address, organizations, phone, isActive);
    }

}
