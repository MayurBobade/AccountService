package com.mayurbobde.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of = {"loanId", "loanAmount"})
public class Loan {

    private String loanId;

    private double loanAmount;

    private double balanceAmount;

    private int balanceTenure;

    private int totalTenure;

    private String status;

    private Customer customer;
}