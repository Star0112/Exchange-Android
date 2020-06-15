package com.urgentrn.urncexchange.models;

import com.urgentrn.urncexchange.models.card.CardTransaction;

import java.util.List;

public class CardTransactionsHistory {

    private int page;
    private int totalPages;
    private int limit;
    private int total;
    private Link links;
    private List<CardTransaction> transactions;
    private String message;

    public int getPage() {
        return page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getLimit() {
        return limit;
    }

    public int getTotal() {
        return total;
    }

    public List<CardTransaction> getTransactions() {
        return transactions;
    }

    public String getMessage() {
        return message;
    }

    private class Link {

        private String nextPageUrl;
        private String previousPageUrl;
    }
}
