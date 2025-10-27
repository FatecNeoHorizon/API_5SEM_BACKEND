package com.neohorizon.api.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import com.neohorizon.api.controller.metrica.MetricsController;
import com.neohorizon.api.service.metrica.DevHoursMetricsService;

import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest(MetricsController.class)
class MetricsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DevHoursMetricsService devHoursMetricsService;
    
    private MockMvcTester mvc;

    @BeforeEach
    void setup() {
        mvc = MockMvcTester.create(mockMvc);
    }

    @Test
    void testWithMockMvcTester() {
        assertThat(mvc.get().uri("/dev-hours")).hasStatusOk();
    }
}

