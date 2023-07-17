package com.abc.senki.service;

import com.abc.senki.model.entity.OrderEntity;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import com.abc.senki.util.CurrencyUtil;

@Service
public class PaypalService {
    public static final String SUCCESS_URL = "/api/order/pay/success/";
    public static final String CANCEL_URL = "/api/order/pay/cancel/";

    public static final String SUCCESS_URL_V2 = "/api/v2/order/pay/success/";
    public static final String CANCEL_URL_V2 = "/api/v2/order/pay/cancel/";

    public static final String SUCCESS_URL_LICENSE="/api/license/pay/success/";

    public static final String CANCEL_URL_LICENSE="/api/license/pay/cancel/";

    public static final String HOST="https://senki.me";
//    public static final String HOST="http://localhost:8080";
    public static final String DIRECT_URL="http://localhost:3000";

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
        amount.setTotal(String.format("%.2f", CurrencyUtil.convertVNDToUsd(order.getTotal())));
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
    public Payment createPayment(
            Double total,
            String currency,
            String method,
            String intent,
            String cancelUrl,
            String successUrl) throws PayPalRESTException {
        //Set amount
        Amount amount=new Amount();
        amount.setCurrency(currency);
        amount.setTotal(String.format("%.2f", CurrencyUtil.convertVNDToUsd(total)));
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
            String host=request.getHeader("origin");
            URI uri=new URI(host);

            String orderId=order.getId().toString();

            Payment payment = createPayment(order, "USD", "paypal", "sale",
                    HOST+CANCEL_URL+orderId,
                    HOST+SUCCESS_URL+orderId
                            );
            for(Links link:payment.getLinks()){
                if(link.getRel().equals("approval_url")){
                    return link.getHref();
                }
            }
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    public String paypalPaymentV2(String payId,HttpServletRequest request,Double total){
        try{

            Payment payment = createPayment(total,"USD", "paypal", "sale",
                    HOST+CANCEL_URL_V2+payId,
                    HOST+SUCCESS_URL_V2+payId);                           ;
            for(Links link:payment.getLinks()){
                if(link.getRel().equals("approval_url")){
                    return link.getHref();
                }
            }
        } catch (PayPalRESTException e) {
        e.printStackTrace();}
        return null;
    }
    public String paypalPaymentSellLicense(String userId,HttpServletRequest request,Double total) throws URISyntaxException {
        try{
            Payment payment=createPayment(total,"USD","paypal","sale",
                    HOST+CANCEL_URL_LICENSE+userId+"&redirectURI="
                            +request.getHeader("origin"),
                    HOST+SUCCESS_URL_LICENSE+userId);
            for(Links link:payment.getLinks()){
                if(link.getRel().equals("approval_url")){
                    return link.getHref();
                }
            }


        } catch ( PayPalRESTException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


}
