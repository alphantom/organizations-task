package com.albina.springproject.models;

import com.albina.springproject.models.catalog.DocumentType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Version;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.FetchType;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "document")
public class Document {

    @Id
    @GeneratedValue(generator="generator")
    @GenericGenerator(name="generator", strategy="foreign",parameters=@Parameter(name="property", value="person"))
    private Long id;

    @Column(name = "date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date date;

    @Column(name = "number", length = 35, nullable = false)
    private String number;

    @Column(name = "type_id", nullable = false)
    private byte typeId;

    @Version
    private Integer version;

    @OneToOne
    @PrimaryKeyJoinColumn
    private Person person;

    @ManyToOne
    @JoinColumn(name = "type_id", insertable = false, updatable = false)
    private DocumentType documentType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public byte getTypeId() {
        return typeId;
    }

    public void setTypeId(byte typeId) {
        this.typeId = typeId;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Document)) return false;
        Document that = (Document) o;
        return Objects.equals(number, that.number) &&
                Objects.equals(typeId, that.typeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, typeId);
    }
}
