package com.fetchrewards.points.service;

import com.fetchrewards.points.PointsApplication;
import com.fetchrewards.points.models.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest(classes = PointsApplication.class)
class PointsServiceTest {

    @Test
    @DisplayName("Should add new transaction and update point balance of payer")
    void itShouldAddTransactionAndUpdatePointBalance() {

        // given
        PointsService pointsService = new PointsService();
        Transaction newTransaction = new Transaction("DANNON", 300, LocalDateTime.now());
        List<Transaction> expectedTransactions = Arrays.asList(newTransaction);
        Map<String, Integer> expectedBalance = new HashMap<String, Integer>() {
            {
                put("DANNON", 300);
            }
        };

        // when
        pointsService.addTransaction(newTransaction);

        // then
        Assertions.assertEquals(expectedBalance, pointsService.getBalance());
        Assertions.assertEquals(expectedTransactions, pointsService.getData().getTransactions());
    }

    @Test
    @DisplayName("Should spent points based on oldest timestamp and return the points spend for each payer")
    void itShouldReturnThePointsSpendForEachPayer() {

        // given
        PointsService pointsService = new PointsService();
        Spend pointsToBeSpend = new Spend(5000);
        List<Transaction> transactions = prepareTransactionData();
        Map<String, Integer> expectedBalance = new HashMap<String, Integer>() {
            {
                put("DANNON", 1100);
            }

            {
                put("UNILIVER", 200);
            }

            {
                put("MILLER COORS", 10000);
            }
        };
        List<PointsSpent> expectedPointsSpent = new HashMap<String, Integer>() {
            {
                put("DANNON", -100);
            }

            {
                put("UNILIVER", -200);
            }

            {
                put("MILLER COORS", -4700);
            }
        }.entrySet().stream().sorted(Map.Entry.comparingByKey())
                .map(e -> new PointsSpent(e.getKey(), e.getValue())).collect(Collectors.toList());

        // when
        pointsService.setData(new Data(transactions, expectedBalance));
        List<PointsSpent> actualPointsSpent = pointsService.spend(pointsToBeSpend);

        // then
        Assertions.assertEquals(expectedPointsSpent, actualPointsSpent);
        Assertions.assertEquals(expectedBalance, pointsService.getBalance());

    }

    @Test
    @DisplayName("Should Throw AppException When Spending Points is greater than Available Points")
    void itShouldThrowAppExceptionForSpendingPointsWhenNotEnoughPointsAvailable() {

        // given
        PointsService pointsService = new PointsService();
        Spend pointsToBeSpend = new Spend(150000);
        List<Transaction> transactions = prepareTransactionData();
        Map<String, Integer> expectedBalance = new HashMap<String, Integer>() {
            {
                put("DANNON", 1100);
            }

            {
                put("UNILIVER", 200);
            }

            {
                put("MILLER COORS", 10000);
            }
        };
        String expectedExceptionMessage = "Not enough points to spend";

        // when
        pointsService.setData(new Data(transactions, expectedBalance));
        AppException exception = Assertions.assertThrows(AppException.class, () -> {
            pointsService.spend(pointsToBeSpend);
        });

        // then
        Assertions.assertEquals(expectedExceptionMessage, exception.getMessage());

    }

    @Test
    @DisplayName("Should Update Balance of all the payers based on points spent")
    void itShouldUpdateBalanceBasedOnPointsSpent() {

        // given
        PointsService pointsService = new PointsService();
        Map<String, Integer> preBalance = new HashMap<String, Integer>() {
            {
                put("DANNON", 1100);
            }

            {
                put("UNILIVER", 200);
            }

            {
                put("MILLER COORS", 10000);
            }
        };
        Map<String, Integer> pointsSpent = new HashMap<String, Integer>() {
            {
                put("DANNON", -100);
            }

            {
                put("UNILIVER", -200);
            }

            {
                put("MILLER COORS", -4700);
            }
        };
        Map<String, Integer> expectedBalance = new HashMap<String, Integer>() {
            {
                put("DANNON", 1000);
            }

            {
                put("UNILIVER", 0);
            }

            {
                put("MILLER COORS", 5300);
            }
        };

        // when
        pointsService.setData(new Data(new ArrayList<>(), preBalance));
        pointsService.updateBalance(pointsSpent);

        // then
        Assertions.assertEquals(expectedBalance, pointsService.getBalance());

    }

    @Test
    @DisplayName("Should Update Payer Balance in transaction repo for a particular payer")
    void itShouldUpdatePayerBalanceInTransactionRepo() {
        // given
        PointsService pointsService = new PointsService();
        List<Transaction> preTransactions = new ArrayList<Transaction>(Arrays.asList(
                new Transaction("DANNON",
                        300,
                        LocalDateTime.of(2022, 10, 31, 10, 0, 0, 0))
        ));
        String payerName = "DANNON";
        Integer pointsToBeUpdated = 100;
        List<Transaction> expectedTransactions = new ArrayList<Transaction>(Arrays.asList(
                new Transaction("DANNON",
                        100,
                        LocalDateTime.of(2022, 10, 31, 10, 0, 0, 0))
        ));

        // when
        pointsService.setData(new Data(preTransactions, new HashMap<>()));
        pointsService.updatePayerPoints(payerName, pointsToBeUpdated);

        // then
        Assertions.assertEquals(expectedTransactions, pointsService.getData().getTransactions());
    }


    List<Transaction> prepareTransactionData() {
        Transaction transaction1 = new Transaction("DANNON",
                300,
                LocalDateTime.of(2022, 10, 31, 10, 0, 0, 0));
        Transaction transaction2 = new Transaction("UNILIVER",
                200,
                LocalDateTime.of(2022, 10, 31, 11, 0, 0, 0));
        Transaction transaction3 = new Transaction("DANNON",
                -200,
                LocalDateTime.of(2022, 10, 31, 15, 0, 0, 0));
        Transaction transaction4 = new Transaction("MILLER COORS",
                10000,
                LocalDateTime.of(2022, 11, 01, 14, 0, 0, 0));
        Transaction transaction5 = new Transaction("DANNON",
                1000,
                LocalDateTime.of(2022, 11, 02, 14, 0, 0, 0));

        return new ArrayList<>(Arrays.asList(transaction1, transaction2, transaction3, transaction4, transaction5));
    }
}