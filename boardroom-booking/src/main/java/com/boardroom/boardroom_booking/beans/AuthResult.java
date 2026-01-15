package com.boardroom.boardroom_booking.beans;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthResult<T> {
    private T data;
    private String message;

    private boolean error;
    private Collection<? extends GrantedAuthority> authorities = new ArrayList<>();
}

