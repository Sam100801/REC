package it.unisalento.recproject.paymentservice.Response;

public class PaymentResponse {

    private String payment_url;

    public String getPaymentLink() {
        return payment_url;
    }

    public void setPaymentLink(String payment_url) {
        this.payment_url = payment_url;
    }

}
