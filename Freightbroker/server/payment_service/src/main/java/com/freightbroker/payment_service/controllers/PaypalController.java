package com.freightbroker.payment_service.controllers;

import com.freightbroker.payment_service.services.PayPalPaymentService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Controller
@RequestMapping("/api/freightbroker")
public class PaypalController {

    @Autowired
    PayPalPaymentService service;

    @PostMapping("/pay")
    public ResponseEntity<String> payment(@RequestParam(name="cost") Double cost, @RequestParam(name="email") String email,@RequestParam(name="id_cargo")Long id_cargo) {
        try {
            Payment payment = service.createPayment(cost, "EUR", "Plată prin platforma Freightbroker",email,id_cargo);
            for(Links link:payment.getLinks()) {
                if(link.getRel().equals("approval_url")) {
                    System.out.println(link.getHref());
                    return ResponseEntity.ok(link.getHref());
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @GetMapping("/success")
    public ResponseEntity<Void> successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId, @RequestParam("payeeEmail")String payeeEmail, @RequestParam("total") Double total,@RequestParam("id_cargo") Long id_cargo) {
        try {
            var result = service.executePayment(paymentId, payerId,payeeEmail, total);
            if (result){
                return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("http://localhost:3000/paymentok")).build();
            }
            else
                return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("http://localhost:3000/paymenterror")).build();

        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).location(URI.create("http://localhost:3000/payment-error")).build();
        }
    }

    @GetMapping("/cancel")
    public ResponseEntity<String> cancelPay(@RequestParam(name = "token") String token) {
        return ResponseEntity.ok("Plata anulata");
    }
}