package org.simpleapp.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Roland Pilpani 07.10.2024
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private String status;

    private T result;

    public static <T> Response<T> success(T payload) {
        return new Response<>("success", payload);
    }

    public static <T> Response<T> success() {
        return success(null);
    }

    public static <T> Response<T> error() {
        return new Response<>("error", null);
    }
}
