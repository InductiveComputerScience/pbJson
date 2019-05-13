package com.progsbase.libraries.JSON;

public class JSONException extends Throwable {
    public JSONException(String errorMessage) {
        super(errorMessage);
    }

    public JSONException(Exception e) {
        super(e);
    }
}
