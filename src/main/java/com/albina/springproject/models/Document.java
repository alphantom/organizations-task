package com.albina.springproject.models;

import com.albina.springproject.models.catalog.DocumentType;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "person")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(name = "date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date date;

    @Column(name = "number", length = 35, nullable = false)
    private String number;

    @Column(name = "type_id", nullable = false, insertable = false, updatable = false)
    private Long typeId;

    @Version
    private Integer version;

    @ManyToOne
    @JoinColumn(name = "type_id")
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

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }
}
