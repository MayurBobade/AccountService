package com.mayurbobde.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode(of = {"accountId"})
@Entity
@ToString(exclude={"customer"})
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    public static final double MIN_BALANCE = 25_000;
    @Id
    @Column(name="customer_id")
    private long accountId;
    private double balance;
    @OneToOne
    @MapsId
    @JoinColumn(name="customer_id")
    private Customer customer;

}