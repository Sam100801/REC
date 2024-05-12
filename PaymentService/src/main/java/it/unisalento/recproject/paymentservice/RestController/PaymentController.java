package it.unisalento.recproject.paymentservice.RestController;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.ExternalAccount;
import com.stripe.model.ExternalAccountCollection;
import com.stripe.model.Token;
import com.stripe.model.checkout.Session;
import com.stripe.param.ExternalAccountCollectionCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import it.unisalento.recproject.paymentservice.Response.PaymentResponse;
import it.unisalento.recproject.paymentservice.domain.Payment;
import it.unisalento.recproject.paymentservice.dto.PaymentDTO;
import it.unisalento.recproject.paymentservice.dto.PaymentListDTO;
import it.unisalento.recproject.paymentservice.exceptions.PaymentException;
import it.unisalento.recproject.paymentservice.repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    PaymentRepository paymentRepository;

    @Value("${stripe.api.key1}")
    private String stripeSecretKey1;

    @Value("${stripe.api.key2}")
    private String stripeSecretKey2;


    @RequestMapping(value = "/create-payment-link-user/{costo}", method = RequestMethod.POST)
    public PaymentResponse createPaymentLinkUser(@PathVariable float costo) throws StripeException {

        costo = costo * 100;
        Stripe.apiKey = stripeSecretKey1;

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:8082/api/payments/confirm-payment")
                .setCancelUrl("http://localhost:8082/api/payments/cancel-payment")
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L).setPriceData(
                                SessionCreateParams.LineItem.PriceData.builder()
                                        .setCurrency("eur")
                                        .setUnitAmount((long) (costo * 1L))
                                        .setProductData(
                                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                        .setName("Bolletta REC")
                                                        .build()
                                        )
                                        .build()
                        )
                        .build()
                )
                .build();

        Session session = Session.create(params);

        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setPaymentLink(session.getUrl());
        return paymentResponse;
    }

    @RequestMapping(value = "/create-payment-link-admin/{costo}", method = RequestMethod.POST)
    public PaymentResponse createPaymentLinkAdmin(@PathVariable float costo) throws StripeException {

             costo = costo * 100;
             Stripe.apiKey = stripeSecretKey2;

            SessionCreateParams params = SessionCreateParams.builder()
                    .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl("http://localhost:8082/api/payments/confirm-payment")
                    .setCancelUrl("http://localhost:8082/api/payments/cancel-payment")
                    .addLineItem(SessionCreateParams.LineItem.builder()
                            .setQuantity(1L).setPriceData(
                                    SessionCreateParams.LineItem.PriceData.builder()
                                            .setCurrency("eur")
                                            .setUnitAmount((long) (costo * 1L))
                                            .setProductData(
                                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                            .setName("Acquisto Rewards")
                                                            .build()
                                            )
                                            .build()
                            )
                            .build()
                    )
                    .build();

            Session session = Session.create(params);

            PaymentResponse paymentResponse = new PaymentResponse();
            paymentResponse.setPaymentLink(session.getUrl());
            return paymentResponse;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public PaymentListDTO getAll(){

        ArrayList<PaymentDTO> list = new ArrayList<>();
        PaymentListDTO paymentList = new PaymentListDTO();

        List<Payment> payments = paymentRepository.findAll();

        for (Payment payment : payments) {
            PaymentDTO paymentDTO = new PaymentDTO();
            paymentDTO.setIdPayment(payment.getIdPayment());
            paymentDTO.setEmailReceivePayment(payment.getEmailReceivePayment());
            paymentDTO.setEmailSendPayment(payment.getEmailSendPayment());
            paymentDTO.setDatePayment(payment.getDatePayment());
            list.add(paymentDTO);
        }

        paymentList.setList(list);

        return paymentList;
    }

    @RequestMapping(value = "/{idReceivePayment}", method = RequestMethod.GET)
    public PaymentDTO getByIdReceivePayment(@PathVariable String idReceivePayment) throws PaymentException {
        Payment payment = paymentRepository.findById(idReceivePayment).orElse(null);

        if(payment == null){
            throw new PaymentException();
        }

        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setIdPayment(payment.getIdPayment());
        paymentDTO.setEmailReceivePayment(payment.getEmailReceivePayment());
        paymentDTO.setEmailSendPayment(payment.getEmailSendPayment());
        paymentDTO.setDatePayment(payment.getDatePayment());

        return paymentDTO;
    }

    @RequestMapping(value = "/{idSendPayment}", method = RequestMethod.GET)
    public PaymentDTO getByIdSendPayment(@PathVariable String idSendPayment) throws PaymentException {
        Payment payment = paymentRepository.findById(idSendPayment).orElse(null);

        if(payment == null){
            throw new PaymentException();
        }

        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setIdPayment(payment.getIdPayment());
        paymentDTO.setEmailReceivePayment(payment.getEmailReceivePayment());
        paymentDTO.setEmailSendPayment(payment.getEmailSendPayment());
        paymentDTO.setDatePayment(payment.getDatePayment());

        return paymentDTO;
    }

    @RequestMapping(value = "/confirm-payment", method = RequestMethod.POST)
    public void confirmPayment(@RequestParam("session_id") String sessionId) throws StripeException {
        Stripe.apiKey = stripeSecretKey2;
        Session session = Session.retrieve(sessionId);
        if (session.getPaymentStatus().equals("paid")) {
            Payment payment = new Payment();
            payment.setDatePayment(LocalDateTime.now());
            paymentRepository.save(payment);
        } else {
            System.out.println("Pagamento non effettuato");
        }
    }

    @RequestMapping(value = "cancel-payment", method = RequestMethod.POST)
    public String cancelPayment(){
        return "Pagamento annullato!";
    }

}
