package com.sourav.service;

import com.sourav.domain.PaymentMethod;
import com.sourav.domain.PaymentOrderStatus;
import com.sourav.modal.PaymentOrder;
import com.sourav.modal.User;
import com.sourav.repository.PaymentOrderRepository;
import com.sourav.response.PaymentResponse;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentOrderRepository paymentOrderRepository;

    @Value("${stripe.api.key}")
    private String stripeSecretKey;

    @Value("${razorpay.api.key}")
    private String apiKey;

    @Value("${razorpay.api.secret}")
    private String apiSecretKey;

    @Override
    public PaymentOrder createOrder(User user, Long amount, PaymentMethod paymentMethod) {
        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setUser(user);
        paymentOrder.setAmount(amount);
        paymentOrder.setPaymentMethod(paymentMethod);
        paymentOrder.setStatus(PaymentOrderStatus.PENDING);

        return paymentOrderRepository.save(paymentOrder);
    }

    @Override
    public PaymentOrder getPaymentOrderById(Long id) throws Exception {
        return paymentOrderRepository.findById(id).orElseThrow(()-> new Exception("Payment order not found"));
    }

    @Override
    public Boolean proccedPaymentOrder(PaymentOrder paymentOrder, String paymentId) throws RazorpayException {

        if(paymentOrder.getStatus() == null){
            paymentOrder.setStatus(PaymentOrderStatus.PENDING);
        }

        if(paymentOrder.getStatus().equals(PaymentOrderStatus.PENDING)){
            if(paymentOrder.getPaymentMethod().equals(PaymentMethod.RAZORPAY)){
                RazorpayClient razorpay= new RazorpayClient(apiKey,apiSecretKey);
                Payment payment = razorpay.payments.fetch(paymentId);

                Integer amount = payment.get("amount");
                String status  = payment.get("status");

                if(status.equals("captured")){
                    paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
                    return  true;
                }
                paymentOrder.setStatus(PaymentOrderStatus.FAILED);
                paymentOrderRepository.save(paymentOrder);
                return false;
            }
            paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
            paymentOrderRepository.save(paymentOrder);
            return true;
        }
        return false;
    }

//    @Override
//    public PaymentResponse createRazorpayPaymentLink(User user, Long amount,Long orderId) throws RazorpayException {
//
//        Long Amount=amount*100;
//
//        try {
//            RazorpayClient  razorpay = new RazorpayClient(apiKey, apiSecretKey);
//
//            //create a json object with payment link request parameters
//            JSONObject paymentLinkRequest = new JSONObject();
//            paymentLinkRequest.put("amount",amount);
//            paymentLinkRequest.put("currency","INR");
//
//            //create a json object with the customer details
//            JSONObject customer = new JSONObject();
//            customer.put("name",user.getFullName());
//
//            customer.put("email",user.getEmail());
//            paymentLinkRequest.put("customer",customer);
//
//            //create a json object with the notification settings
//            JSONObject notify = new JSONObject();
//            notify.put("email",true);
//            paymentLinkRequest.put("notify",notify);
//
//            //Set the remainder settings
//            paymentLinkRequest.put("remainder_enable",true);
//
//            //Set the callback url and method
//            paymentLinkRequest.put("callback_url" , "http://localhost:8080/wallet?order_id="+ orderId);
//            paymentLinkRequest.put("callback_method","get");
//
//            //Create the payment link using the paymentLink.create() method
//            PaymentLink payment = razorpay.paymentLink.create(paymentLinkRequest);
//
//            String paymentLinkId = payment.get("id");
//            String paymentLinkUrl = payment.get("short_url");
//
//            PaymentResponse res = new PaymentResponse();
//            res.setPayment_url(paymentLinkUrl);
//
//            return res;
//
//        } catch (RazorpayException e) {
//            System.out.println("Error creating payment link: " + e.getMessage());
//            throw new RazorpayException(e.getMessage());
//        }
//    }

    @Override
    public PaymentResponse createRazorpayPaymentLink(User user, Long amount, Long orderId) throws RazorpayException {

        // Convert amount to paise
        Long AmountInPaise = amount * 100;

        try {
            RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecretKey);

            // Create a JSON object with payment link request parameters
            JSONObject paymentLinkRequest = new JSONObject();
            paymentLinkRequest.put("amount", AmountInPaise);
            paymentLinkRequest.put("currency", "INR");
            paymentLinkRequest.put("description", "Payment for order #" + orderId);

            // Create a JSON object with the customer details
            JSONObject customer = new JSONObject();
            customer.put("name", user.getFullName());
            customer.put("email", user.getEmail());
            paymentLinkRequest.put("customer", customer);

            // Create a JSON object with the notification settings
            JSONObject notify = new JSONObject();
            notify.put("email", true);
            paymentLinkRequest.put("notify", notify);

            // Set the callback URL and method
            paymentLinkRequest.put("callback_url", "http://localhost:8080/wallet?order_id=" + orderId);
            paymentLinkRequest.put("callback_method", "get");

            // Create the payment link using the paymentLink.create() method
            PaymentLink paymentLink = razorpay.paymentLink.create(paymentLinkRequest);

            String paymentLinkId = paymentLink.get("id");
            String paymentLinkUrl = paymentLink.get("short_url");

            PaymentResponse res = new PaymentResponse();
            res.setPayment_url(paymentLinkUrl);

            return res;

        } catch (RazorpayException e) {
            System.out.println("Error creating payment link: " + e.getMessage());
            throw new RazorpayException(e.getMessage());
        }
    }


    @Override
    public PaymentResponse createStripPaymentLink(User user, Long amount, Long orderId) throws StripeException {
        Stripe.apiKey = stripeSecretKey;

        SessionCreateParams params = SessionCreateParams.builder().
                addAllPaymentMethodType(Collections.singletonList(SessionCreateParams.
                        PaymentMethodType.CARD)).setMode(SessionCreateParams.
                        Mode.PAYMENT).setSuccessUrl("http://localhost:8080/wallet?order_id="+orderId).
                setCancelUrl("http://localhost:8080/payment/cancel").addLineItem(SessionCreateParams.LineItem.builder().
                        setQuantity(1L).setPriceData(SessionCreateParams.LineItem.PriceData.builder().setCurrency("usd").
                                setUnitAmount(amount*100).setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder().
                                        setName("Top up wallet").build()).build()).build()).build();

        Session session = Session.create(params);

        System.out.println("session _____ "+session);

        PaymentResponse res = new PaymentResponse();
        res.setPayment_url(session.getUrl());
        return res;
    }
}
