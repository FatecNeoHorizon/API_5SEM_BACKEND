package com.neohorizon.api.controller.comum;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public abstract class BaseController {

    protected ResponseEntity <Void> noContent() {
        return ResponseEntity.noContent().build();
    }

    protected <T> ResponseEntity<T> ok(T body) {
        return ResponseEntity.ok(body);
    }

    protected <T> ResponseEntity<T> created(T body) {
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    protected <T> ResponseEntity<List<T>> okList(List<T> body) {
        return ResponseEntity.ok(body);
    }

    protected <T> ResponseEntity<T> notFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    protected <T> ResponseEntity<T> badRequest() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

}
