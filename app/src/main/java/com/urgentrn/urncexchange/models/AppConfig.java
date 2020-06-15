package com.urgentrn.urncexchange.models;

import java.util.HashMap;
import java.util.List;

public class AppConfig {

    private String version;
    private String domain;
    private String environment;
    private int timeout;
    private Restrictions restrictions;
    private HashMap<String, String> integrations;
    private List<Notification> notifications;
    private List<Link> links;
    private TransactionStrings strings;

    public String getVersion() {
        return version;
    }

    public String getDomain() {
        return domain;
    }

    public String getEnvironment() {
        return environment;
    }

    public int getTimeout() {
        return timeout;
    }

    public Restrictions getRestrictions() {
        return restrictions;
    }

    public HashMap<String, String> getIntegrations() {
        return integrations;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public List<Link> getLinks() {
        return links;
    }

    public TransactionStrings getStrings() {
        return strings;
    }

    public class Restrictions {

        HashMap<String, TierLevel> assets;
        HashMap<String, TierLevel> entities;

        public HashMap<String, TierLevel> getAssets() {
            return assets;
        }

        public int getTierLevel(String key) {
            TierLevel asset = assets.get(key);
            return asset == null ? 0 : asset.tier;
        }

        public class TierLevel {

            private int tier;

            public int getTier() {
                return tier;
            }
        }
    }

    private class Notification {

        private String type;
        private String message;
    }

    public class TransactionStrings {

        private HashMap<String, String> transactions;

        public HashMap<String, String> getTransactions() {
            return transactions;
        }
    }
}
