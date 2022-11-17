package com.fetchrewards.points.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ApiResponse<T> {

    private boolean status;
    private String message;
    private T data;
}
