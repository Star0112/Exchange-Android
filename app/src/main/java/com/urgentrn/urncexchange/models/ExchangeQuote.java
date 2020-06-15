package com.urgentrn.urncexchange.models;

public class ExchangeQuote {

    private int id;
    private ExchangeInfo from_exchange;
    private ExchangeInfo to_exchange;
    private double from_amount;
    private double from_quantity;
    private double to_amount;
    private double to_quantity;
    private double rate;
    private String rateFormatted;
    private String availability;
    private Fee fee;
    private String total;
    private String expire_at;

    public int getId() {
        return id;
    }

    public ExchangeInfo getFromExchange() {
        return from_exchange;
    }

    public ExchangeInfo getToExchange() {
        return to_exchange;
    }

    public double getFromAmount() {
        return from_amount;
    }

    public double getFromQuantity() {
        return from_quantity;
    }

    public double getToAmount() {
        return to_amount;
    }

    public double getToQuantity() {
        return to_quantity;
    }

    public double getRate() {
        return rate;
    }

    public String getRateFormatted() {
        return rateFormatted;
    }

    public String getAvailability() {
        return availability;
    }

    public Fee getFee() {
        return fee;
    }

    public String getTotal() {
        return total;
    }

    public String getExpireAt() {
        return expire_at;
    }

    public class ExchangeInfo {

        private int id;
        private int asset_id;
        private int fund_id;
        private String platform;
        private boolean status;
        private Fund fund;
        private Asset asset;

        public int getId() {
            return id;
        }

        public int getAssetId() {
            return asset_id;
        }

        public int getFundId() {
            return fund_id;
        }

        public String getPlatform() {
            return platform;
        }

        public boolean isStatus() {
            return status;
        }

        public Fund getFund() {
            return fund;
        }

        public Asset getAsset() {
            return asset;
        }
    }

    public class Fee {

        private Asset asset;
        private double amount;
        private String amountFormatted;

        public Asset getAsset() {
            return asset;
        }

        public double getAmount() {
            return amount;
        }

        public String getAmountFormatted() {
            return amountFormatted;
        }
    }
}
