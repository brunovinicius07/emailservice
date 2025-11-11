package com.emailservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ForgotPasswordEvent {
    private String email;
    private String nameUser;
    private String token;
}
