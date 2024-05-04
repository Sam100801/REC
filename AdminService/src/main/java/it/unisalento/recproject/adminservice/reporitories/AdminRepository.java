package it.unisalento.recproject.adminservice.reporitories;

import it.unisalento.recproject.adminservice.domain.Admin;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AdminRepository extends MongoRepository<Admin, String> {
    Optional<Admin> findByEmail(String email);
}
