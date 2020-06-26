package com.urgentrn.urncexchange.models.response;

import java.util.List;

public class BaseResponse {

    private String status;
    private errors errors;

    public String isSuccess() {
        return status;
    }

    public errors getError() {
        return errors;
    }

    public int getErrorCode() {
        if (errors != null ) {
            return errors.getCode();
        }
        return 0;
    }

    public String getErrorMessage() {
        if (errors != null) {
            final String message = errors.getMessage();
            if (message != null) {
                return message;
            }
        }
        return "unexpected error";
    }

    private class errors {

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
