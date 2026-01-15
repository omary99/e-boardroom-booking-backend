package com.boardroom.boardroom_booking.utils;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestWrapper<T> {

    @Valid
    private T data;

}
