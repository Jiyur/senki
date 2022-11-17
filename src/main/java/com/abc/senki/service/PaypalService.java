package com.abc.senki.service;

import com.abc.senki.model.entity.CartEntity;
import com.abc.senki.model.entity.CartItemEntity;
import com.abc.senki.model.entity.OrderDetailEntity;
import com.abc.senki.model.entity.OrderEntity;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaypalService {
    public static final String SUCCESS_URL = "/api/order/pay/success/";
    public static final String CANCEL_URL = "/api/order/pay/cancel/";
    @Autowired
    private APIContext apiContext;

    public Payment createPayment(
            OrderEntity order,
            String currency,
            String method,
            String intent,
            String cancelUrl,
            String successUrl) throws PayPalRESTException {
        //Set amount
        Amount amount=new Amount();
        amount.setCurrency(currency);
        amount.setTotal(String.format("%.2f", order.getTotal()));
        //Init transaction
        Transaction transaction=new Transaction();
        transaction.setAmount(amount);
        List<Transaction>transactions=new ArrayList<>();
        transactions.add(transaction);

        //Init payment
        Payer payer=new Payer();
        payer.setPaymentMethod(method);
        //Handle payerInfo
//        PayerInfo payerInfo=payer.getPayerInfo();
        //Handle payment
        Payment payment = new Payment();
        payment.setIntent(intent);
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        //Set redirect urls
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        payment.setRedirectUrls(redirectUrls);

        return payment.create(apiContext);


    }
    //Execute payment
    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException{
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(payerId);
        return payment.execute(apiContext, paymentExecute);
    }

    public String paypalPayment(OrderEntity order, HttpServletRequest request){

        try {
            Payment payment = createPayment(order, "USD", "paypal", "sale",
                    request.getHeader("origin")+CANCEL_URL+order.getId().toString(),
                    request.getHeader("origin")+SUCCESS_URL+order.getId().toString());
            for(Links link:payment.getLinks()){
                if(link.getRel().equals("approval_url")){
                    return link.getHref();
                }
            }
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        return null;
    }


}
