package it.unisalento.recproject.orderservice.repositories;

import it.unisalento.recproject.orderservice.domain.OrderAdmin;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderAdminRepository extends MongoRepository<OrderAdmin, String> {

    List<OrderAdmin> findAllById(String email);
}

