package it.unisalento.recproject.adminservice.RestControllers;

import it.unisalento.recproject.adminservice.domain.Admin;
import it.unisalento.recproject.adminservice.dto.AdminDTO;
import it.unisalento.recproject.adminservice.dto.AdminListDTO;
import it.unisalento.recproject.adminservice.exceptions.AdminException;
import it.unisalento.recproject.adminservice.reporitories.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/amministratore")
public class AdminRestController {

    @Autowired
    AdminRepository adminRepository;

    @RequestMapping(value = "/incasso/{email}", method = RequestMethod.GET)
    public float getIncasso(@PathVariable String email) throws AdminException {

        Optional<Admin> amministratore = adminRepository.findByEmail(email);

        if(amministratore.isEmpty()){
            throw new AdminException();
        }

        return amministratore.get().getIncasso();
    }

    @RequestMapping(value = "/saldo/{email}", method = RequestMethod.GET)
    public float getSaldo(@PathVariable String email) throws AdminException {
        Optional<Admin> amministratore = adminRepository.findByEmail(email);

        if(amministratore.isEmpty()){
            throw new AdminException();
        }

        return amministratore.get().getSaldo();
    }

    @RequestMapping(value = "/performance_REC", method = RequestMethod.GET)
    public float getPerformance() {
        /**Implementare la logica per calcolare la performance
         **/
        float performance = 100;
        return performance;
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

            list.add(amministratoreDTO);
        }
        return amministratoreListDTO;
    }

    @RequestMapping(value = "/update/{email}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public String update(@RequestBody AdminDTO amministratoreDTO, @PathVariable String email) throws AdminException {
        Optional<Admin> amministratore = adminRepository.findByEmail(email);

        if (amministratore.isEmpty()){
            throw new AdminException();
        }

        amministratore.get().setEmail(amministratoreDTO.getEmail());

        adminRepository.save(amministratore.get());

        return "L'amministratore con id: " + email + " è stato aggiornato!";
    }
}
