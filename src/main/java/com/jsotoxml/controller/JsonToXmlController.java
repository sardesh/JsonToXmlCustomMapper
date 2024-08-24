package com.jsotoxml.controller;

import com.jsotoxml.service.JsonToXmlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class JsonToXmlController {

    @Autowired
    private JsonToXmlService service;

    @PostMapping("/convert")
    public String convertJsonToXml(@RequestBody String json) {
        try {
            return service.convertJsonToXml(json);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}