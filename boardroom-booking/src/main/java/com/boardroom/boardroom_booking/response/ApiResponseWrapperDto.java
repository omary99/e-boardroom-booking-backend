package com.boardroom.boardroom_booking.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class ApiResponseWrapperDto implements Serializable {
    private Object data;
    public Boolean status;
    public Integer statusCode;
    public String description;

    public void setData(Object data) {
        this.data = data;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }
}
