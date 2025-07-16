package com.eazyBytes.accounts.service.impl;

import com.eazyBytes.accounts.constants.AccountsConstants;
import com.eazyBytes.accounts.dto.AccountsDto;
import com.eazyBytes.accounts.dto.CustomerDto;
import com.eazyBytes.accounts.entity.Accounts;
import com.eazyBytes.accounts.entity.Customer;
import com.eazyBytes.accounts.exceptions.CustomerAlreadyExistsException;
import com.eazyBytes.accounts.exceptions.ResourceNotFoundException;
import com.eazyBytes.accounts.mapper.AccountsMapper;
import com.eazyBytes.accounts.mapper.CustomerMapper;
import com.eazyBytes.accounts.repository.AccountsRepository;
import com.eazyBytes.accounts.repository.CustomerRepository;
import com.eazyBytes.accounts.service.IAccountsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountsServiceImpl implements IAccountsService {

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;

    /**
     * @param customersDto - CustomerDto Object
     */
    @Override
    public void createAccount(CustomerDto customersDto) {
        Customer customer = CustomerMapper.mapToCustomer(customersDto, new Customer());
        // check if customer already exists
        Optional<Customer> customerOptional = customerRepository.findByMobileNumber(customersDto.getMobileNumber());
        if(customerOptional.isPresent()) {
            throw new CustomerAlreadyExistsException("Customer already exists with mobile number " + customersDto.getMobileNumber());
        }
        customerRepository.save(customer);

        Accounts newAccount = createNewAccount(customer);
        accountsRepository.save(newAccount);
    }

    /**
     * @param mobileNumber - mobile number of customer
     */
    @Override
    public CustomerDto getAccounts(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));

        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId());

        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
        customerDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));
        return customerDto;
    }

    /**
     * @param customerDto - CustomerDto Object
     */
    @Override
    public boolean updateAccountDetails(CustomerDto customerDto) {
        boolean isUpdated = false;
        AccountsDto accountsDto = customerDto.getAccountsDto();
        if(accountsDto !=null ){
            Accounts accounts = accountsRepository.findById(accountsDto.getAccountNumber()).orElseThrow(
                    () -> new ResourceNotFoundException("Account", "AccountNumber", accountsDto.getAccountNumber().toString())
            );
            AccountsMapper.mapToAccounts(accountsDto, accounts);
            accounts = accountsRepository.save(accounts);

            Long customerId = accounts.getCustomerId();
            Customer customer = customerRepository.findById(customerId).orElseThrow(
                    () -> new ResourceNotFoundException("Customer", "CustomerID", customerId.toString())
            );
            CustomerMapper.mapToCustomer(customerDto,customer);
            customerRepository.save(customer);
            isUpdated = true;
        }
        return  isUpdated;
    }

    /**
     * @param mobileNumber - Input Mobile Number
     * @return boolean indicating if the delete of Account details is successful or not
     */
    @Override
    public boolean deleteAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        accountsRepository.deleteByCustomerId(customer.getCustomerId());
        customerRepository.deleteById(customer.getCustomerId());
        return true;
    }

    /**
     * @param customer - Customer Object
     * @return the new account details
     */
    private Accounts createNewAccount(Customer customer) {
        Accounts newAccount = new Accounts();
        newAccount.setCustomerId(customer.getCustomerId());
        Long randomAccNumber = 1000000000L + new Random().nextInt(900000000);

        newAccount.setAccountNumber(randomAccNumber);
        newAccount.setAccountType(AccountsConstants.SAVINGS);
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);
        newAccount.setCreatedAt(LocalDateTime.now());
        newAccount.setCreatedBy("Admin");
        return newAccount;
    }

}
