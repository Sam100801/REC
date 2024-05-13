package it.unisalento.recproject.orderservice.RestController;

import it.unisalento.recproject.orderservice.domain.OrderAdmin;
import it.unisalento.recproject.orderservice.domain.OrderUser;
import it.unisalento.recproject.orderservice.dto.OrderDTO;
import it.unisalento.recproject.orderservice.dto.RewardDTO;
import it.unisalento.recproject.orderservice.repositories.OrderAdminRepository;
import it.unisalento.recproject.orderservice.repositories.OrderUserRepository;
import it.unisalento.recproject.orderservice.security.AuthorizationAPI;
import it.unisalento.recproject.orderservice.security.JwtUtilities;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderRestController {

    @Autowired
    private OrderUserRepository orderUserRepository;

    @Autowired
    private JwtUtilities jwtUtilities;

    @Autowired
    private OrderAdminRepository orderAdminRepository;


    @RequestMapping(value = "/admin/{email}", method = RequestMethod.POST)
    public ResponseEntity<?> OrderAdmin(@PathVariable String email, @RequestBody List<RewardDTO> rewards, HttpServletRequest request) {
        try {

            if (rewards == null || rewards.isEmpty()) {
                return ResponseEntity.badRequest().body("L'ordine deve includere almeno un reward.");
            }

            double totalPrice = 0.0F;
            for (RewardDTO reward : rewards) {
                totalPrice += reward.getCosto();
            }

            OrderAdmin order = new OrderAdmin();
            order.setUserEmail(email);
            order.setCosto(totalPrice);
            order.setLocalDateTime(LocalDateTime.now());
            order.setRewards(rewards);

            order = orderAdminRepository.save(order);

            return ResponseEntity.ok().body(order);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante la creazione dell'ordine: " + e.getMessage());
        }
    }

    @RequestMapping(value = "/getAdmin/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getOrderAdminById(@PathVariable String id) {
        try {
            OrderAdmin order = orderAdminRepository.findById(id).orElse(null);
            if (order == null) {
                return ResponseEntity.badRequest().body("Ordine non trovato.");
            }
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante il recupero dell'ordine: " + e.getMessage());
        }
    }

    @RequestMapping(value = "/getUser/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getOrderUserById(@PathVariable String id){
        try {
            OrderUser order = orderUserRepository.findById(id).orElse(null);
            if (order == null){
                return ResponseEntity.badRequest().body("Ordine non trovato.");
            }
            return ResponseEntity.ok(order);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante il recupero dell'ordine: " + e.getMessage());
        }
    }

    @RequestMapping(value = "/user/{email}", method = RequestMethod.POST)
    public ResponseEntity<?> OrderUser(@PathVariable String email, @RequestBody List<RewardDTO> rewards, HttpServletRequest request) {
        try {

            if (rewards == null || rewards.isEmpty()) {
                return ResponseEntity.badRequest().body("L'ordine deve includere almeno un reward.");
            }

            double totalCrediti = 0;
            for (RewardDTO reward : rewards) {
                totalCrediti += reward.getCosto();
            }

            OrderUser newOrder = new OrderUser();

            newOrder.setUserEmail(email);
            newOrder.setCostoCrediti(totalCrediti);
            newOrder.setLocalDateTime(LocalDateTime.now());
            newOrder.setRewards(rewards);

            String url = "http://host.docker.internal:8088/api/users/togliCrediti/"+email+"?crediti=" + (float) totalCrediti;
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Void> response1 = restTemplate.exchange(url, HttpMethod.PUT, null, Void.class);

            newOrder = orderUserRepository.save(newOrder);

            return ResponseEntity.ok().body("Ordine creato con successo. ID ordine: " + newOrder.getId() + ", Importo totale: " + totalCrediti);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante la creazione dell'ordine: " + e.getMessage());
        }
    }

    @RequestMapping(value = "/admin/{email}", method = RequestMethod.GET)
    public ResponseEntity<?> getAllAdminByEmail(@PathVariable String email, HttpServletRequest request) {
        try {

            List<OrderAdmin> orderAdmin = orderAdminRepository.findAllById(email);

            if (orderAdmin == null) {
                return ResponseEntity.badRequest().body("Ordine non trovato.");
            }
            return ResponseEntity.ok(orderAdmin);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante il recupero dell'ordine: " + e.getMessage());
        }
    }

    @RequestMapping(value = "/user/{email}", method = RequestMethod.GET)
    public ResponseEntity<?> getAllUserByEmail(@PathVariable String email, HttpServletRequest request) {
        try {

            AuthorizationAPI authorizationAPI = new AuthorizationAPI();
            authorizationAPI.checkAuthorizationRole(request, jwtUtilities);

            List<OrderUser> orderUser = orderUserRepository.findAllById(email);

            if (orderUser == null) {
                return ResponseEntity.badRequest().body("Ordine non trovato.");
            }
            return ResponseEntity.ok(orderUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante il recupero dell'ordine: " + e.getMessage());
        }
    }

}

