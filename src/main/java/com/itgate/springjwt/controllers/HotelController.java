package com.itgate.springjwt.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.itgate.springjwt.models.Admin;
import com.itgate.springjwt.models.Hotel;
import com.itgate.springjwt.models.ResourceNotFoundException;
import com.itgate.springjwt.models.ResponseMessage;
import com.itgate.springjwt.models.Vehicule;
import com.itgate.springjwt.repository.AdminRepository;
import com.itgate.springjwt.repository.HotelRepository;
import com.itgate.springjwt.utils.StorageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;



@RestController
@RequestMapping("/Hotels")
@CrossOrigin("*")

public class HotelController {
	
	@Autowired
	HotelRepository hotelRepository;
	@Autowired
	AdminRepository adminRepository;
	 @Autowired
		private StorageService service;
	    public final java.nio.file.Path rootLocation =  Paths.get("upload");
	@GetMapping(path = "/All")
	public List<Hotel> getAll(){
		return hotelRepository.findAll();
	}
	

    @RequestMapping(path = "/upload2/{admin_id}", method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseMessage> uploadFilesAndSaveProduct(@RequestParam("files") MultipartFile[] files,
                                                                     @RequestParam(name = "file", required = false) MultipartFile file,
                                                                     Hotel hotel, 
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
                hotel.setImages(fileNames);
            }

            // Handle single file
            if (file != null) {
                String fileName = Integer.toString(new Random().nextInt(1000000));
                String ext = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf('.'), file.getOriginalFilename().length());
                String name = file.getOriginalFilename().substring(0, file.getOriginalFilename().indexOf('.'));
                String original = name + fileName + ext;
                Files.copy(file.getInputStream(), this.rootLocation.resolve(original));
                hotel.setImage(original);
            }

            Admin en = adminRepository.findById(admin_id).orElse(null);
            hotel.setAdmin(en);
            hotel.setStatus(true);

            hotelRepository.save(hotel);
            String message = "Uploaded the file(s) hotel successfully";
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));

        } catch (Exception e) {
            String message = "Fail to upload file(s)";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }
	@GetMapping("/files/{filename:.+}")
	public ResponseEntity<Resource>getFile(@PathVariable String filename){
	        Resource file=service.loadFile(filename);
	        return ResponseEntity.ok()	
	                .header(HttpHeaders.CONTENT_DISPOSITION,"attachement;filename=\""+file.getFilename()+"\"")
	                        .body(file);
	}
	@Operation(summary = "get a hotel by its id")
	@ApiResponses(value= {
	  @ApiResponse (responseCode = "200" ,description = "hotel found",
	    content = { @Content(mediaType = "application/json",
	    schema = @Schema(implementation = Hotel.class))}),

	  @ApiResponse (responseCode = "400" ,description = "invalid is supplied",
	    content = @Content),
	  @ApiResponse (responseCode = "404" ,description = "hotel not found ",
	    content = @Content)
	})
	    
	@PostMapping(path = "/Add")
	public Hotel CreatHotel(@RequestBody Hotel h) {
		return hotelRepository.save(h);
		
	}
	@GetMapping(path = "/findbyid/{id}")
	public Hotel FindHotelById(@PathVariable Long id) {
		return hotelRepository.findById(id).orElse(null);
	}
	

	@DeleteMapping("/Delet/{id}")
	public ResponseEntity DeletHotel(@PathVariable Long id) {
		 hotelRepository.deleteById(id);
		 return ResponseEntity.ok().build();
	}
	
	
	@PutMapping("/updateHotel/{id}")
	public ResponseEntity<Hotel> updateHotel(@PathVariable("id") long id,
			                                @RequestParam("admin_id") long admin_id,
	                                        @ModelAttribute Hotel hotelDetails,
	                                        @RequestParam(value = "file", required = false) MultipartFile file,
	                                        @RequestParam(value = "files", required = false) MultipartFile[] files)
	
	{
		
		  // Check if the admin ID is valid before proceeding
	    if (!adminRepository.existsById(admin_id)) {
	        throw new ResourceNotFoundException("Admin not found with id: " + admin_id);
	    }
	    Hotel hotel = hotelRepository.findById(id)
	            .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + id));

	    // Update fields that are specified in the request
	    if (hotelDetails.getName() != null) {
	        hotel.setName(hotelDetails.getName());
	    }

	    if (hotelDetails.getAdresse() != null) {
	        hotel.setAdresse(hotelDetails.getAdresse());
	    }
	    if (hotelDetails.getChambre() != null) {
	        hotel.setChambre(hotelDetails.getChambre());
	    }
	    if (hotelDetails.getRegien() != null) {
	        hotel.setRegien(hotelDetails.getRegien());
	    }
	    if (hotelDetails.getPrix() != 0) {
	        hotel.setPrix(hotelDetails.getPrix());
	    }
	    if (hotelDetails.getPromo() != null || hotelDetails.getPromo() == null) {
	        hotel.setPromo(hotelDetails.getPromo());
	    }
	    if (hotelDetails.getPrixpromo() != 0) {
	        hotel.setPrixpromo(hotelDetails.getPrixpromo());
	    }

	    if (hotelDetails.getDescription() != null) {
	        hotel.setDescription(hotelDetails.getDescription());
	    }

	    if (hotelDetails.getEtoil() != 0) {
	        hotel.setEtoil(hotelDetails.getEtoil());
	    }

	    // Save the updated hotel if no files were provided
	    if (file == null || file.isEmpty() || (files == null || files.length == 0)) {
	        Hotel updatedHotel = hotelRepository.save(hotel);
	        return ResponseEntity.ok(updatedHotel);
	    }

	    // Delete the previous single image if it exists
	    if (hotel.getImage() != null && file != null) {
	        try {
	            Files.deleteIfExists(this.rootLocation.resolve(hotel.getImage()));
	        } catch (IOException e) {
	            throw new RuntimeException("Failed to delete previous image: " + hotel.getImage(), e);
	        }
	    }

	    // Delete the previous image collection if it exists
	    if (hotel.getImages() != null && files != null) {
	        for (String image : hotel.getImages()) {
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
	            hotel.setImage(original);
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
	        hotel.setImages(fileNames);
	    }

	    // Save the updated hotel with the new image(s)
	    Hotel updatedHotel = hotelRepository.save(hotel);
	    return ResponseEntity.ok(updatedHotel);
	}

	@PutMapping("/update/{id}")
	public Hotel UpdateHotel(@RequestBody Hotel h , @PathVariable Long id) {
		Hotel v1=hotelRepository.findById(id).orElse(null);
		if(v1!=null) {
			h.setId(id);
			return hotelRepository.saveAndFlush(h);
		}else {
			throw new RuntimeException("Errer");
		}
	}
	  
	@RequestMapping(path = "/toggleStatus/{hotelId}", method = RequestMethod.PUT)
	public ResponseEntity<ResponseMessage> toggleStatus(@PathVariable Long hotelId) {
	    Optional<Hotel> optionalHotel = hotelRepository.findById(hotelId);

	    if (optionalHotel.isPresent()) {
	        Hotel hotel = optionalHotel.get();
	        Boolean currentStatus = hotel.getStatus();

	        if (currentStatus == null || !currentStatus) {
	            hotel.setStatus(true); // Set status to true
	        } else {
	            hotel.setStatus(false); // Set status to false
	        }

	        hotelRepository.save(hotel);

	        String message = "Status of Hotel with ID " + hotelId + " has been toggled.";
	        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
	    } else {
	        String message = "Hotel with ID " + hotelId + " does not exist.";
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(message));
	    }
	}



}
