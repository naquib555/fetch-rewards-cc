package com.fetchrewards.points.resource;

import com.fetchrewards.points.models.ApiResponse;
import com.fetchrewards.points.models.AppException;
import com.fetchrewards.points.models.Spend;
import com.fetchrewards.points.models.Transaction;
import com.fetchrewards.points.service.PointsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = {"/api/v1/points"})
public class PointsResource {

    private PointsService pointsService;

    public PointsResource(PointsService pointsService) {
        this.pointsService = pointsService;
    }

    @PostMapping("add")
    public ResponseEntity<ApiResponse> add(@Valid @RequestBody Transaction transaction) {
        pointsService.addTransaction(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, "success", new ArrayList<>()
                ));
    }

    @PostMapping("spend")
    public ResponseEntity<ApiResponse> spend(@Valid @RequestBody Spend spend) {
        return ResponseEntity.ok(new ApiResponse(true, "success", pointsService.spend(spend)));
    }

    @GetMapping("balance")
    public ResponseEntity<ApiResponse> balance() {
        return ResponseEntity.ok(new ApiResponse(true, "success", pointsService.getBalance()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ApiResponse(false, "Validation Error", errors);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AppException.class)
    public ApiResponse handleValidationExceptions(
            AppException ex) {

        return new ApiResponse(false, ex.getMessage(), null);
    }

}
