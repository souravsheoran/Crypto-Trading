package com.sourav.repository;

import com.sourav.modal.TwoFactorOTP;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TwoFactorOtpRepository extends JpaRepository <TwoFactorOTP, String> {
    TwoFactorOTP findByUserId(Long userId) ;
}
