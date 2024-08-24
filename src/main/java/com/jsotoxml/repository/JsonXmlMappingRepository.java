package com.jsotoxml.repository;

import com.jsotoxml.model.JsonXmlMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JsonXmlMappingRepository extends JpaRepository<JsonXmlMapping, Long> {
}