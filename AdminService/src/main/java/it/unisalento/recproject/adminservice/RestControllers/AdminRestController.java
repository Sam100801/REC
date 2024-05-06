package it.unisalento.recproject.adminservice.RestControllers;

import it.unisalento.recproject.adminservice.domain.Admin;
import it.unisalento.recproject.adminservice.dto.AdminDTO;
import it.unisalento.recproject.adminservice.dto.AdminListDTO;
import it.unisalento.recproject.adminservice.exceptions.AdminException;
import it.unisalento.recproject.adminservice.reporitories.AdminRepository;
import it.unisalento.recproject.adminservice.security.AuthorizationAPI;
import it.unisalento.recproject.adminservice.security.JwtUtilities;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/amministratore")
public class AdminRestController {

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    JwtUtilities jwtUtilities;

    @RequestMapping(value = "/registrazione", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registrazione(@RequestBody AdminDTO amministratoreDTO){

            Admin amministratore = new Admin();

            amministratore.setNome(amministratoreDTO.getNome());
            amministratore.setCognome(amministratoreDTO.getCognome());
            amministratore.setEmail(amministratoreDTO.getEmail());
            amministratore.setIban(amministratoreDTO.getIban());
            amministratore.setIncasso(0);
            amministratore.setSaldo(0);

            amministratore = adminRepository.save(amministratore);

            amministratoreDTO.setId(amministratore.getId());
            return ResponseEntity.ok(amministratoreDTO);
    }
    @RequestMapping(value = "/incasso/{email}", method = RequestMethod.GET)
    public float getIncasso(@PathVariable String email, HttpServletRequest request) throws AdminException {

        AuthorizationAPI authorizationAPI = new AuthorizationAPI();
        authorizationAPI.checkAuthorizationRole(request, jwtUtilities);

        Optional<Admin> amministratore = adminRepository.findByEmail(email);

        if(amministratore.isEmpty()){
            throw new AdminException();
        }

        return amministratore.get().getIncasso();
    }

    @RequestMapping(value = "/saldo/{email}", method = RequestMethod.GET)
    public float getSaldo(@PathVariable String email, HttpServletRequest request) throws AdminException {

        AuthorizationAPI authorizationAPI = new AuthorizationAPI();
        authorizationAPI.checkAuthorizationRole(request, jwtUtilities);

        Optional<Admin> amministratore = adminRepository.findByEmail(email);

        if(amministratore.isEmpty()){
            throw new AdminException();
        }

        return amministratore.get().getSaldo();
    }

    @RequestMapping(value = "/performanceREC", method = RequestMethod.GET)
    public float getPerformance(HttpServletRequest request) {

        AuthorizationAPI authorizationAPI = new AuthorizationAPI();
        authorizationAPI.checkAuthorizationRole(request, jwtUtilities);

        float energiaTotaleREC;
        float energiaVendutaREC;

        // Vado a fare una chiamata all'API del microservizio abitazione per ricavare l'energia totale disponibile all'intenro della REC
        String url = "http://host.docker.internal:8085/api/abitazione/energia_disponibile_totale";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Float> response = restTemplate.exchange(url, HttpMethod.GET, entity, Float.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            energiaTotaleREC = response.getBody();
        } else {
            throw new RuntimeException("Errore durante la chiamata all'API");
        }

        // Vado a fare una chiamata all'API del microservizio abitazione per ricavare l'energia totale venduta all'interno della REC
        url = "http://host.docker.internal:8085/api/abitazione/energia_venduta_totale";
        entity = new HttpEntity<>(headers);
        response = restTemplate.exchange(url, HttpMethod.GET, entity, Float.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            energiaVendutaREC = response.getBody();
        } else {
            throw new RuntimeException("Errore durante la chiamata all'API");
        }

        return (energiaVendutaREC / (energiaTotaleREC + energiaVendutaREC)) * 100;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public AdminListDTO getAllAdmin(){
        ArrayList<AdminDTO> list = new ArrayList<>();
        AdminListDTO amministratoreListDTO = new AdminListDTO();
        amministratoreListDTO.setList(list);

        List<Admin> amministratori = adminRepository.findAll();

        for (Admin amministratore : amministratori){
            AdminDTO amministratoreDTO = new AdminDTO();

            amministratoreDTO.setNome(amministratore.getNome());
            amministratoreDTO.setCognome(amministratore.getCognome());
            amministratoreDTO.setId(amministratore.getId());
            amministratoreDTO.setEmail(amministratore.getEmail());
            amministratoreDTO.setIban(amministratore.getIban());

            list.add(amministratoreDTO);
        }
        return amministratoreListDTO;
    }
}
