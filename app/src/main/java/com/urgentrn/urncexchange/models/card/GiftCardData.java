package com.urgentrn.urncexchange.models.card;

import java.util.List;

public class GiftCardData {

    private List<GiftCard> refs;
    private GiftFee fee;
    private Meta meta;

    public List<GiftCard> getRefs() {
        return refs;
    }

    public double getFee() {
        return fee.amount;
    }

    public int getPage() {
        return meta != null ? meta.page : 0;
    }

    public int getTotalPages() {
        return meta != null ? meta.totalPages : 0;
    }

    public int getTotal() {
        return meta != null ? meta.total : 0;
    }

    private class GiftFee {

        private String um;
        private double amount;
    }

    private class Meta {

        private int page;
        private int totalPages;
        private String limit;
        private int total;
        private Link links;

        private class Link {

            private String nextPageUrl;
            private String previousPageUrl;
        }
    }
}
