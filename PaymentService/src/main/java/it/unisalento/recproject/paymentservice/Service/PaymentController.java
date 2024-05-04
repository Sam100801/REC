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
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class PaymentController {

    private final String stripeSecretKey = "your_stripe_secret_key";
    private final String stripeAccountId = "your_stripe_account_id";
    
    

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
}
