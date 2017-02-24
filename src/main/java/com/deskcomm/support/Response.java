package com.deskcomm.support;

import org.json.JSONException;
import org.json.JSONObject;

import static com.deskcomm.support.Keys.*;

/**
 * Created by Jay Rathod on 19-01-2017.
 */
public class Response<T> {
    private boolean result;
    private int errorType;
    private String errorCode;
    private String message;
    private T data;

    public Response() {
    }

    public Response(String jsonString) {
        try {
            JSONObject builder = new JSONObject(jsonString);
            result = builder.getBoolean(JSON_RESULT);

            errorType = builder.getInt(JSON_ERROR_TYPE);
            if (builder.has(JSON_MESSAGE))
                message = builder.getString(JSON_MESSAGE);
            if (builder.has(JSON_DATA))
                data = (T) builder.get(JSON_DATA);
            if (builder.has(ERROR_CODE))
                errorCode = builder.getString(ERROR_CODE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Response(JSONObject jsonObject) {
        try {
            result = jsonObject.getBoolean(JSON_RESULT);
            errorType = jsonObject.getInt(JSON_ERROR_TYPE);
            message = jsonObject.getString(JSON_MESSAGE);
            data = (T) jsonObject.get(JSON_DATA);
            errorCode = jsonObject.getString(ERROR_CODE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return toJSONObject().toString();
    }

    public JSONObject toJSONObject() {
        return new JSONObject()
                .put(JSON_RESULT, result)
                .put(JSON_ERROR_TYPE, errorType)
                .put(ERROR_CODE, errorCode)
                .put(JSON_MESSAGE, message)
                .put(JSON_DATA, data);
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getErrorType() {
        return errorType;
    }

    public void setErrorType(int errorType) {
        this.errorType = errorType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
