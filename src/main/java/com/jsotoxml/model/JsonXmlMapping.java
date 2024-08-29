package com.jsotoxml.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
public class JsonXmlMapping {
    @Id
    private Long id;
    private String jsonPathOperand1;
    private String jsonPathOperand2;
    private String operator;
    private String targetXpath;
    @OneToMany(mappedBy = "jsonXmlMapping")
    private List<Validation> validations;
    private String xmlAttribute; // New field for XML attribute
    private String parentXpath;  // New field for parent element
}