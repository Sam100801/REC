package it.unisalento.recproject.adminservice.RestControllers;

import it.unisalento.recproject.adminservice.domain.Admin;
import it.unisalento.recproject.adminservice.domain.OrderAdmin;
import it.unisalento.recproject.adminservice.domain.OrderUser;
import it.unisalento.recproject.adminservice.dto.*;
import it.unisalento.recproject.adminservice.dto.RewardDTO;
import it.unisalento.recproject.adminservice.exceptions.AdminException;
import it.unisalento.recproject.adminservice.reporitories.AdminRepository;
import it.unisalento.recproject.adminservice.security.AuthorizationAPI;
import it.unisalento.recproject.adminservice.security.JwtUtilities;
import it.unisalento.recproject.adminservice.service.pdf.*;
import it.unisalento.recproject.adminservice.service.pdf.componimentoPDFBolletta.AddressDetails;
import it.unisalento.recproject.adminservice.service.pdf.componimentoPDFBolletta.HeaderDetails;
import it.unisalento.recproject.adminservice.service.pdf.componimentoPDFBolletta.ProductTableHeader;
import it.unisalento.recproject.adminservice.service.response.PaymentResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.swing.plaf.BorderUIResource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/amministratore")
public class AdminRestController {

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    JwtUtilities jwtUtilities;

    private static int invoiceNumber = 1;

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

    @RequestMapping(value = "/EnergiaPDF/{email}", method = RequestMethod.POST)
    public void creaPdfEnergiaAcquistata(@PathVariable String email, HttpServletResponse response) throws IOException {

        String invoiceNo = String.valueOf(invoiceNumber++);

        LocalDate ls = LocalDate.now();
        String pdfName = "bolletta_" + ls + ".pdf";
        PdfBolletta pdfInvoiceCreator = new PdfBolletta(pdfName);
        pdfInvoiceCreator.createDocument();

        HeaderDetails headerDetails = new HeaderDetails();
        headerDetails.setInvoiceNo(invoiceNo).setInvoiceDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).setInvoiceTitle("Bolletta REC").setInvoiceNoText("Numero fattura:").setInvoiceDateText("Data fattura:");
        pdfInvoiceCreator.createHeader(headerDetails);

        // Chiamata all'API del microservizio venditaEnergia per ottenere le vendite di energia dell'utente
        RestTemplate restTemplate = new RestTemplate();
        String urlEnergia = "http://host.docker.internal:8085/api/venditaEnergia/allByDateAndEmail/" + email;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<VenditaEnergiaListDTO> responseEntityEnergia = restTemplate.exchange(urlEnergia, HttpMethod.GET, requestEntity,VenditaEnergiaListDTO.class);
        VenditaEnergiaListDTO venditaEnergiaListDTO = responseEntityEnergia.getBody();
        assert venditaEnergiaListDTO != null;


        AddressDetails addressDetails = new AddressDetails();
        addressDetails.setBillingCompany("REC").setBillingName("Samuel Schirinzi").setBillingAddress("Via Piave 60").setBillingEmail("progettoprincipi167@gmail.com").setShippingName(venditaEnergiaListDTO.getList().get(0).getEmailAcquirente()).setShippingAddress(venditaEnergiaListDTO.getList().get(0).getViaAcquirente()).build();

        pdfInvoiceCreator.createAddress(addressDetails);

        ProductTableHeader productTableHeader = new ProductTableHeader();
        productTableHeader.setDescription("Energia acquistata").build();

        pdfInvoiceCreator.createTableHeader(productTableHeader);
        List<VenditaEnergiaDTO> productList = pdfInvoiceCreator.getDummyProductList(venditaEnergiaListDTO);
        productList = pdfInvoiceCreator.getDummyProductList(venditaEnergiaListDTO);
        pdfInvoiceCreator.createProduct(productList);

        // Chiamata all'API del microservizio pagamento per ottenere il link di pagamento
        String url = "http://host.docker.internal:8082/api/payments/create-payment-link-user";

