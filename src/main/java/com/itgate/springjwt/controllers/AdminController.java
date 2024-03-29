package com.itgate.springjwt.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.itgate.springjwt.models.Admin;
import com.itgate.springjwt.models.Client;
import com.itgate.springjwt.models.ERole;
import com.itgate.springjwt.models.ResourceNotFoundException;
import com.itgate.springjwt.models.Role;
import com.itgate.springjwt.payload.request.SignupRequest;
import com.itgate.springjwt.payload.response.MessageResponse;
import com.itgate.springjwt.repository.AdminRepository;
import com.itgate.springjwt.repository.RoleRepository;
import com.itgate.springjwt.repository.UserRepository;
import com.itgate.springjwt.utils.StorageService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/Admins")
@CrossOrigin("*")

public class AdminController {
	@Autowired
	AdminRepository adminRepository;
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
	    
	    
	    
	    @GetMapping("/files/{filename:.+}")
	  	public ResponseEntity<Resource>getFile(@PathVariable String filename){
	  	        Resource file=service.loadFile(filename);
	  	        return ResponseEntity.ok()
	  	                .header(HttpHeaders.CONTENT_DISPOSITION,"attachement;filename=\""+file.getFilename()+"\"")
	  	                        .body(file);
	  	}

	@GetMapping(path = "/All")
	public List<Admin> getAll(){
		return adminRepository.findAll();
	}
	@PostMapping(path = "/Add")
	public Admin CreatAdmin(@RequestBody Admin a) {
		return adminRepository.save(a);
		
	}
	@GetMapping(path = "/findbyid/{id}")
	public Admin FindAdminById(@PathVariable Long id) {
		return adminRepository.findById(id).orElse(null);
	}
	 @PutMapping("/updateAdmin/{id}")
	    public ResponseEntity<Admin> updateAdmin(@PathVariable("id") long id,
	                                                @ModelAttribute Admin adminDetails,
	                                                @RequestParam(value = "file", required = false) MultipartFile file) {
	        Admin admin = adminRepository.findById(id)
	            .orElseThrow(() -> new ResourceNotFoundException("Admin not found with id: " + id));

	        // Update fields that are specified in the request
	        if (adminDetails.getUsername() != null) {
	            admin.setUsername(adminDetails.getUsername());
	        }

	        if (adminDetails.getEmail() != null) {
	            admin.setEmail(adminDetails.getEmail());
	        }

	       

	        // Save the updated client if no file was provided
	        if (file == null || file.isEmpty()) {
	            Admin updatedAdmin = adminRepository.save(admin);
	            return ResponseEntity.ok(updatedAdmin);
	        }

	        // Delete the previous image if it exists
	        if (admin.getImage() != null) {
	            try {
	                Files.deleteIfExists(this.rootLocation.resolve(admin.getImage()));
	            } catch (IOException e) {
	                throw new RuntimeException("Failed to delete previous image: " + admin.getImage(), e);
	            }
	        }

	        // Save the new image
	        try {
	            String fileName = Integer.toString(new Random().nextInt(1000000));
	            String ext = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf('.'), file.getOriginalFilename().length());
	            String name = file.getOriginalFilename().substring(0, file.getOriginalFilename().indexOf('.'));
	            String original = name + fileName + ext;
	            Files.copy(file.getInputStream(), this.rootLocation.resolve(original));
	            admin.setImage(original);
	        } catch (Exception e) {
	            throw new RuntimeException("Failed to save new image", e);
	        }

	        // Save the updated client with the new image
	        Admin updatedAdmin = adminRepository.save(admin);
	        return ResponseEntity.ok(updatedAdmin);
	    }

	@DeleteMapping("/Delet/{id}")
	public ResponseEntity DeletAdmin(@PathVariable Long id) {
		 adminRepository.deleteById(id);
		 return ResponseEntity.ok().build();
	}
	@PostMapping
    public Admin saveproduct(@RequestParam("file")MultipartFile file,Admin admin) {
        try {
            String fileName = Integer.toString(new Random().nextInt(1000000));
            String ext = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf('.'), file.getOriginalFilename().length());
            String name = file.getOriginalFilename().substring(0, file.getOriginalFilename().indexOf('.'));
            String original = name + fileName + ext;
            Files.copy(file.getInputStream(), this.rootLocation.resolve(original));
           
            
            admin.setImage(original);
            return adminRepository.save(admin);

        } catch (Exception e) {
            throw new RuntimeException("fail file problem backend");
        }
    }


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid SignupRequest signUpRequest, @RequestParam("file") MultipartFile file, Admin a) throws MessagingException {


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
        Admin admin = new Admin(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getDescription());

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();


        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            Role ADRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(ADRole);

        }
        try {
            String fileName = Integer.toString(new Random().nextInt(1000000));
            String ext = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf('.'), file.getOriginalFilename().length());
            String name = file.getOriginalFilename().substring(0, file.getOriginalFilename().indexOf('.'));
            String original = name + fileName + ext;
            Files.copy(file.getInputStream(), this.rootLocation.resolve(original));
            admin.setImage(original);

        } catch (Exception e) {
            throw new RuntimeException("fail file problem backend");
        }
        admin.setConfirm(false); //mail confirm
        String from = "Do-Lisha@gmail.com";
        String to = signUpRequest.getEmail();
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setSubject("Complete registration");
        helper.setFrom(from);
        helper.setTo(to);
        helper.setText("<HTML><body><a href=\"http://localhost:8088/Admins/updateconfirm?email=" + signUpRequest.getEmail() + "\">VERIFY</a></body></HTML>", true);
        mailSender.send(message);

        admin.setRoles(roles);
        userRepository.save(admin);

        return ResponseEntity.ok(new MessageResponse("Admin registered successfully!"));
    }


    @GetMapping("/updateconfirm")
    public String updateconfirm(String email) {
        Admin c1 = adminRepository.findByEmail(email);
        if (c1 != null) {
            c1.setConfirm(true);
            adminRepository.saveAndFlush(c1);
            return "verify";
        } else {
            throw new RuntimeException("fail");

        }
    }
   
}
