package zju.se.management.controller;

import zju.se.management.utils.ResponseData;

public class BaseController {
    public <T extends ResponseData> Response<T> ResponseOK(T data, String message) {
        return new Response<>(Response.OK, data, message);
    }

    public <T> Response<T> ResponseOK(String message) {
        return new Response<>(Response.OK, null, message);
    }

    public <T extends ResponseData> Response<T> ResponseError(T data, String message) {
        return new Response<>(Response.ERROR, data, message);
    }

    public <T> Response<T> ResponseError(String message) {
        return new Response<>(Response.ERROR, null, message);
    }
}