        ResponseEntity<PaymentResponse> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, PaymentResponse.class);

        // Ottenimento del corpo della risposta
        PaymentResponse paymentResponse = responseEntity.getBody();
        assert paymentResponse != null;

        List<String> tncList = new ArrayList<>();
        tncList.add("- Ci impegniamo a proteggere la privacy dei nostri clienti e ad utilizzare le loro informazioni personali solo per scopi commerciali interni. Consulta la nostra politica sulla privacy per ulteriori informazioni.");
        tncList.add("- Il pagamento deve essere effettuato entro 30 giorni dalla data della fattura.");

        String imagePath = "https://www.communityenergyinc.com/wp-content/uploads/download.jpg";

        pdfInvoiceCreator.createTnc(tncList, false, imagePath);
        pdfInvoiceCreator.addClickableButton("Clicca qui per pagare", paymentResponse.getPaymentLink());

        pdfInvoiceCreator.closeDocument();

        File pdfFile = new File(pdfName);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=" + pdfFile.getName());
        response.setContentLength((int) pdfFile.length());
        Files.copy(pdfFile.toPath(), response.getOutputStream());
        response.getOutputStream().flush();

        System.out.println("PDF created successfully");
    }

    @RequestMapping(value = "/RewardAdminPDF/{id}", method = RequestMethod.POST)
    public void creaPdfRewardAdmin(@PathVariable String id, HttpServletResponse response) throws IOException {

        String invoiceNo = String.valueOf(invoiceNumber++);

        LocalDate ls = LocalDate.now();
        String pdfName = "AcquistoRewardAdmin" + ls + ".pdf";
        PdfOrdineRewardAdmin pdfInvoiceCreator = new PdfOrdineRewardAdmin(pdfName);
        pdfInvoiceCreator.createDocument();

        it.unisalento.recproject.adminservice.service.pdf.componimentoPDFOrdineReward.HeaderDetails headerDetails = new it.unisalento.recproject.adminservice.service.pdf.componimentoPDFOrdineReward.HeaderDetails();
        headerDetails.setInvoiceNo(invoiceNo).setInvoiceDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).setInvoiceTitle("Fattura REC").setInvoiceNoText("Numero fattura:").setInvoiceDateText("Data fattura:");
        pdfInvoiceCreator.createHeader(headerDetails);

        RestTemplate restTemplate = new RestTemplate();
        String urlOrder = "http://host.docker.internal:8087/api/order/getAdmin/" + id;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ResponseEntity <OrderAdmin> responseEntityOrder = restTemplate.exchange(urlOrder, HttpMethod.GET, requestEntity, OrderAdmin.class);

        OrderAdmin orderAdmin = responseEntityOrder.getBody();


        it.unisalento.recproject.adminservice.service.pdf.componimentoPDFOrdineReward.AddressDetails addressDetails = new it.unisalento.recproject.adminservice.service.pdf.componimentoPDFOrdineReward.AddressDetails();
        assert orderAdmin != null;
        addressDetails.setBillingCompany("REC").setBillingName("Samuel Miglietta").setBillingAddress("Via Piave 60").setBillingEmail("samuel@gmail.com").setShippingName("Cliente Samuel").setShippingAddress("Via delle rose 60").build();

        pdfInvoiceCreator.createAddress(addressDetails);

        it.unisalento.recproject.adminservice.service.pdf.componimentoPDFOrdineReward.ProductTableHeader productTableHeader = new it.unisalento.recproject.adminservice.service.pdf.componimentoPDFOrdineReward.ProductTableHeader();
        productTableHeader.setDescription("Reward Acquistati").build();

        pdfInvoiceCreator.createTableHeader(productTableHeader);

        List<RewardDTO> productList = pdfInvoiceCreator.getDummyProductList(orderAdmin);
        productList = pdfInvoiceCreator.getDummyProductList(orderAdmin);
        pdfInvoiceCreator.createProduct(productList);

        // Chiamata all'API del microservizio pagamento per ottenere il link di pagamento
        String urlPagamento = "http://host.docker.internal:8082/api/payments/create-payment-link-admin";
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<PaymentResponse> responseEntity = restTemplate.exchange(urlPagamento, HttpMethod.POST, requestEntity, PaymentResponse.class);

        // Ottenimento del corpo della risposta
        PaymentResponse paymentResponse = responseEntity.getBody();
        assert paymentResponse != null;

        List<String> tncList = new ArrayList<>();
        tncList.add("- Ci impegniamo a proteggere la privacy dei nostri clienti e ad utilizzare le loro informazioni personali solo per scopi commerciali interni. Consulta la nostra politica sulla privacy per ulteriori informazioni.");
        tncList.add("- Il pagamento deve essere effettuato entro 30 giorni dalla data della fattura.");

        String imagePath = "https://www.communityenergyinc.com/wp-content/uploads/download.jpg";

        pdfInvoiceCreator.createTnc(tncList, false, imagePath);
        pdfInvoiceCreator.addClickableButton("Clicca qui per pagare", paymentResponse.getPaymentLink());

        pdfInvoiceCreator.closeDocument();

        File pdfFile = new File(pdfName);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=" + pdfFile.getName());
        response.setContentLength((int) pdfFile.length());
        Files.copy(pdfFile.toPath(), response.getOutputStream());
        response.getOutputStream().flush();

        System.out.println("PDF created successfully");

    }

    @RequestMapping(value = "/RewardUserPDF/{id}", method = RequestMethod.POST)
    public void creaPdfRewardUser(@PathVariable String id, HttpServletResponse response) throws IOException {

        String invoiceNo = String.valueOf(invoiceNumber++);

        LocalDate ls = LocalDate.now();
        String pdfName = "AcquistoRewardUser" + ls + ".pdf";
        PdfOrdineRewardUser pdfInvoiceCreator = new PdfOrdineRewardUser(pdfName);
        pdfInvoiceCreator.createDocument();

        it.unisalento.recproject.adminservice.service.pdf.componimentoPDFOrdineReward.HeaderDetails headerDetails = new it.unisalento.recproject.adminservice.service.pdf.componimentoPDFOrdineReward.HeaderDetails();
        headerDetails.setInvoiceNo(invoiceNo).setInvoiceDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).setInvoiceTitle("Fattura REC").setInvoiceNoText("Numero fattura:").setInvoiceDateText("Data fattura:");
        pdfInvoiceCreator.createHeader(headerDetails);

        RestTemplate restTemplate = new RestTemplate();
        String urlOrder = "http://host.docker.internal:8087/api/order/getUser/" + id;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ResponseEntity <OrderUser> responseEntityOrder = restTemplate.exchange(urlOrder, HttpMethod.GET, requestEntity, OrderUser.class);

        OrderUser orderUser = responseEntityOrder.getBody();


        it.unisalento.recproject.adminservice.service.pdf.componimentoPDFOrdineReward.AddressDetails addressDetails = new it.unisalento.recproject.adminservice.service.pdf.componimentoPDFOrdineReward.AddressDetails();
        assert orderUser != null;
        addressDetails.setBillingCompany("REC").setBillingName("Samuel Miglietta").setBillingAddress("Via Piave 60").setBillingEmail("samuel@gmail.com").setShippingName("Cliente Samuel").setShippingAddress("Via delle rose 60").build();

        pdfInvoiceCreator.createAddress(addressDetails);

        it.unisalento.recproject.adminservice.service.pdf.componimentoPDFOrdineReward.ProductTableHeader productTableHeader = new it.unisalento.recproject.adminservice.service.pdf.componimentoPDFOrdineReward.ProductTableHeader();
        productTableHeader.setDescription("Reward Acquistati").build();

        pdfInvoiceCreator.createTableHeader(productTableHeader);

        List<RewardDTO> productList = pdfInvoiceCreator.getDummyProductList(orderUser);
        productList = pdfInvoiceCreator.getDummyProductList(orderUser);
        pdfInvoiceCreator.createProduct(productList);

        List<String> tncList = new ArrayList<>();
        tncList.add("- Ci impegniamo a proteggere la privacy dei nostri clienti e ad utilizzare le loro informazioni personali solo per scopi commerciali interni. Consulta la nostra politica sulla privacy per ulteriori informazioni.");
        tncList.add("- Il pagamento deve essere effettuato entro 30 giorni dalla data della fattura.");

        String imagePath = "https://www.communityenergyinc.com/wp-content/uploads/download.jpg";

        pdfInvoiceCreator.createTnc(tncList, false, imagePath);
        pdfInvoiceCreator.closeDocument();

        File pdfFile = new File(pdfName);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=" + pdfFile.getName());
        response.setContentLength((int) pdfFile.length());
        Files.copy(pdfFile.toPath(), response.getOutputStream());
        response.getOutputStream().flush();

        System.out.println("PDF created successfully");
    }
}
