package com.mayurbobde.controller;

import com.mayurbobde.model.Account;
import com.mayurbobde.model.Loan;
import com.mayurbobde.model.Transaction;
import com.mayurbobde.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountsController {
	
	
    @Autowired
    private AccountService accountService;

    @GetMapping("/{accountId}/statement")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Missing description",
                            content = @Content(mediaType = "text/plain")),
                    @ApiResponse(
                            responseCode = "200",
                            description = "JVM system properties of a particular host.",
                            content = @Content(mediaType = "application/json"
                                    )) })
    @Operation(
            summary = "Get JVM system properties for particular host",
            description = "Retrieves and returns the JVM system properties from the system "
                    + "service running on the particular host.")

    public Transaction checkAccountBalance(@PathVariable("accountId") @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true , description = "account id ") long accountId){
        return accountService.checkBalance(accountId);
    }

    @PutMapping("/transaction/{accountId}")
    @ApiResponse(description = "Returns the Transaction object", responseCode = "200")
    public Transaction withdraw(@PathVariable("accountId") long accountId, @Valid @RequestBody Transaction transaction){
        if (transaction.getType().equalsIgnoreCase("WITHDRAW")){
            return accountService.withdraw(accountId, transaction );
        }
        return accountService.deposit(accountId, transaction );
    }

    @GetMapping("/list")
    public Map<String, Object> fetchAccounts(
                            @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
                            @RequestParam(value = "size", defaultValue = "10") int pageSize,
                            @RequestParam(value = "sortBy", defaultValue = "accountId") String sortBy){

        final Page<Account> pageResponse = this.accountService.fetchAllAccounts(pageNo, pageSize, sortBy);
        long totalNumberOfRecords = pageResponse.getTotalElements();
        final int totalPages = pageResponse.getTotalPages();
        Map<String, Object> response = new HashMap<>();
        response.put("records", totalNumberOfRecords);
        response.put("pages", totalPages);
        response.put("data", pageResponse.getContent());
        return response;
    }

    @GetMapping("/balance")
    public Map<String, Object>  fetchAccountsByBalance(
            @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
            @RequestParam(value = "size", defaultValue = "10") int pageSize,
            @RequestParam(value = "gt" , defaultValue = "50000") double gt,
            @RequestParam(value = "lt" , defaultValue = "200000") double lt){
        final Page<Account> pageResponse = this.accountService.fetchAccountsBetween(pageNo, pageSize, gt, lt);
        long totalNumberOfRecords = pageResponse.getTotalElements();
        final int totalPages = pageResponse.getTotalPages();
        Map<String, Object> response = new HashMap<>();
        response.put("records", totalNumberOfRecords);
        response.put("pages", totalPages);
        response.put("data", pageResponse.getContent());
        return response;
    }

    @PostMapping("/{customerId}/loans")
    public Loan applyForLoan(@PathVariable("customerId") long customerId, @RequestBody Loan loan,HttpServletRequest request){
    	
    	String token = request.getHeader("Authorization");
    	//System.out.println(token);
    	
        return this.accountService.applyForLoan(customerId, loan,token);
    }


}