package com.boardroom.boardroom_booking.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseWrapper<T> extends DefaultRespData {
    private T item;
}
