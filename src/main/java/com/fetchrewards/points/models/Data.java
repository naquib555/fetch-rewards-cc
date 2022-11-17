package com.fetchrewards.points.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
public class Data {
    // transaction history of all the points awarded
    private List<Transaction> transactions;
    // balance of all the payers
    private Map<String, Integer> balances;
}
