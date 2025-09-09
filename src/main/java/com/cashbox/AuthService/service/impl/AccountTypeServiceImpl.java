package com.cashbox.AuthService.service.impl;

import com.cashbox.AuthService.entity.AccountType;
import com.cashbox.AuthService.repository.AccountTypeRepository;
import com.cashbox.AuthService.service.AccountTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountTypeServiceImpl implements AccountTypeService {

    private final AccountTypeRepository accountTypeRepository;

    @Override
    public List<AccountType> getAllAccountTypes() {
        return accountTypeRepository.findAll();
    }
}
