package com.ecommerce.project.controllers;

import com.ecommerce.project.model.User;
import com.ecommerce.project.payload.AddressDTO;
import com.ecommerce.project.service.AddressService;
import com.ecommerce.project.util.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/api")
@Tag(name="Address Controller", description = "This controller will " +
        "have all the endpoints for addresses.")//This is for making changes on Swagger UI
public class AddressController {
    @Autowired
    AddressService addressService;
    @Autowired
    AuthUtil authUtil;

    @PostMapping("/addresses")
    @Operation(summary = "Add an Address.")//For swagger UI
    public ResponseEntity<AddressDTO>addAddress(@Valid @RequestBody AddressDTO address)
    {       User user=authUtil.loggedInUser();
            AddressDTO savedAddressDTO=addressService.createAddress(address,user);
            return new ResponseEntity<>(savedAddressDTO, HttpStatus.CREATED);
    }

    @GetMapping("/addresses")
    @Operation(summary = "Get all the addresses.")
    public ResponseEntity<List<AddressDTO>>getAddresses()
    {
        List<AddressDTO>addressList=addressService.getAddresses();
        return new ResponseEntity<>(addressList, HttpStatus.OK);
    }
    @GetMapping("/addresses/{addressId}")
    @Operation(summary = "Get an address by AddressId.")
    public ResponseEntity<AddressDTO>getAddressesById(@PathVariable Long addressId)
    {
        AddressDTO addressDTO=addressService.getAddressById(addressId);
        return new ResponseEntity<>(addressDTO, HttpStatus.OK);
    }

    @GetMapping("/users/addresses")
    @Operation(summary = "Get all the addresses of a user.")
    public ResponseEntity<List<AddressDTO>>getUserAddresses()
    {   User user=authUtil.loggedInUser();
        List<AddressDTO>addressList=addressService.getUserAddresses(user);
        return new ResponseEntity<>(addressList, HttpStatus.OK);
    }

    @PutMapping("/addresses/{addressId}")
    @Operation(summary = "Update an Address by Address ID.")
    public ResponseEntity<AddressDTO>updateAddressById(@PathVariable Long addressId,
                                                        @RequestBody AddressDTO addressDTO)
    {
        AddressDTO updatedAddress=addressService.updateAddressById(addressId,addressDTO);
        return new ResponseEntity<>(updatedAddress, HttpStatus.OK);
    }

    @DeleteMapping("/addresses/{addressId}")
    @Operation(summary = "Delete an Address by Address ID.")
    public ResponseEntity<String>deleteAddressById(@PathVariable Long addressId)
    {
        String status=addressService.deleteAddressById(addressId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }
}
