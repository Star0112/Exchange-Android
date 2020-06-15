package com.urgentrn.urncexchange.models;

import java.util.List;

public class PlaidData {

    private Institution institution;
    private PlaidAccount account;
    private String account_id;
    private List<PlaidAccount> accounts;
    private String link_session_id;
    private String public_token;

    public void setInstitution(String institution_id, String institution_name) {
        this.institution = new Institution(institution_name, institution_id);
    }

    public void setAccount(PlaidAccount account) {
        this.account = account;
    }

    public void setAccountId(String account_id) {
        this.account_id = account_id;
    }

    public void setAccounts(List<PlaidAccount> accounts) {
        this.accounts = accounts;
    }

    public void setLinkSessionId(String link_session_id) {
        this.link_session_id = link_session_id;
    }

    public void setPublicToken(String public_token) {
        this.public_token = public_token;
    }

    private class Institution {

        private String name;
        private String institution_id;

        private Institution(String name, String institution_id) {
            this.name = name;
            this.institution_id = institution_id;
        }
    }
}
