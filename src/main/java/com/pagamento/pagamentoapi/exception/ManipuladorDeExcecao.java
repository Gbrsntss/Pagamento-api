package com.pagamento.pagamentoapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ManipuladorDeExcecao {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> tratarExcecaoGenerica(Exception ex, WebRequest request) {
        Map<String, Object> corpo = new HashMap<>();
        corpo.put("status", HttpStatus.BAD_REQUEST.value());
        corpo.put("erro", ex.getMessage());
        corpo.put("caminho", request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(corpo, HttpStatus.BAD_REQUEST);
    }
}
