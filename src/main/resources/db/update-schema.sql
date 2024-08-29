CREATE TABLE json_xml_mapping
(
    id                 BIGINT       NOT NULL,
    json_path_operand1 VARCHAR(255) NULL,
    json_path_operand2 VARCHAR(255) NULL,
    operator           VARCHAR(255) NULL,
    target_xpath       VARCHAR(255) NULL,
    xml_attribute      VARCHAR(255) NULL,
    parent_xpath       VARCHAR(255) NULL,
    CONSTRAINT pk_jsonxmlmapping PRIMARY KEY (id)
);

CREATE TABLE validation
(
    id                  BIGINT       NOT NULL,
    validation_type     VARCHAR(255) NULL,
    json_xml_mapping_id BIGINT       NULL,
    CONSTRAINT pk_validation PRIMARY KEY (id)
);

ALTER TABLE validation
    ADD CONSTRAINT FK_VALIDATION_ON_JSON_XML_MAPPING FOREIGN KEY (json_xml_mapping_id) REFERENCES json_xml_mapping (id);