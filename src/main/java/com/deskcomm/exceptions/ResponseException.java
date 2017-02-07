package com.deskcomm.exceptions;

import javax.ws.rs.core.Response;

/**
 * Created by Jay Rathod on 22-01-2017.
 */
public class ResponseException extends Throwable {

    private Response.Status status;

    public ResponseException(Response.Status status) {
        this.status = status;
    }


    @Override
    public String getMessage() {
        return status.getStatusCode() + " - " + status.getReasonPhrase();
    }

    public Response.Status getStatus() {
        return status;
    }

    public void setStatus(Response.Status status) {
        this.status = status;
    }
}
