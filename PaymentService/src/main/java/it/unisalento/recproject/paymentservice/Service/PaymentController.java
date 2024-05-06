package it.unisalento.recproject.paymentservice.Service;

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
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final String stripeSecretKey = "your_stripe_secret_key";
    private final String stripeAccountId = "your_stripe_account_id";

    @Autowired
    PaymentRepository paymentRepository;
    

    @PostMapping("/create")
    public PaymentResponse createPaymentLink(@RequestParam("iban") String iban) {
        try {
            // Set API key
            Stripe.apiKey = stripeSecretKey;

            // Create bank account token
            Map<String, Object> bankAccountParams = new HashMap<>();
            bankAccountParams.put("type", "iban");
            bankAccountParams.put("iban", iban);

            // Create bank account token
            Token bankAccountToken = Token.create(bankAccountParams);

            // Retrieve external accounts of the Stripe account
            ExternalAccountCollection externalAccounts = Account.retrieve(stripeAccountId).getExternalAccounts();

            // Add bank account as payment method
            ExternalAccount bankAccount = externalAccounts.create(
                    ExternalAccountCollectionCreateParams.builder()
                            .setExternalAccount(bankAccountToken.getId())
                            .build()
            );

            // Check if the bank account was added successfully
            if (bankAccount != null) {
                // Continue with creating the payment as before
                SessionCreateParams params = SessionCreateParams.builder()
                        .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        // Other payment details...
                        .build();

                // Create session with Stripe
                Session session = Session.create(params);

                // Build response with payment link
                PaymentResponse paymentResponse = new PaymentResponse();
                paymentResponse.setPaymentLink(session.getCurrency());

                return paymentResponse;
            } else {
                // Handle case where bank account could not be added
                throw new RuntimeException("Unable to add bank account. Check the provided details.");
            }
        } catch (StripeException e) {
            // Handle Stripe exceptions
            throw new RuntimeException("Error processing payment. Please try again later.", e);
        }
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
}
