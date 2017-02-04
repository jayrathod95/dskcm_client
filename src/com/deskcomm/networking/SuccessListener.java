package com.deskcomm.networking;

import javax.ws.rs.core.Response;

/**
 * Created by Jay Rathod on 18-01-2017.
 */
abstract public class SuccessListener {
    public abstract void onSuccess(Response response);
}
