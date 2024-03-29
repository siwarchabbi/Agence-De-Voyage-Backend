package com.itgate.springjwt.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.itgate.springjwt.models.Admin;
import com.itgate.springjwt.models.Hotel;
import com.itgate.springjwt.models.ResourceNotFoundException;
import com.itgate.springjwt.models.ResponseMessage;
import com.itgate.springjwt.models.Vehicule;
import com.itgate.springjwt.repository.AdminRepository;
import com.itgate.springjwt.repository.VehiculeRepository;
import com.itgate.springjwt.utils.StorageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;



@RestController
@RequestMapping("/Vehicules")
@CrossOrigin("*")

public class VehiculeController {
	
	@Autowired
	VehiculeRepository vehiculeRepository;
	
	@Autowired
	AdminRepository adminRepository;
	@Autowired
    private StorageService storage;
		private StorageService service;
	    public final java.nio.file.Path rootLocation =  Paths.get("upload");
	@GetMapping(path = "/All")
	public List<Vehicule> getAll(){
		return vehiculeRepository.findAll();
	}
	
	@DeleteMapping("/Delet/{id}")
	public ResponseEntity DeletVehicule(@PathVariable Long id) {
		 vehiculeRepository.deleteById(id);
		 return ResponseEntity.ok().build();
	}
	@RequestMapping(path = "/upload3/{admin_id}", method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseMessage> uploadFilesAndSaveVehicule(@RequestParam("files") MultipartFile[] files,
                                                                     @RequestParam(name = "file", required = false) MultipartFile file,
                                                                     Vehicule vehicule, 
                                                                     @PathVariable Long admin_id) {

        try {
            // Handle multiple files
            if (files != null && files.length > 0) {
                ArrayList<String> fileNames = new ArrayList<>();
                Arrays.asList(files).stream().forEach(fileItem -> {
                    try {
                        String fileName = Integer.toString(new Random().nextInt(1000000));
                        String ext = fileItem.getOriginalFilename().substring(fileItem.getOriginalFilename().indexOf('.'), fileItem.getOriginalFilename().length());
                        String name = fileItem.getOriginalFilename().substring(0, fileItem.getOriginalFilename().indexOf('.'));
                        String original = name + fileName + ext;
                        Files.copy(fileItem.getInputStream(), this.rootLocation.resolve(original));
                        fileNames.add(original);
                    } catch (Exception e) {
                        throw new RuntimeException("fail file problem backend");
                    }
                });
                vehicule.setImages(fileNames);
            }

            // Handle single file
            if (file != null) {
                String fileName = Integer.toString(new Random().nextInt(1000000));
                String ext = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf('.'), file.getOriginalFilename().length());
                String name = file.getOriginalFilename().substring(0, file.getOriginalFilename().indexOf('.'));
                String original = name + fileName + ext;
                Files.copy(file.getInputStream(), this.rootLocation.resolve(original));
                vehicule.setImage(original);
            }

            Admin en = adminRepository.findById(admin_id).orElse(null);
            vehicule.setAdmin(en);
            vehicule.setStatus(true);

            vehiculeRepository.save(vehicule);
            String message = "Uploaded the file(s) vehicule successfully";
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));

        } catch (Exception e) {
            String message = "Fail to upload file(s)";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }
	@GetMapping("/files/{filename:.+}")
	public ResponseEntity<Resource>getFile(@PathVariable String filename){
	        Resource file=storage.loadFile(filename);
	        return ResponseEntity.ok()
	                .header(HttpHeaders.CONTENT_DISPOSITION,"attachement;filename=\""+file.getFilename()+"\"")
	                        .body(file);
	}
	@Operation(summary = "get a vehicule by its id")
	@ApiResponses(value= {
	  @ApiResponse (responseCode = "200" ,description = "vehicule found",
	    content = { @Content(mediaType = "application/json",
	    schema = @Schema(implementation = Vehicule.class))}),

	  @ApiResponse (responseCode = "400" ,description = "invalid is supplied",
	    content = @Content),
	  @ApiResponse (responseCode = "404" ,description = "vehicule not found ",
	    content = @Content)
	})
	@PostMapping(path = "/Add")
	public Vehicule CreatVehicule(@RequestBody Vehicule v) {
		return vehiculeRepository.save(v);
		
	}
	@GetMapping(path = "/findbyid/{id}")
	public Vehicule FindVehiculeById(@PathVariable Long id) {
		return vehiculeRepository.findById(id).orElse(null);
	}
	@PutMapping("/update/{id}")
	public Vehicule UpdateVehicule(@RequestBody Vehicule v , @PathVariable Long id) {
		Vehicule v1=vehiculeRepository.findById(id).orElse(null);
		if(v1!=null) {
			v.setId(id);
			return vehiculeRepository.saveAndFlush(v);
		}else {
			throw new RuntimeException("Errer");
		}
	}
	
	@RequestMapping(path = "/toggleStatus/{vehiculeId}", method = RequestMethod.PUT)
	public ResponseEntity<ResponseMessage> toggleStatus(@PathVariable Long vehiculeId) {
	    Optional<Vehicule> optionalVehicule = vehiculeRepository.findById(vehiculeId);

	    if (optionalVehicule.isPresent()) {
	        Vehicule vehicule = optionalVehicule.get();
	        Boolean currentStatus = vehicule.getStatus();

	        if (currentStatus == null || !currentStatus) {
	            vehicule.setStatus(true); // Set status to true
	        } else {
	            vehicule.setStatus(false); // Set status to false
	        }

	        vehiculeRepository.save(vehicule);

	        String message = "Status of vehicule with ID " + vehiculeId + " has been toggled.";
	        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
	    } else {
	        String message = "vehicule with ID " + vehiculeId + " does not exist.";
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(message));
	    }
	}

       
	
	@PutMapping("/updateVehicule/{id}")
	public ResponseEntity<Vehicule> updateVehicule(@PathVariable("id") long id,
			                                @RequestParam("admin_id") long admin_id,
	                                        @ModelAttribute Vehicule vehiculeDetails,
	                                        @RequestParam(value = "file", required = false) MultipartFile file,
	                                        @RequestParam(value = "files", required = false) MultipartFile[] files)
	
	{
		
		  // Check if the admin ID is valid before proceeding
	    if (!adminRepository.existsById(admin_id)) {
	        throw new ResourceNotFoundException("Admin not found with id: " + admin_id);
	    }
	    Vehicule vehicule = vehiculeRepository.findById(id)
	            .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + id));

	    // Update fields that are specified in the request
	    if (vehiculeDetails.getName() != null) {
	        vehicule.setName(vehiculeDetails.getName());
	    }

	    if (vehiculeDetails.getPays() != null) {
	        vehicule.setPays(vehiculeDetails.getPays());
	    }

	    if (vehiculeDetails.getPlace() != null) {
	        vehicule.setPlace(vehiculeDetails.getPlace());
	    }
	    if (vehiculeDetails.getPromo() != null || vehiculeDetails.getPromo() == null) {
	    	vehicule.setPromo(vehiculeDetails.getPromo());
	    }
	    if (vehiculeDetails.getPrixpromo() != 0) {
	    	vehicule.setPrixpromo(vehiculeDetails.getPrixpromo());
	    }
	    if (vehiculeDetails.getPrix() != null) {
	    	vehicule.setPrix(vehiculeDetails.getPrix());
	    }if (vehiculeDetails.getMatricule() != null) {
	    	vehicule.setMatricule(vehiculeDetails.getMatricule());
	    }if (vehiculeDetails.getChauffeur() != null) {
	    	vehicule.setChauffeur(vehiculeDetails.getChauffeur());
	    }
	   
	    // Save the updated Vehicule if no files were provided
	    if (file == null || file.isEmpty() || (files == null || files.length == 0)) {
	        Vehicule updatedVehicule = vehiculeRepository.save(vehicule);
	        return ResponseEntity.ok(updatedVehicule);
	    }

	    // Delete the previous single image if it exists
	    if (vehicule.getImage() != null && file != null) {
	        try {
	            Files.deleteIfExists(this.rootLocation.resolve(vehicule.getImage()));
	        } catch (IOException e) {
	            throw new RuntimeException("Failed to delete previous image: " + vehicule.getImage(), e);
	        }
	    }

	    // Delete the previous image collection if it exists
	    if (vehicule.getImages() != null && files != null) {
	        for (String image : vehicule.getImages()) {
	            try {
	                Files.deleteIfExists(this.rootLocation.resolve(image));
	            } catch (IOException e) {
	                throw new RuntimeException("Failed to delete previous image: " + image, e);
	            }
	        }
	    }

	    // Save the new single image
	    if (file != null && !file.isEmpty()) {
	        try {
	            String fileName = Integer.toString(new Random().nextInt(1000000));
	            String ext = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf('.'), file.getOriginalFilename().length());
	            String name = file.getOriginalFilename().substring(0, file.getOriginalFilename().indexOf('.'));
	            String original = name + fileName + ext;
	            Files.copy(file.getInputStream(), this.rootLocation.resolve(original));
	            vehicule.setImage(original);
	        } catch (Exception e) {
	            throw new RuntimeException("Failed to save new image", e);
	        }
	    }

	    // Save the new image collection
	    if (files != null && files.length > 0) {
	        ArrayList<String> fileNames = new ArrayList<>();
	        Arrays.asList(files).stream().forEach(fileItem -> {
	            try {
	                String fileName = Integer.toString(new Random().nextInt(1000000));
	                String ext = fileItem.getOriginalFilename().substring(fileItem.getOriginalFilename().indexOf('.'), fileItem.getOriginalFilename().length());
	                String name = fileItem.getOriginalFilename().substring(0, fileItem.getOriginalFilename().indexOf('.'));
	                String original = name + fileName + ext;
	                Files.copy(fileItem.getInputStream(), this.rootLocation.resolve(original));
	                fileNames.add(original);
	            } catch (Exception e) {
	                throw new RuntimeException("Failed to save new image", e);
	            }
	        });
	        vehicule.setImages(fileNames);
	    }

	    // Save the updated Vehicule with the new image(s)
	    Vehicule updatedVehicule = vehiculeRepository.save(vehicule);
	    return ResponseEntity.ok(updatedVehicule);
	}
}
