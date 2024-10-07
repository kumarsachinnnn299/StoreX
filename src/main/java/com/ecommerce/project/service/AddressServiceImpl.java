package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Address;
import com.ecommerce.project.model.User;
import com.ecommerce.project.payload.AddressDTO;
import com.ecommerce.project.repositories.AddressRepository;
import com.ecommerce.project.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService{
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    AddressRepository addressRepository;

    @Autowired
    UserRepository userRepository;
    @Override
    public AddressDTO createAddress(AddressDTO addressDTO, User user) {
        Address address=modelMapper.map(addressDTO,Address.class);

        List<Address>addressList=user.getAddresses();
        addressList.add(address);
        user.setAddresses(addressList);
        address.setUser(user);
        Address savedAddress=addressRepository.save(address);

        return  modelMapper.map(savedAddress,AddressDTO.class);

    }

    @Override
    public List<AddressDTO> getAddresses() {
        List<Address>addresses=addressRepository.findAll();
        List<AddressDTO>addressDTOList=addresses.stream()
                .map(address -> modelMapper.map(address,AddressDTO.class)).toList();
        return  addressDTOList;
    }

    @Override
    public AddressDTO getAddressById(Long addressId) {
        Address address=addressRepository.findById(addressId)
                .orElseThrow(()-> new ResourceNotFoundException("Address","addressId",addressId));
        return modelMapper.map(address,AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getUserAddresses(User user) {
        List<Address>addresses=user.getAddresses();
        List<AddressDTO>addressDTOList=addresses.stream()
                .map(address -> modelMapper.map(address,AddressDTO.class)).toList();
        return  addressDTOList;
    }

    @Override
    public AddressDTO updateAddressById(Long addressId,AddressDTO addressDTO) {
        Address addressFromDatabase=addressRepository.findById(addressId)
                .orElseThrow(()->new ResourceNotFoundException("Address","addressId",addressId));

        addressFromDatabase.setCountry(addressDTO.getCountry());
        addressFromDatabase.setState(addressDTO.getState());
        addressFromDatabase.setStreet(addressDTO.getStreet());
        addressFromDatabase.setBuildingName(addressDTO.getBuildingName());
        addressFromDatabase.setPinCode(addressDTO.getPinCode());
        addressFromDatabase.setCityName(addressDTO.getCityName());

        Address updatedAddress=addressRepository.save(addressFromDatabase);

        //Updating this address in the user
        User user=addressFromDatabase.getUser();
        user.getAddresses().removeIf(address -> address.getAddressId().equals(addressId));
        user.getAddresses().add(updatedAddress);

        userRepository.save(user);
        return  modelMapper.map(updatedAddress,AddressDTO.class);
    }

    @Override
    public String deleteAddressById(Long addressId) {

        //Delete from Address table
        Address addressFromDatabase=addressRepository.findById(addressId)
                .orElseThrow(()->new ResourceNotFoundException("Address","addressId",addressId));



        //Deleting from user's address
        User user=addressFromDatabase.getUser();
        user.getAddresses().removeIf(address -> address.getAddressId().equals(addressId));
        userRepository.save(user);

        addressRepository.delete(addressFromDatabase);

        return "Address with addressId "+addressId+" has been deleted successfully!!";
    }
}
