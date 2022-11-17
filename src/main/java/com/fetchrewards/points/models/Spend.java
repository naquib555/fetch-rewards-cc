package com.fetchrewards.points.models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Spend {
    @NotNull(message = "Points is mandatory")
    @Range(min = 1, message = "Points has to be a positive number greater than 0")
    private Integer points;
}
