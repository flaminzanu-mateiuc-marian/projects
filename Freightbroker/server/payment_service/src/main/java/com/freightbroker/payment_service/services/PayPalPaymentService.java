package com.freightbroker.payment_service.services;

import com.freightbroker.payment_service.model.PaymentDTO;
import com.freightbroker.payment_service.model.PaymentIdDTO;
import com.freightbroker.payment_service.repository.PaymentRepository;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.freightbroker.payment_service.config.CorsConfig.getIp;


@Service
public class PayPalPaymentService {
    @Autowired
    PaymentRepository paymentRepository;

    private static final String INTENT = "SALE";
    private static final String METHOD = "paypal";

    @Autowired
    private APIContext apiContext;

    public Payment createPayment(Double total, String currency, String description, String payeeEmail, Long id_cargo) throws PayPalRESTException {
        Double auxTotal = total;
        Amount amount = new Amount();
        amount.setCurrency(currency);
        total = new BigDecimal(total).setScale(2, RoundingMode.HALF_UP).doubleValue();
        amount.setTotal(String.format("%.2f", total));
        Transaction transaction = new Transaction();

        transaction.setDescription(description);
        transaction.setAmount(amount);
        Payee payee = new Payee();
        payee.setEmail(payeeEmail);
        transaction.setPayee(payee);
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(METHOD);

        Payment payment = new Payment();
        payment.setIntent(INTENT);
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl("http://localhost:8085/api/freightbroker/cancel");
        redirectUrls.setReturnUrl("http://localhost:8085/api/freightbroker/success?payeeEmail=" + payeeEmail + "&total=" + auxTotal+"&id_cargo="+id_cargo);
        payment.setRedirectUrls(redirectUrls);

        return payment.create(apiContext);
    }


    public boolean executePayment(String paymentId, String payerId, String payeeEmail, Double total) throws PayPalRESTException {
        try {
            var result = paymentRepository.findById(new PaymentIdDTO(payerId, payeeEmail, total));
            if (result.isEmpty()) {
                Payment payment = new Payment();
                payment.setId(paymentId);
                PaymentExecution paymentExecute = new PaymentExecution();
                paymentExecute.setPayerId(payerId);
                payment.execute(apiContext, paymentExecute);
                paymentRepository.save(new PaymentDTO(new PaymentIdDTO(payerId, payeeEmail, total)));
                return true;
            } else return false;
        } catch (Exception e) {
            return false;
        }
    }

}
