package it.unisalento.recproject.paymentservice.repositories;

import it.unisalento.recproject.paymentservice.domain.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaymentRepository extends MongoRepository<Payment, String>{
}
