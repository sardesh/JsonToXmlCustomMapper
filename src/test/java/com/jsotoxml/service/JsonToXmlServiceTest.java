package com.jsotoxml.service;

import com.jsotoxml.model.JsonXmlMapping;
import com.jsotoxml.model.Validation;
import com.jsotoxml.model.ValidationType;
import com.jsotoxml.repository.JsonXmlMappingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JsonToXmlServiceTest {

    @Mock
    private JsonXmlMappingRepository repository;

    @InjectMocks
    private JsonToXmlService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service.rootNode = "testNode";
    }



    @Test
    void testConvertJsonToXml_withDynamicChildElements() throws Exception {
        String json = "{ \"store\": { \"book\": [ { \"category\": \"reference\", \"price\": 8.95 }, { \"category\": \"fiction\", \"price\": 12.99 } ] } }";
        JsonXmlMapping mapping = new JsonXmlMapping();
        mapping.setJsonPathOperand1("$.store.book[*]");
        mapping.setTargetXpath("book");
        mapping.setOperator("none");
        when(repository.findAll()).thenReturn(Collections.singletonList(mapping));

        String xml = service.convertJsonToXml(json);
        assertTrue(xml.contains("<book>"));
    }

    @Test
    void testConvertJsonToXml_withoutDynamicChildElements() throws Exception {
        String json = "{ \"store\": { \"book\": { \"category\": \"reference\", \"price\": 8.95 } } }";
        JsonXmlMapping mapping = new JsonXmlMapping();
        mapping.setJsonPathOperand1("$.store.book.category");
        mapping.setTargetXpath("category");
        mapping.setOperator("none");
        when(repository.findAll()).thenReturn(Collections.singletonList(mapping));

        String xml = service.convertJsonToXml(json);
        assertTrue(xml.contains("<category>reference</category>"));
    }

    @Test
    void testConvertJsonToXml_invalidJsonPath() {
        String json = "{ \"store\": { \"book\": { \"category\": \"reference\", \"price\": 8.95 } } }";
        JsonXmlMapping mapping = new JsonXmlMapping();
        mapping.setJsonPathOperand1("$.store.invalidPath");
        mapping.setTargetXpath("invalid");
        mapping.setOperator("none");
        when(repository.findAll()).thenReturn(Collections.singletonList(mapping));

        assertThrows(Exception.class, () -> service.convertJsonToXml(json));
    }

    @Test
    void testConvertJsonToXml_withValidations() throws Exception {
        String json = "{ \"store\": { \"book\": { \"price\": 8.95 } } }";
        JsonXmlMapping mapping = new JsonXmlMapping();
        mapping.setJsonPathOperand1("$.store.book.price");
        mapping.setTargetXpath("price");
        mapping.setOperator("none");

        Validation validation = new Validation();
        validation.setValidationType(ValidationType.NOT_NULL);
        mapping.setValidations(Collections.singletonList(validation));

        when(repository.findAll()).thenReturn(Collections.singletonList(mapping));

        String xml = service.convertJsonToXml(json);
        assertTrue(xml.contains("<price>8.95</price>"));
    }

    @Test
    void testConvertJsonToXml_withOperators() throws Exception {
        String json = "{ \"store\": { \"book\": { \"price1\": 8.95, \"price2\": 1.05 } } }";
        JsonXmlMapping mapping = new JsonXmlMapping();
        mapping.setJsonPathOperand1("$.store.book.price1");
        mapping.setJsonPathOperand2("$.store.book.price2");
        mapping.setTargetXpath("totalPrice");
        mapping.setOperator("+");

        when(repository.findAll()).thenReturn(Collections.singletonList(mapping));

        String xml = service.convertJsonToXml(json);
        assertTrue(xml.contains("<totalPrice>10.0</totalPrice>"));
    }
}