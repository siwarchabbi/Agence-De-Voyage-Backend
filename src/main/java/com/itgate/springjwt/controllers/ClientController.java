package com.itgate.springjwt.controllers;

import com.itgate.springjwt.models.Client;
import com.itgate.springjwt.models.ERole;
import com.itgate.springjwt.models.ResourceNotFoundException;
import com.itgate.springjwt.models.Role;
import com.itgate.springjwt.models.User;
import com.itgate.springjwt.payload.request.SignupRequest;
import com.itgate.springjwt.payload.response.MessageResponse;
import com.itgate.springjwt.repository.ClientRepository;
import com.itgate.springjwt.repository.RoleRepository;
import com.itgate.springjwt.repository.UserRepository;
import com.itgate.springjwt.utils.StorageService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

@RestController
@RequestMapping("/Clients")
@CrossOrigin("*")

public class ClientController {
    @Autowired
    ClientRepository clientRepository;

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
	private StorageService service;
    public final java.nio.file.Path rootLocation =  Paths.get("upload");
    
  


    @GetMapping
    public List<Client> getall() {
        return clientRepository.findAll();
    }

    @GetMapping("/{id}")
    public Client getOneClient( @PathVariable Long id) {
        return clientRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Client createUser(@RequestBody Client c) {
        return clientRepository.save(c);
    }

    @PutMapping("/{id}")
    public Client updateUser(@RequestBody Client c, @PathVariable Long id) {
        Client c1 = clientRepository.findById(id).orElse(null);
        if (c1 != null) {
            c.setId(id);
            return clientRepository.saveAndFlush(c);
        } else {
            throw new RuntimeException("fail");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable Long id) {
        clientRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/updateClient2/{id}")
    public ResponseEntity<Client> updateClient2(@PathVariable("id") long id,
                                                @ModelAttribute Client clientDetails,
                                                @RequestParam(value = "file", required = false) MultipartFile file) {
        Client client = clientRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));

        // Update fields that are specified in the request
        if (clientDetails.getUsername() != null) {
            client.setUsername(clientDetails.getUsername());
        }

        if (clientDetails.getEmail() != null) {
            client.setEmail(clientDetails.getEmail());
        }

        if (clientDetails.getAdress() != null) {
            client.setAdress(clientDetails.getAdress());
        }

        if (clientDetails.getCity() != null) {
            client.setCity(clientDetails.getCity());
        }

        // Save the updated client if no file was provided
        if (file == null || file.isEmpty()) {
            Client updatedClient = clientRepository.save(client);
            return ResponseEntity.ok(updatedClient);
        }

        // Delete the previous image if it exists
        if (client.getImage() != null) {
            try {
                Files.deleteIfExists(this.rootLocation.resolve(client.getImage()));
            } catch (IOException e) {
                throw new RuntimeException("Failed to delete previous image: " + client.getImage(), e);
            }
        }

        // Save the new image
        try {
            String fileName = Integer.toString(new Random().nextInt(1000000));
            String ext = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf('.'), file.getOriginalFilename().length());
            String name = file.getOriginalFilename().substring(0, file.getOriginalFilename().indexOf('.'));
            String original = name + fileName + ext;
            Files.copy(file.getInputStream(), this.rootLocation.resolve(original));
            client.setImage(original);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save new image", e);
        }

        // Save the updated client with the new image
        Client updatedClient = clientRepository.save(client);
        return ResponseEntity.ok(updatedClient);
    }
    @PutMapping("/{id}/Confirm")
    public ResponseEntity<Map<String, Boolean>> toggleClientConfirm(@PathVariable Long id) {
        clientRepository.toggleClientConfirm(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("confirm", clientRepository.findById(id).get().getConfirm());
        return ResponseEntity.ok(response);
    }


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid SignupRequest signUpRequest, @RequestParam("file") MultipartFile file, Client c) throws MessagingException {


        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new CUSTOMER's account
        Client client = new Client(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getAdress(), signUpRequest.getCity());

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();


        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            Role CUSRole = roleRepository.findByName(ERole.ROLE_CUSTOMER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(CUSRole);

        }
        try {
            String fileName = Integer.toString(new Random().nextInt(1000000));
            String ext = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf('.'), file.getOriginalFilename().length());
            String name = file.getOriginalFilename().substring(0, file.getOriginalFilename().indexOf('.'));
            String original = name + fileName + ext;
            Files.copy(file.getInputStream(), this.rootLocation.resolve(original));
            client.setImage(original);

        } catch (Exception e) {
            throw new RuntimeException("fail file problem backend");
        }
        client.setConfirm(false); //mail confirm
        String from = "Do-liSha@gmail.com";
        String to = signUpRequest.getEmail();
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setSubject("Complete registration in DoLisha");
        helper.setFrom(from);
        helper.setTo(to);
        helper.setText("<HTML><body><p>Cher(e) client(e), </p>\n"
        		+ "\n"
        		+ "<p>Nous sommes ravis de vous accueillir sur notre site de voyage ! Pour compléter votre inscription et profiter pleinement de nos services, il vous suffit de cliquer sur le lien ci-dessous :"
        		+ "</p><a href=\"http://localhost:8088/Clients/updateconfirm?email=" + signUpRequest.getEmail() + "\">VÉRIFIER MON COMPTE </a><p>En cliquant sur ce lien, vous serez dirigé(e) vers une page où vous pourrez finaliser votre inscription en fournissant les informations nécessaires. Une fois cette étape terminée, vous pourrez découvrir toutes les fonctionnalités de notre site, parcourir nos offres exceptionnelles et planifier vos prochaines aventures en toute simplicité.\n"
        				+ "\n"
        				+ "Nous sommes convaincus que notre site de voyage répondra à toutes vos attentes et vous offrira une expérience unique. Notre équipe reste à votre disposition pour toute assistance ou information supplémentaire dont vous pourriez avoir besoin.\n"
        				+ "\n"
        				+ "</p><p>Nous vous remercions de votre confiance et nous sommes impatients de vous accompagner dans votre exploration du monde.\n"
        				+ "\n"
        				+ "Bienvenue dans notre communauté de voyageurs !\n"
        				+ "</p>"
        				+ "<p>Cordialement,</p>"
        				+ "<p>L'équipe de DoLisha</p></body></HTML>", true);
        mailSender.send(message);

        client.setRoles(roles);
        userRepository.save(client);

        return ResponseEntity.ok(new MessageResponse("customer registered successfully!"));
    }


    @GetMapping("/updateconfirm")
    public String updateconfirm(String email) {
        Client c1 = clientRepository.findByEmail(email);
        if (c1 != null) {
            c1.setConfirm(true);
            clientRepository.saveAndFlush(c1);
            return "verify";
        } else {
            throw new RuntimeException("fail");

        }
    }
 
    @GetMapping("/files/{filename:.+}")
	public ResponseEntity<Resource>getFile(@PathVariable String filename){
	        Resource file=service.loadFile(filename);
	        return ResponseEntity.ok()
	                .header(HttpHeaders.CONTENT_DISPOSITION,"attachement;filename=\""+file.getFilename()+"\"")
	                        .body(file);
	}
}

