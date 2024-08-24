package com.sourav.service;

import com.sourav.domain.PaymentMethod;
import com.sourav.modal.PaymentOrder;
import com.sourav.modal.User;
import com.sourav.response.PaymentResponse;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;

public interface PaymentService {

    PaymentOrder createOrder(User user, Long amount, PaymentMethod paymentMethod);

    PaymentOrder getPaymentOrderById(Long id) throws Exception;

    Boolean proccedPaymentOrder(PaymentOrder paymentOrder, String paymentId) throws RazorpayException;

    PaymentResponse createRazorpayPaymentLink(User user, Long amount,Long orderId) throws RazorpayException;

    PaymentResponse createStripPaymentLink(User user, Long amount, Long orderId) throws StripeException;
}
