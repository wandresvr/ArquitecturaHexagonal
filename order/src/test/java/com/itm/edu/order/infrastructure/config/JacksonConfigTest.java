package com.itm.edu.order.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JacksonConfigTest {

    @Test
    void testObjectMapperConfiguration() {
        // Arrange
        JacksonConfig jacksonConfig = new JacksonConfig();
        ObjectMapper objectMapper = jacksonConfig.objectMapper();

        // Assert
        assertNotNull(objectMapper);
        assertFalse(objectMapper.getSerializationConfig().isEnabled(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS));
        assertFalse(objectMapper.getSerializationConfig().isEnabled(com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS));
        assertFalse(objectMapper.getDeserializationConfig().isEnabled(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES));
        assertEquals(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL, objectMapper.getSerializationConfig().getDefaultPropertyInclusion().getValueInclusion());
    }
} 