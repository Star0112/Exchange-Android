package com.urgentrn.urncexchange.models;

import java.util.HashMap;
import java.util.List;

public class TierLevel {

    private String title;
    private HashMap<String, Period> periods;

    public String getTitle() {
        return title;
    }

    public HashMap<String, Period> getPeriods() {
        return periods;
    }

    public class Period {

        private String title;
        private List<Limit> limits;

        public String getTitle() {
            return title;
        }

        public List<Limit> getLimits() {
            return limits;
        }

        public class Limit {

            private String title;
            private double amount;
            private String amountFormatted;
            private boolean noLimit;

            public Limit(String title, String amountFormatted) {
                this.title = title;
                this.amountFormatted = amountFormatted;
            }

            public String getTitle() {
                return title;
            }

            public double getAmount() {
                return amount;
            }

            public String getAmountFormatted() {
                return amountFormatted;
            }

            public boolean isNoLimit() {
                return noLimit;
            }
        }
    }
}
