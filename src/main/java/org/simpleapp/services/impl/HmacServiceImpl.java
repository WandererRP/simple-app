package org.simpleapp.services.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.simpleapp.models.Response;
import org.simpleapp.models.SignatureResult;
import org.simpleapp.services.HmacService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * @author Roland Pilpani 07.10.2024
 */
@Service
@RequiredArgsConstructor
public class HmacServiceImpl implements HmacService {
    @Value("${hmac.secret}")
    private String SECRET_KEY;

    @Override
    public Response<List<SignatureResult>> calculateHash(String operationId, Map<String, String> formParams) {
        TreeMap<String, String> sortedParams = new TreeMap<>(formParams);
        String dataString = sortedParams.entrySet()
                .stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));
        System.out.println("Data: " + dataString);
        HmacUtils hmacUtils = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, SECRET_KEY);
        String signature = hmacUtils.hmacHex(dataString);
        return Response.success(List.of(new SignatureResult(signature)));
    }
}
