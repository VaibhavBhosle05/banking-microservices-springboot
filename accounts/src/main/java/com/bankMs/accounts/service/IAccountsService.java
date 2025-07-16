package com.eazyBytes.accounts.service;

import com.eazyBytes.accounts.dto.CustomerDto;

public interface IAccountsService {

    /**
     * @param customersDto - CustomerDto Object
     */
    void createAccount(CustomerDto customersDto);

    /**
     * @param mobileNumber - mobile number of customer
     */
    CustomerDto getAccounts(String mobileNumber);

    /**
     *
     * @param customerDto - CustomerDto Object
     * @return boolean indicating if the update of Account details is successful or not
     */
    boolean updateAccountDetails(CustomerDto customerDto);

    /**
     *
     * @param mobileNumber - Input Mobile Number
     * @return boolean indicating if the delete of Account details is successful or not
     */
    boolean deleteAccount(String mobileNumber);
}
