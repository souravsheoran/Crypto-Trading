package com.sourav.service;

import com.sourav.domain.VerificationType;
import com.sourav.modal.ForgetPasswordToken;
import com.sourav.modal.User;

public interface ForgetPasswordService {

    ForgetPasswordToken createToken(User user, String id, String otp, VerificationType verificationType, String sendTo);

    ForgetPasswordToken findById(String id);

    ForgetPasswordToken findByUser(Long userId);

    void deleteToken(ForgetPasswordToken token);
}
