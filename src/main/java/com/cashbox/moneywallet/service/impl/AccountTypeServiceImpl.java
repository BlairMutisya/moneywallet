package com.cashbox.moneywallet.service.impl;

import com.cashbox.moneywallet.entity.AccountType;
import com.cashbox.moneywallet.repository.AccountTypeRepository;
import com.cashbox.moneywallet.service.AccountTypeService;
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
