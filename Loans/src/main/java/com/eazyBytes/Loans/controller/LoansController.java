package com.eazyBytes.Loans.controller;

import com.eazyBytes.Loans.constants.LoansConstants;
import com.eazyBytes.Loans.dto.LoansDto;
import com.eazyBytes.Loans.dto.LoansSupportInfoDto;
import com.eazyBytes.Loans.dto.ResponseDto;
import com.eazyBytes.Loans.service.ILoansService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/loan")
@Validated
public class LoansController {

    private final ILoansService service;

    @Value("${build.version}")
    private String buildVersion;

    @Autowired
    private Environment environment;

    @Autowired
    private LoansSupportInfoDto loansSupportInfoDto;

    public LoansController(@Qualifier("loansService") ILoansService service) {
        this.service = service;
    }

    @PostMapping(value = "apply-loan")
    public ResponseEntity<ResponseDto> CreateLoan(@RequestParam @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
                                                      String mobileNumber) {

        service.createLoan(mobileNumber);
        return ResponseEntity
                .status(HttpStatus.CREATED)// actual request status
                .body(new ResponseDto(LoansConstants.STATUS_201, LoansConstants.MESSAGE_201)); // custom status message
    }

    @GetMapping(value = "loan-details")
    public ResponseEntity<LoansDto> getLoanDetails(
            @RequestParam
            @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
            String mobileNumber) {

        LoansDto dto = service.fetchLoan(mobileNumber);

        return  ResponseEntity.status(HttpStatus.OK).body(dto);

    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateLoanDetails(@Valid @RequestBody LoansDto loansDto) {
        boolean isUpdated = service.updateLoan(loansDto);
        if(isUpdated) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(LoansConstants.STATUS_200, LoansConstants.MESSAGE_200));
        }else{
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(LoansConstants.STATUS_417, LoansConstants.MESSAGE_417_UPDATE));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteLoanDetails(@RequestParam
                                                         @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
                                                         String mobileNumber) {
        boolean isDeleted = service.deleteLoan(mobileNumber);
        if (isDeleted) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(LoansConstants.STATUS_200, LoansConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(LoansConstants.STATUS_417, LoansConstants.MESSAGE_417_DELETE));
        }
    }

    @GetMapping("/build-info")
    public ResponseEntity<String> getBuildInfo() {
        return ResponseEntity.status(HttpStatus.OK).body(buildVersion);
    }

    @GetMapping("/get-env-variables")
    public ResponseEntity<String> getSystemInfo() {
        return ResponseEntity.status(HttpStatus.OK).body(environment.getProperty("OS"));
    }

    @GetMapping("/get-support-details")
    public ResponseEntity<LoansSupportInfoDto> getSupportInfo() {
        return ResponseEntity.status(HttpStatus.OK).body(loansSupportInfoDto);
    }

}
