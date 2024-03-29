package com.itgate.springjwt.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itgate.springjwt.models.Admin;
import com.itgate.springjwt.models.Client;
import com.itgate.springjwt.models.Reclamation;
import com.itgate.springjwt.models.ResourceNotFoundException;
import com.itgate.springjwt.repository.AdminRepository;
import com.itgate.springjwt.repository.ClientRepository;
import com.itgate.springjwt.repository.ReclamationRepository;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/reclamations")
@CrossOrigin("*")
public class ReclamationController {

    @Autowired
    private ReclamationRepository reclamationRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AdminRepository adminRepository;

    @PostMapping("/savreclamation/{client_id}")
    public Reclamation createReclamation(@RequestBody Reclamation reclamation, @PathVariable Long client_id) {
        Client client = clientRepository.findById(client_id)
            .orElseThrow(() -> new ResourceNotFoundException("Client not found"));
        reclamation.setClient(client);
        reclamation.setMessg(reclamation.getMessg());
        return reclamationRepository.save(reclamation);
    }





    @GetMapping("/{id}")
    public Reclamation getReclamation(@PathVariable Long id) {
        return reclamationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Reclamation not found"));
    }

    @GetMapping
    public Collection<Reclamation> getAllReclamations() {
        return reclamationRepository.findAll();
    }

    @PostMapping("/{id}/reply")
    public Reclamation replyToReclamation(@PathVariable Long id, @RequestBody String reply) {
        Admin admin = adminRepository.findById(1L)
            .orElseThrow(() -> new ResourceNotFoundException("Admin not found"));
        Reclamation reclamation = reclamationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Reclamation not found"));
        reclamation.setAdmin(admin);
        reclamation.setMessg(reply);
        return reclamationRepository.save(reclamation);
    }
}
