package com.cashbox.AuthService.controller;

import com.cashbox.AuthService.entity.AccountType;
import com.cashbox.AuthService.service.AccountTypeService;
import com.cashbox.AuthService.common.BaseApiResponse;
import com.cashbox.AuthService.util.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/account-types")
@RequiredArgsConstructor
public class AccountTypeController {

    private final AccountTypeService accountTypeService;

    @GetMapping
    public BaseApiResponse<List<AccountType>> getAllAccountTypes() {
        List<AccountType> types = accountTypeService.getAllAccountTypes();
        return ResponseUtils.success(types, "Account types fetched successfully");
    }
}
