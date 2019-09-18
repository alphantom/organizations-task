package com.albina.springproject.models;

import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Version;
import javax.persistence.ManyToMany;
import javax.persistence.JoinTable;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "organization")
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(name = "fullname", nullable = false)
    private String fullName;

    @Column(length = 12, nullable = false, unique = true)
    private String inn;

    @Column(length = 9, nullable = false)
    private String kpp;

    @Column(length = 255, nullable = false)
    private String address;

    @Column(length = 15)
    private String phone;

    @Column(name = "active", nullable = false)
    private boolean isActive;

    @Version
    private Integer version;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE }, fetch = FetchType.LAZY)
    @JoinTable(
            name = "organization_office ",
            joinColumns = { @JoinColumn(name = "org_id") },
            inverseJoinColumns = { @JoinColumn(name = "off_id") }
    )
    private Set<Office> offices = new HashSet<>();

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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    public String getKpp() {
        return kpp;
    }

    public void setKpp(String kpp) {
        this.kpp = kpp;
    }

    public String getAddress() {
        return address;
    }

    public Set<Office> getOffices() {
        return offices;
    }

    public void addOffice(Office office) {
        this.offices.add(office);
    }

    public void removeOffice(Office office) {
        this.offices.remove(office);
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

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }


}
