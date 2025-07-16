package com.eazyBytes.accounts.controller;

import com.eazyBytes.accounts.constants.AccountsConstants;
import com.eazyBytes.accounts.dto.AccountsSupportDto;
import com.eazyBytes.accounts.dto.CustomerDto;
import com.eazyBytes.accounts.dto.ResponseDto;
import com.eazyBytes.accounts.service.IAccountsService;
import io.micrometer.common.lang.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
// When applied to a class, @Validated enables the validation of method parameters within that class,
// provided those parameters are also annotated with validation constraints.
@Validated
public class AccountsController {

    @Autowired
    private final IAccountsService accountsService;

    @Value("${build.version}")
    private String buildVersion;

    @Autowired
    private Environment environment;

    @Autowired
    private AccountsSupportDto accountsServiceDto;

    @Autowired
    public AccountsController(IAccountsService accountsService) {
        this.accountsService = accountsService;
    }

    @PostMapping(path = "/create-account")
    public ResponseEntity<ResponseDto> createAccount(@Valid @RequestBody CustomerDto customersDto) {
        //@Valid -> Triggers validation before entering method body
        accountsService.createAccount(customersDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(AccountsConstants.STATUS_201, AccountsConstants.MESSAGE_201));
    }

    @GetMapping(path = "/fetch")
    public ResponseEntity<CustomerDto> getAccountDetails(@RequestParam @Pattern(regexp = "(^|[0-9]{10})", message = "Mobile number should be 10 digits")
                                                             String mobileNumber, @RequestParam @Nullable String accountNumber) {

        CustomerDto customerDto = accountsService.getAccounts(mobileNumber);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(customerDto);
    }

    @PutMapping(path = "/update-account")
    public ResponseEntity<ResponseDto> updateAccountDetails(@Valid @RequestBody CustomerDto customersDto) {
        if (customersDto == null || customersDto.getMobileNumber() == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDto(AccountsConstants.STATUS_400, AccountsConstants.MESSAGE_400));
        }

        boolean result = accountsService.updateAccountDetails(customersDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteAccountDetails(@RequestParam @Pattern(regexp = "(^|[0-9]{10})", message = "Mobile number should be 10 digits")
                                                                String mobileNumber, @RequestParam @Nullable String accountNumber) {

        boolean isDeleted = accountsService.deleteAccount(mobileNumber);
        if (isDeleted) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(AccountsConstants.STATUS_417, AccountsConstants.MESSAGE_417_DELETE));
        }
    }

    @GetMapping("/build-info")
    public ResponseEntity<String> getBuildInfo() {
        return ResponseEntity.status(HttpStatus.OK).body(buildVersion);
    }

    @GetMapping("/get-env")
    public ResponseEntity<String> getEnvVariables() {
        return ResponseEntity.status(HttpStatus.OK).body(environment.getProperty("OS"));
    }

    @GetMapping("/get-support-info")
    public ResponseEntity<AccountsSupportDto> getSupportDetails() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accountsServiceDto);
    }
}
