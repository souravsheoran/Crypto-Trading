package com.sourav.modal;

import com.sourav.domain.OrderStatus;
import com.sourav.domain.OrderType;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne //one user have many relations
    @JoinColumn(name = "user_id") // Specify the foreign key column name
    private User user;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING) // Use this if OrderType is an enum
    private OrderType orderType;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private LocalDateTime timestamp=LocalDateTime.now();

    @Column(nullable = false)
    @Enumerated(EnumType.STRING) // Use this if OrderStatus is an enum
    private OrderStatus status;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private OrderItem orderItem;
}
