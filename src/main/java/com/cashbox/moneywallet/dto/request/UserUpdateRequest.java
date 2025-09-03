package com.cashbox.moneywallet.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String phone;
    private String password; // optional, update only if provided
}
