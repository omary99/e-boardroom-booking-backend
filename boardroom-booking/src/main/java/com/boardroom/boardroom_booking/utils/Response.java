package com.boardroom.boardroom_booking.utils;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Setter
@Getter
public class Response<T> {

    private Boolean status;

    private Integer code;

    private String message;

    private T data;

    private List<T> dataList;

    public Response() {
    }

    public Response(Boolean status, Integer code, T data) {
        this.status = status;
        this.code = code;
        this.data = data;
    }

    public Response(Boolean status, Integer code, List<T> dataList) {
        this.status = status;
        this.code = code;
        this.dataList = dataList;
    }

	public Response(Boolean status, Integer code, String message, T data) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.data = data;
    }

	public Response(Integer code, Boolean status, String message,
                    List<T> data) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.dataList = data;
    }

}
