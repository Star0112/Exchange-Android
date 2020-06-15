package com.urgentrn.urncexchange.models.request;

public class GetVersionsRequest {

    private String version;
    private String environment;

    public GetVersionsRequest(String version, String environment) {
        this.version = version;
        this.environment = environment;
    }
}
