package com.urgentrn.urncexchange.models.response;

import java.util.List;

public class BaseResponse {

    private boolean success;
    private List<Error> error;

    public boolean isSuccess() {
        return success;
    }

    public List<Error> getError() {
        return error;
    }

    public int getErrorCode() {
        final List<Error> error = getError();
        if (error != null && error.size() > 0) {
            return error.get(0).code;
        }
        return 0;
    }

    public String getErrorMessage() {
        final List<Error> error = getError();
        if (error != null && error.size() > 0) {
            final String message = error.get(0).message;
            if (message != null) {
                return message;
            }
        }
        return "unexpected error";
    }

    private class Error {

        private int code;
        private String message;

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }
}
