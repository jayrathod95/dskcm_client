package com.deskcomm.networking;

import com.deskcomm.exceptions.ResponseException;

/**
 * Created by Jay Rathod on 18-01-2017.
 */
abstract public class ErrorListener {
    public abstract void onError(ResponseException e);
}
