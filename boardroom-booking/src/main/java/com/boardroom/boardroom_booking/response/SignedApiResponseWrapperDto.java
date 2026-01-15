package com.boardroom.boardroom_booking.response;

import lombok.Data;

@Data
public class SignedApiResponseWrapperDto {
    private Object data;
    private String signature;
}
