package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.IntegranteAsserts.*;
import static com.mycompany.myapp.domain.IntegranteTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IntegranteMapperTest {

    private IntegranteMapper integranteMapper;

    @BeforeEach
    void setUp() {
        integranteMapper = new IntegranteMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getIntegranteSample1();
        var actual = integranteMapper.toEntity(integranteMapper.toDto(expected));
        assertIntegranteAllPropertiesEquals(expected, actual);
    }
}
