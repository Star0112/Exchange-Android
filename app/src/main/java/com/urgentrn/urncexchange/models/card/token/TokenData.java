package com.urgentrn.urncexchange.models.card.token;

import java.io.Serializable;

public class TokenData implements Serializable {

    private String token;
    private String card_token;
    private String state;
    private String state_reason;
    private String fulfillment_status;
    private String issuer_eligibility_decision;
    private String created_time;
    private String last_modified_time;
    private TokenServiceProvider token_service_provider;
    private Device device;
    private WalletProviderProfile wallet_provider_profile;
    private AddressVerification address_verification;

    private class TokenServiceProvider implements Serializable {

        private String token_reference_id;
        private String pan_reference_id;
        private String token_requestor_id;
        private String token_requestor_name;
        private String token_type;
        private String token_score;
        private String token_eligibility_decision;
    }

    private class Device implements Serializable {

        private String type;
        private String language_code;
        private String device_id;
        private String phone_number;
        private String name;
        private String location;
        private String ip_address;
    }

    private class AddressVerification implements Serializable {

        private String name;
        private String street_address;
        private String zip;
    }
}
