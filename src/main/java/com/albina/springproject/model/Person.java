package com.albina.springproject.model;

import com.albina.springproject.model.catalog.Country;
import com.albina.springproject.model.catalog.Document;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;

    @Column(name = "second_name", length = 50)
    private String secondName;

    @Column(name = "middle_name", length = 50)
    private String middleName;

    @Column(length = 50, nullable = false)
    private String position;

    @Column(length = 15)
    private String phone;

    @Column(name = "doc_date")
    @Temporal(TemporalType.DATE)
    private Date docDate;

    @Column(name = "doc_number", length = 35)
    private String docNumber;

    @Column(nullable = false)
    private boolean identified = false;

    @Column(name = "off_id", nullable = false, insertable = false, updatable = false)
    private Long officeId;

    @Column(name = "doc_id", insertable = false, updatable = false)
    private byte documentId;

    @Column(name = "country_id", insertable = false, updatable = false)
    private short countryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "off_id")
    private Office office;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doc_id")
    private Document document;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    private Country citizenship;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getDocDate() {
        return docDate;
    }

    public void setDocDate(Date docDate) {
        this.docDate = docDate;
    }

    public String getDocNumber() {
        return docNumber;
    }

    public void setDocNumber(String docNumber) {
        this.docNumber = docNumber;
    }

    public boolean isIdentified() {
        return identified;
    }

    public void setIdentified(boolean identified) {
        this.identified = identified;
    }

    public Long getOfficeId() {
        return officeId;
    }

    public void setOfficeId(Long officeId) {
        this.officeId = officeId;
    }

    public byte getDocumentId() {
        return documentId;
    }

    public void setDocumentId(byte documentId) {
        this.documentId = documentId;
    }

    public short getCountryId() {
        return countryId;
    }

    public void setCountryId(short countryId) {
        this.countryId = countryId;
    }

    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public Country getCitizenship() {
        return citizenship;
    }

    public void setCitizenship(Country citizenship) {
        this.citizenship = citizenship;
    }
}
