package com.urgentrn.urncexchange.models.response;

public class GetApiResponse extends BaseResponse {

    private ApiUrl data;

    public boolean isProduction() {
        return data.mode == 1;
    }

    public String getProduction() {
        return data.production;
    }

    public String getSandbox() {
        return data.sandbox;
    }

    public String getCountry() {
        return data.country;
    }

    private class ApiUrl {

        private int mode;
        private String production;
        private String sandbox;
        private String country;
    }
}
