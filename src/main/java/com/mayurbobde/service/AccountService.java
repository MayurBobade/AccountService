package com.mayurbobde.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.mayurbobde.model.Account;
import com.mayurbobde.model.Loan;
import com.mayurbobde.model.Transaction;
import com.mayurbobde.repository.AccountsRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {
	
	
    @Autowired
    private AccountsRepository accountsRepository;
    
    @Autowired
    private RestTemplate restTemplate;




    public Transaction checkBalance(long accountId){
        Account account = this.accountsRepository.findById(accountId)
                                                    .orElseThrow(() -> new IllegalArgumentException("Account with Account id does not exists"));
        Transaction transaction = new Transaction();
        transaction.setAmount(account.getBalance());
        transaction.setAccountId(account.getAccountId());
        transaction.setType("CHECK_BALANCE");
        return transaction;
    }

    public Transaction deposit(long accountId, Transaction transaction){
        Account account = this.accountsRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account with Account id does not exists"));
        double updatedAccountBalance = account.getBalance() + transaction.getAmount();
        account.setBalance(updatedAccountBalance);
        this.accountsRepository.save(account);
        Transaction transactionOut = new Transaction();
        transactionOut.setAmount(account.getBalance());
        transactionOut.setAccountId(account.getAccountId());
        transactionOut.setAccountId(account.getAccountId());
        transactionOut.setType("DEPOSIT");
        transactionOut.setNotes(transaction.getNotes());
        return transactionOut;
    }

    public Transaction withdraw(long accountId, Transaction transaction) {
        Account account = this.accountsRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account with Account id does not exists"));

        double currentAccountBalance = account.getBalance();
        double withdrawalAmount = transaction.getAmount();
        if ((currentAccountBalance - withdrawalAmount) > Account.MIN_BALANCE){
            currentAccountBalance = currentAccountBalance - withdrawalAmount;
            account.setBalance(currentAccountBalance);
            this.accountsRepository.save(account);
            Transaction transactionOut = new Transaction();
            transactionOut.setAmount(withdrawalAmount);
            transactionOut.setAccountId(account.getAccountId());
            transactionOut.setType("WITHDRAW");
            transactionOut.setAccountId(account.getAccountId());
            transactionOut.setNotes(transaction.getNotes());
            return transaction;
        }
        throw new IllegalArgumentException("Insufficient Balance ");
    }

    public Page<Account> fetchAccountsGreaterThan(int pageNo, int pageSize,double balance) {
        final PageRequest pageRequest = PageRequest.of(pageNo, pageSize);
        return this.accountsRepository.findByBalanceGreaterThan(balance, pageRequest);
    }
    public Page<Account> fetchAccountsBetween(int pageNo, int pageSize,double gtBalance, double ltBalance) {
        final PageRequest pageRequest = PageRequest.of(pageNo, pageSize);
        return this.accountsRepository.findByBalanceBetween(gtBalance, ltBalance, pageRequest);
    }

    public Page<Account> fetchAllAccounts(int pageNo, int pageSize, String sortBy) {
        final PageRequest pageRequest = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        final Page<Account> pageResponse = this.accountsRepository.findAll(pageRequest);
        return pageResponse;
    }

    //we need to talk to Loans microservice using the post method and send the reponse back
    //@CircuitBreaker(name = "loanservice", fallbackMethod = "fallback")
    public Loan applyForLoan(long customerId, Loan loan,String token) {
        //1st approach using Discovery client
        //return applyForLoanUsingDiscoveryClient(customerId, loan);
       return applyForLoanUsingClientSideLoanBalancer(customerId, loan,token);
       // return applyForLoanUsingFeignClient(customerId, loan);

        //pushing the message to the kafka topic called orders-topic
        //this.source.output().send(MessageBuilder.withPayload(loan).build());

       // return fallback(null);
    }


    private Loan applyForLoanUsingClientSideLoanBalancer(long customerId, Loan loan, String token) {
    	
    	HttpHeaders headers = new HttpHeaders();
    	headers.set("Authorization", token);
    	headers.setContentType(MediaType.APPLICATION_JSON);

    	HttpEntity<Loan> requestEntity = new HttpEntity<>(loan, headers);

    	ResponseEntity<Loan> loanResponseEntity = restTemplate.postForEntity("http://LOANSERVICE"+"/customer/"+customerId+"/loans", requestEntity, Loan.class);
    	
       //final ResponseEntity<Loan> loanResponseEntity = this.restTemplate.postForEntity("http://localhost:9094"+"/api/customer/"+customerId+"/loans", loan, Loan.class);
        //System.out.println("Loan Id :: " +loanResponseEntity.getBody().getLoanId());
        System.out.println("Status code after making the call :: "+loanResponseEntity.getStatusCode().toString());
        return loanResponseEntity.getBody();
    }
}