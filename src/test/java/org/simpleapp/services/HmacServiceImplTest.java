package org.simpleapp.services;

import org.junit.jupiter.api.Test;
import org.simpleapp.models.Response;
import org.simpleapp.models.SignatureResult;
import org.simpleapp.services.impl.HmacServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Roland Pilpani 08.10.2024
 */
@SpringBootTest
public class HmacServiceImplTest {

    @Value("${hmac.secret}")
    private String SECRET_KEY;

    @Autowired
    private HmacServiceImpl hmacService;

    @Test
    public void testCalculateHash() {
        assertNotNull(SECRET_KEY, "SECRET_KEY должен быть инициализирован");
        System.out.println(SECRET_KEY);
        Map<String, String> formParams = Map.of("name1", "value1", "name2", "value2");

        Response<List<SignatureResult>> response = hmacService.calculateHash("operation123", formParams);

        assertEquals("success", response.getStatus());
        assertNotNull(response.getResult().get(0).getSignature());
    }

    @Test
    public void testCalculateHashWithDifferentOrders() {
        Map<String, String> formParams1 = new LinkedHashMap<>();
        formParams1.put("b", "value2");
        formParams1.put("a", "value1");
        formParams1.put("c", "value3");

        Map<String, String> formParams2 = new LinkedHashMap<>();
        formParams2.put("c", "value3");
        formParams2.put("a", "value1");
        formParams2.put("b", "value2");


        Response<List<SignatureResult>> response1 = hmacService.calculateHash("operation123", formParams1);
        Response<List<SignatureResult>> response2 = hmacService.calculateHash("operation123", formParams2);
        assertEquals(response1.getResult().get(0).getSignature(), response2.getResult().get(0).getSignature());
    }
}
