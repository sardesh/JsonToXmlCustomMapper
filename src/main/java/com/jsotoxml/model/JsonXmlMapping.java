package com.jsotoxml.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private String xmlAttribute;
    private String parentXpath;

    public String detectDynamicChildPath() {
        Pattern pattern = Pattern.compile("\\$\\..*\\[\\*\\]");
        Matcher matcher = pattern.matcher(jsonPathOperand1);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }
}