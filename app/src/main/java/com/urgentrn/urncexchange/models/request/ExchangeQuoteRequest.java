package com.urgentrn.urncexchange.models.request;

public class ExchangeQuoteRequest {

    private int from_exchange_id;
    private int to_exchange_id;
    private Double from_quantity;
    private Double to_quantity;

    private Integer account_id;
    private Integer service_id;

    public void setFromExchangeId(int from_exchange_id) {
        this.from_exchange_id = from_exchange_id;
    }

    public void setToExchangeId(int to_exchange_id) {
        this.to_exchange_id = to_exchange_id;
    }

    public void setFromQuantity(double from_quantity) {
        this.from_quantity = from_quantity;
    }

    public void setToQuantity(double to_quantity) {
        this.to_quantity = to_quantity;
    }

    public void setAccountId(int account_id) {
        this.account_id = account_id;
    }

    public void setServiceId(int service_id) {
        this.service_id = service_id;
    }
}
