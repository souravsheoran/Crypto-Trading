package com.sourav.modal;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data

public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne  //Relation between user and wallet
    private User user;

    private BigDecimal balance=BigDecimal.ZERO;
}
