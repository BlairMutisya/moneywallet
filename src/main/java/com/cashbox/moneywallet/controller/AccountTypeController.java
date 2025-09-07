package com.cashbox.moneywallet.controller;

import com.cashbox.moneywallet.entity.AccountType;
import com.cashbox.moneywallet.service.AccountTypeService;
import com.cashbox.moneywallet.common.BaseApiResponse;
import com.cashbox.moneywallet.util.ResponseUtils;
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
