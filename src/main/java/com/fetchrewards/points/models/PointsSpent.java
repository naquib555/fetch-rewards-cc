package com.fetchrewards.points.models;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class PointsSpent {
    private String payer;
    private Integer points;
}
