package com.urgentrn.urncexchange.models.card.token;

import java.io.Serializable;
import java.util.HashMap;

public class WalletProviderProfile implements Serializable {

    private HashMap<String, String> account;
    private HashMap<String, String> risk_assessment;
    private String device_score;

    private String getEmailAddress() {
        return account.get("email_address");
    }

    private String getAccountScore() {
        return account.get("score");
    }

    private String getRiskAssessmentScore() {
        return risk_assessment.get("score");
    }

    private String getRiskAssessmentVersion() {
        return risk_assessment.get("version");
    }

    public String getDeviceScore() {
        return device_score;
    }
}
