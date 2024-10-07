package com.ecommerce.project.controllers;

import com.ecommerce.project.model.User;
import com.ecommerce.project.payload.AddressDTO;
import com.ecommerce.project.service.AddressService;
import com.ecommerce.project.util.AuthUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/api")
public class AddressController {
    @Autowired
    AddressService addressService;
    @Autowired
    AuthUtil authUtil;

    @PostMapping("/addresses")
    public ResponseEntity<AddressDTO>addAddress(@Valid @RequestBody AddressDTO address)
    {       User user=authUtil.loggedInUser();
            AddressDTO savedAddressDTO=addressService.createAddress(address,user);
            return new ResponseEntity<>(savedAddressDTO, HttpStatus.CREATED);
    }

    @GetMapping("/addresses")
    public ResponseEntity<List<AddressDTO>>getAddresses()
    {
        List<AddressDTO>addressList=addressService.getAddresses();
        return new ResponseEntity<>(addressList, HttpStatus.OK);
    }
    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO>getAddressesById(@PathVariable Long addressId)
    {
        AddressDTO addressDTO=addressService.getAddressById(addressId);
        return new ResponseEntity<>(addressDTO, HttpStatus.OK);
    }

    @GetMapping("/users/addresses")
    public ResponseEntity<List<AddressDTO>>getUserAddresses()
    {   User user=authUtil.loggedInUser();
        List<AddressDTO>addressList=addressService.getUserAddresses(user);
        return new ResponseEntity<>(addressList, HttpStatus.OK);
    }

    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO>updateAddressById(@PathVariable Long addressId,
                                                        @RequestBody AddressDTO addressDTO)
    {
        AddressDTO updatedAddress=addressService.updateAddressById(addressId,addressDTO);
        return new ResponseEntity<>(updatedAddress, HttpStatus.OK);
    }

    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<String>deleteAddressById(@PathVariable Long addressId)
    {
        String status=addressService.deleteAddressById(addressId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }
}
