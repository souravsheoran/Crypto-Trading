package com.sourav.repository;

import com.sourav.modal.ForgetPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForgetPasswordRepository extends JpaRepository<ForgetPasswordToken, String> {

    ForgetPasswordToken findByUserId(long userid);
}
