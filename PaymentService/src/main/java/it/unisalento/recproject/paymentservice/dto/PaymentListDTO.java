package it.unisalento.recproject.paymentservice.dto;

import java.util.ArrayList;

public class PaymentListDTO {
    ArrayList<PaymentDTO> list;

    public ArrayList<PaymentDTO> getList() {
        return list;
    }

    public void setList(ArrayList<PaymentDTO> list) {
        this.list = list;
    }
}
