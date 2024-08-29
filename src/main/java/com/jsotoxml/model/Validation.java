package com.jsotoxml.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Validation {
    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    private ValidationType validationType;

    @ManyToOne
    @JoinColumn(name = "json_xml_mapping_id")
    private JsonXmlMapping jsonXmlMapping;

}
