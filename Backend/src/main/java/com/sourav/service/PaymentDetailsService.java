package com.sourav.service;

import com.sourav.modal.PaymentDetails;
import com.sourav.modal.User;

public interface PaymentDetailsService {

    public PaymentDetails addPaymentDetails(String accountNumber, String accountHolderName, String ifsc, String bankName, User user);

    public PaymentDetails getUserPaymentDetails(User user);
}
