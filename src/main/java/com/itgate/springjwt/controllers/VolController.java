package com.itgate.springjwt.controllers;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
import com.itgate.springjwt.models.ResponseMessage;
import com.itgate.springjwt.models.Vehicule;
import com.itgate.springjwt.models.Vol;
import com.itgate.springjwt.repository.AdminRepository;
import com.itgate.springjwt.repository.VolRepository;
import com.itgate.springjwt.utils.StorageService;


@RestController
@RequestMapping("/Vols")
@CrossOrigin("*")

public class VolController {
	@Autowired
	VolRepository volRepository;
	@Autowired
	AdminRepository adminRepository;
	 @Autowired
		private StorageService service;
	    public final java.nio.file.Path rootLocation =  Paths.get("upload");
	@GetMapping(path = "/All")
	public List<Vol> getAll(){
		return volRepository.findAll();
	}
	@PostMapping(path = "/Add")
	public Vol CreatVol(@RequestBody Vol v) {
		return volRepository.save(v);
		
	}
	@GetMapping(path = "/findbyid/{id}")
	public Vol FindVolById(@PathVariable Long id) {
		return volRepository.findById(id).orElse(null);
	}
	@PutMapping("/update/{id}")
	public Vol UpdateVol(@RequestBody Vol v , @PathVariable Long id) {
		Vol v1=volRepository.findById(id).orElse(null);
		if(v1!=null) {
			v.setId(id);
			return volRepository.saveAndFlush(v);
		}else {
			throw new RuntimeException("Errer");
		}
	}
	@DeleteMapping("/Delet/{id}")
	public ResponseEntity DeletVehicule(@PathVariable Long id) {
		volRepository.deleteById(id);
		 return ResponseEntity.ok().build();
	}
	@RequestMapping(path = "/addvol/{admin_id}", method = RequestMethod.POST)
	public ResponseEntity<ResponseMessage> Savevol( @RequestBody Vol vol, @PathVariable Long admin_id) {
	    try {
	        Admin en = adminRepository.findById(admin_id).orElse(null);
	        vol.setAdmin(en);
	        volRepository.save(vol);
	        String message = "Vol saved successfully";
	        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
	    } catch (Exception e) {
	        String message = "Fail to save Vol";
	        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
	    }
	}
	

	

}
