package com.fetchrewards.points.service;

import com.fetchrewards.points.models.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PointsService {

    // Data to hold transaction records and balance of payers
    private Data data;

    public PointsService() {
        this.data = new Data(new ArrayList<>(), new HashMap<>());
    }

    /**
     * Adds awarded points information into transaction list
     * <p>
     * This method adds the new transaction and update the balance of the payer
     *
     * @param transaction Awarded Points information
     */
    public List<Transaction> addTransaction(Transaction transaction) {

        // updating balance with awarded points
        getData().getBalances().put(transaction.getPayer(),
                getData().getBalances().getOrDefault(transaction.getPayer(), 0) + transaction.getPoints());

        // adding into transaction repo
        getData().getTransactions().add(transaction);

        return getData().getTransactions();
    }

    /**
     * Adds awarded points information into transaction list
     * <p>
     * This method adds the new transaction and update the balance of the payer
     *
     * @param spend Points to be spent
     * @return the amount of points spent for each payer
     */
    public List<PointsSpent> spend(Spend spend) {
        getData().getTransactions().sort(Comparator.comparing(Transaction::getTimestamp));

        Integer totalPoints = getData().getTransactions().stream().collect(Collectors.summingInt(Transaction::getPoints));

        if (totalPoints < spend.getPoints()) {
            throw new AppException("Not enough points to spend");
        }

        // to store all the used transaction
        List<Transaction> usedTransaction = new ArrayList<>();

        // points been spent for each payer
        Map<String, Integer> pointsSpent = new HashMap<>();

        Integer remainingPoints = spend.getPoints();

        int counter = 0;
        while (remainingPoints > 0) {

            Transaction transaction = getData().getTransactions().get(counter++);

            int pointsToSpent = 0;
            if (transaction.getPoints() < remainingPoints) {
                pointsToSpent = transaction.getPoints();
                remainingPoints -= transaction.getPoints();

                usedTransaction.add(transaction);
            } else {
                // updating the points of the payer
                updatePayerPoints(transaction.getPayer(), transaction.getPoints() - remainingPoints);

                pointsToSpent = remainingPoints;
                remainingPoints = 0;
            }

            // setting the points for payer
            pointsSpent.put(transaction.getPayer(), pointsSpent.getOrDefault(transaction.getPayer(), 0) - pointsToSpent);
        }

        // removing all the used transaction from the repo
        getData().getTransactions().removeAll(usedTransaction);

        // updating the current balance after spending points
        updateBalance(pointsSpent);

        return pointsSpent.entrySet().stream().sorted(Map.Entry.comparingByKey())
                .map(e -> new PointsSpent(e.getKey(), e.getValue())).collect(Collectors.toList());
    }

    /**
     * Returns the balance of all payer points
     * <p>
     * This method calls returns the balances of payer points
     *
     * @return object containing payer and their current balance
     */
    public Map<String, Integer> getBalance() {
        if (getData().getBalances().isEmpty())
            throw new AppException("No transaction exists");

        return getData().getBalances();
    }

    /**
     * Update the balances of all the payers
     * <p>
     * This method updates the balance by adding subtracted balance of each payer
     *
     * @param pointsSpent subtracted balance of each payer
     */
    public void updateBalance(Map<String, Integer> pointsSpent) {

        pointsSpent.forEach((k, v) -> {
            getData().getBalances().put(k,
                    getData().getBalances().getOrDefault(k, 0) + v);
        });
    }

    /**
     * Update the points of a specific payer
     * <p>
     * This method updates the points of given payer in the transaction repo
     *
     * @param payer         name of the payer
     * @param updatedPoints points to be updated
     */
    public void updatePayerPoints(String payer, Integer updatedPoints) {

        getData().getTransactions().stream()
                .filter(x -> x.getPayer().equals(payer))
                .findFirst()
                .ifPresent(x -> x.setPoints(updatedPoints));
    }

    /**
     * Return data holding transaction and balance
     *
     * @return data that holds transaction and balance
     */
    public Data getData() {
        return data;
    }

    /**
     * Set data holding transaction and balance
     *
     * @param data set data that holds transaction and balance
     */
    public Data setData(Data data) {
        return this.data = data;
    }
}
