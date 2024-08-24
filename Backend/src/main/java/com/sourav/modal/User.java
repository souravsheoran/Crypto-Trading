package com.sourav.modal;

import com.sourav.domain.USER_ROLE;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)  //It is used to auto generate the Id
    private Long id;

    private String fullName;
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)  //The annotation can only send the data not retrieve the data
    private String password;

    @Embedded
    private TwoFactorAuth twoFactorAuth = new TwoFactorAuth();

    //This is Enum type from domain package
    private USER_ROLE role =  USER_ROLE.ROLE_CUSTOMER;
}
