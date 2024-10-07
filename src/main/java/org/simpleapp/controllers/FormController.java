package org.simpleapp.controllers;

import lombok.RequiredArgsConstructor;
import org.simpleapp.models.Response;
import org.simpleapp.models.SignatureResult;
import org.simpleapp.services.HmacService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author Roland Pilpani 07.10.2024
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/form")
public class FormController {
    private final HmacService hmacService;

    @PostMapping("/submit/{operationId}")
    public Response<List<SignatureResult>> submitForm(@PathVariable String operationId, @RequestParam Map<String, String> formParams) {

        try {
            System.out.println("submitForm for operationId: " + operationId +
                    " and formParams: " + formParams);
            return hmacService.calculateHash(operationId, formParams);
        } catch (Exception e) {
            System.out.println("error in submitForm for operationId: " + operationId +
                    " and formParams: " + formParams +
                    " error: " + e.getMessage());
            return Response.error();
        }
    }

}
