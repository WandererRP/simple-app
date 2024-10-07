package org.simpleapp.services;

import org.simpleapp.models.Response;
import org.simpleapp.models.SignatureResult;

import java.util.List;
import java.util.Map;

/**
 * @author Roland Pilpani 07.10.2024
 */
public interface HmacService {
    Response<List<SignatureResult>> calculateHash(String operationId, Map<String, String> formParams);
}
