package org.simpleapp.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.simpleapp.models.Response;
import org.simpleapp.models.SignatureResult;
import org.simpleapp.services.HmacService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Roland Pilpani 07.10.2024
 */
@SpringBootTest
@AutoConfigureMockMvc
public class FormControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HmacService hmacService;

    @BeforeEach
    public void setUp() {
        when(hmacService.calculateHash(anyString(), anyMap()))
                .thenReturn(Response.success(List.of(new SignatureResult("test_signature"))));
    }

    @Test
    public void testSubmitFormSuccess() throws Exception {
        mockMvc.perform(post("/form/submit/123")
                        .header("Token", "token123")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name1", "value1")
                        .param("name2", "value2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"status\":\"success\",\"result\":[{\"signature\":\"test_signature\"}]}"));
    }

    @Test
    public void testSubmitFormExceptionHandling() throws Exception {
        when(hmacService.calculateHash(anyString(), anyMap())).thenThrow(new RuntimeException("Test exception"));
        mockMvc.perform(post("/form/submit/123")
                        .header("Token", "token123")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name1", "value1")
                        .param("name2", "value2"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"status\":\"error\"}"));
    }
}
