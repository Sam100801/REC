package it.unisalento.recproject.orderservice.repositories;

import it.unisalento.recproject.orderservice.domain.OrderUser;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderUserRepository extends MongoRepository<OrderUser, String> {

    List<OrderUser> findAllById(String email);

}
