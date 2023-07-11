package com.authservice.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

public class ErrorResponseHandler {

    public static Mono<Void> handleErrorResponse(ServerHttpResponse response, HttpStatus status, String errorMessage) {
        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String errorBody = "{\"error\": \"" + errorMessage + "\"}";
        return response.writeWith(Mono.just(response.bufferFactory().wrap(errorBody.getBytes())));
    }
}
