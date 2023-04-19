package com.mayurbobde.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode
public class Transaction {

    @NotNull(message = "Account id cannot be null")
    private long accountId;

    private String type;

    @Max(value = 50_000, message = "The amount should not exceed 50k")
    private double amount;

    @NotEmpty(message = "notes cannot be empty")
    private String notes;
}