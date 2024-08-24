package com.jsotoxml;

import org.springframework.boot.SpringApplication;

public class TestDemoApplication {

    public static void main(String[] args) {
        SpringApplication.from(JsonToXmlMapperApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
