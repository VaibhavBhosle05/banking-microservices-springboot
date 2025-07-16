package com.eazyBytes.Loans.service.impl;

import com.eazyBytes.Loans.constants.LoansConstants;
import com.eazyBytes.Loans.dto.LoansDto;
import com.eazyBytes.Loans.entity.Loans;
import com.eazyBytes.Loans.exception.LoanAlreadyExistsException;
import com.eazyBytes.Loans.exception.ResourceNotFoundException;
import com.eazyBytes.Loans.mapper.LoansMapper;
import com.eazyBytes.Loans.repository.ILoansRepository;
import com.eazyBytes.Loans.service.ILoansService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class LoansService implements ILoansService {

    private ILoansRepository repository;

    /**
     * @param mobileNumber - Mobile Number of the Customer
     */
    @Override
    public void createLoan(String mobileNumber) {

        Optional<Loans> loan = repository.findByMobileNumber(mobileNumber);

        if(loan.isPresent()) {
            throw new LoanAlreadyExistsException("Loan already availed by the customer.");
        }

        repository.save(createNewLoan(mobileNumber));
    }

    /**
     * @param mobileNumber - Input mobile Number
     * @return Loan Details based on a given mobileNumber
     */
    @Override
    public LoansDto fetchLoan(String mobileNumber) {
        Loans loan = repository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Mobile number not found"));

        return LoansMapper.mapToLoansDto(loan, new LoansDto());
    }

    /**
     * @param loansDto - LoansDto Object
     * @return boolean indicating if the update of card details is successful or not
     */
    @Override
    public boolean updateLoan(LoansDto loansDto) {
        Loans loans = repository.findByMobileNumber(loansDto.getMobileNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Mobile number is not registered"));

        repository.save(LoansMapper.mapToLoans(loansDto, new Loans()));
        return true;
    }

    /**
     * @param mobileNumber - Input Mobile Number
     * @return boolean indicating if the delete of loan details is successful or not
     */
    @Override
    public boolean deleteLoan(String mobileNumber) {
        Loans loans = repository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Mobile number is not registered")
        );
        repository.deleteById(loans.getLoanId());
        return true;
    }

    /**
     * @param mobileNumber - Mobile Number of the Customer
     * @return the new loan details
     */
    private Loans createNewLoan(String mobileNumber) {
        Loans newLoan = new Loans();
        long randomLoanNumber = 100000000000L + new Random().nextInt(900000000);
        newLoan.setLoanNumber(Long.toString(randomLoanNumber));
        newLoan.setMobileNumber(mobileNumber);
        newLoan.setLoanType(LoansConstants.HOME_LOAN);
        newLoan.setTotalLoan(LoansConstants.NEW_LOAN_LIMIT);
        newLoan.setAmountPaid(0);
        newLoan.setOutstandingAmount(LoansConstants.NEW_LOAN_LIMIT);
        return newLoan;
    }

}
