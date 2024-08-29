package com.jsotoxml.service;

import com.jayway.jsonpath.JsonPath;
import com.jsotoxml.model.JsonXmlMapping;
import com.jsotoxml.model.Validation;
import com.jsotoxml.repository.JsonXmlMappingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JsonToXmlService {
    @Value("${xml.root.node}")
    String rootNode;

    private final JsonXmlMappingRepository repository;

    @Autowired
    public JsonToXmlService(JsonXmlMappingRepository repository) {
        this.repository = repository;
    }

    public String convertJsonToXml(String json) throws Exception {
        List<JsonXmlMapping> mappings = repository.findAll();
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement(rootNode);
        doc.appendChild(rootElement);

        Map<String, Element> parentElements = new HashMap<>();
        parentElements.put(rootNode, rootElement);

        for (JsonXmlMapping mapping : mappings) {
            Object operand1 = JsonPath.read(json, mapping.getJsonPathOperand1());
            Object operand2 = JsonPath.read(json, mapping.getJsonPathOperand2());
            Object result = applyOperator(operand1, operand2, mapping.getOperator());
            applyValidations(result, mapping.getValidations());

            Element parentElement = parentElements.getOrDefault(mapping.getParentXpath(), rootElement);
            Element element = doc.createElement(mapping.getTargetXpath());
            if (mapping.getXmlAttribute() != null && !mapping.getXmlAttribute().isEmpty()) {
                element.setAttribute(mapping.getXmlAttribute(), result.toString());
            } else {
                element.appendChild(doc.createTextNode(result.toString()));
            }
            parentElement.appendChild(element);
            parentElements.put(mapping.getTargetXpath(), element);

            // Handle dynamic child elements
            String dynamicChildPath = mapping.detectDynamicChildPath();
            if (dynamicChildPath != null) {
                List<Object> childElements = JsonPath.read(json, dynamicChildPath);
                for (Object child : childElements) {
                    Element childElement = doc.createElement(mapping.getTargetXpath());
                    childElement.appendChild(doc.createTextNode(child.toString()));
                    element.appendChild(childElement);
                }
            }
        }

        // Convert Document to String
        return convertDocumentToString(doc);
    }

    private Object applyOperator(Object operand1, Object operand2, String operator) {
        return switch (operator) {
            case "+" -> (Double) operand1 + (Double) operand2;
            case "-" -> (Double) operand1 - (Double) operand2;
            case "x" -> (Double) operand1 * (Double) operand2;
            case "/" -> (Double) operand1 / (Double) operand2;
            case "round" -> Math.round((Double) operand1);
            case "none" -> operand1;
            default -> throw new IllegalArgumentException("Invalid operator");
        };
    }

    private void applyValidations(Object result, List<Validation> validations) {
        for (Validation validation : validations) {
            switch (validation.getValidationType()) {
                case NOT_NULL:
                    if (result == null) {
                        throw new IllegalArgumentException("Validation failed: result is null");
                    }
                    break;
                case GREATER_THAN:
                    if ((Double) result <= 0) {
                        throw new IllegalArgumentException("Validation failed: result is not greater than zero");
                    }
                    break;
                case LESS_THAN:
                    if ((Double) result >= 0) {
                        throw new IllegalArgumentException("Validation failed: result is not less than zero");
                    }
                    break;
                case EQUALS:
                    // Implement equals validation logic
                    break;
                case VALUES:
                    // Implement values validation logic
                    break;
                case BETWEEN:
                    // Implement between validation logic
                    break;
                default:
                    throw new IllegalArgumentException("Invalid validation type");
            }
        }
    }

    private String convertDocumentToString(Document doc) {
        return doc.toString();
    }
}