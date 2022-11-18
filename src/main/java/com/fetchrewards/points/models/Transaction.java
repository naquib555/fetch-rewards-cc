package com.fetchrewards.points.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@JsonIgnoreProperties({"id"})
public class Transaction {

    private Integer id;
    @NotBlank(message = "Payer is required")
    private String payer;
    @NotNull(message = "Points is mandatory")
    private Integer points;
    @NotNull(message = "Timestamp is mandatory")
    private LocalDateTime timestamp;
}
