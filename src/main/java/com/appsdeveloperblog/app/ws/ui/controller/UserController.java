package com.appsdeveloperblog.app.ws.ui.controller;


import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appsdeveloperblog.app.ws.service.AddressService;
import com.appsdeveloperblog.app.ws.service.UserService;
import com.appsdeveloperblog.app.ws.shared.dto.AddressDTO;
import com.appsdeveloperblog.app.ws.shared.dto.UserDto;
import com.appsdeveloperblog.app.ws.ui.model.request.UserDetailsRequestModel;
import com.appsdeveloperblog.app.ws.ui.model.response.AddressesRest;
import com.appsdeveloperblog.app.ws.ui.model.response.OperationStatusModel;
import com.appsdeveloperblog.app.ws.ui.model.response.RequestOperationName;
import com.appsdeveloperblog.app.ws.ui.model.response.RequestOperationStatus;
import com.appsdeveloperblog.app.ws.ui.model.response.UserRest;

@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	AddressService addressesService;
	
	@GetMapping(path="/{id}", produces = {MediaType.APPLICATION_XML_VALUE , MediaType.APPLICATION_JSON_VALUE})
	public UserRest getUser(@PathVariable String id) {
		UserRest returnValue = new UserRest();
		UserDto userDto = userService.getUserByUserId(id);
		BeanUtils.copyProperties(userDto, returnValue);
		return returnValue;
	}
	
	


	@PostMapping(consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }, produces = {
			MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {

		ModelMapper modelMapper = new ModelMapper();
		UserDto userDto = modelMapper.map(userDetails, UserDto.class);
		UserDto createdUser = userService.createUser(userDto);
		UserRest returnValue =modelMapper.map(createdUser,UserRest.class);
		
		return returnValue;
	}

	@PutMapping(path="/{id}", 
			consumes = {MediaType.APPLICATION_XML_VALUE , MediaType.APPLICATION_JSON_VALUE},
			produces = {MediaType.APPLICATION_XML_VALUE , MediaType.APPLICATION_JSON_VALUE}
	)
	public UserRest updateUser(@PathVariable String id , @RequestBody UserDetailsRequestModel userDetails) {

		UserRest returnValue = new UserRest();

		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userDetails, userDto);
		UserDto updatedUser = userService.updateUser(id,userDto);
		BeanUtils.copyProperties(updatedUser,returnValue);
		return returnValue;
	}

	@DeleteMapping(path="/{id}")
	public OperationStatusModel deleteUser(@PathVariable String id) {
		OperationStatusModel returnValue = new OperationStatusModel();
		returnValue.setOperationName(RequestOperationName.DELETE.name());
		userService.deleteUser(id);
		
		returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return returnValue;
	}
	
	@GetMapping(produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limit", defaultValue = "2") int limit) {
		List<UserRest> returnValue = new ArrayList<>();

		List<UserDto> users = userService.getUsers(page, limit);
		
		Type listType = new TypeToken<List<UserRest>>() {
		}.getType();
		returnValue = new ModelMapper().map(users, listType);

		/*for (UserDto userDto : users) {
			UserRest userModel = new UserRest();
			BeanUtils.copyProperties(userDto, userModel);
			returnValue.add(userModel);
		}*/

		return returnValue;
	}
	
	
	
	@GetMapping(path="/{id}/addresses", produces = {MediaType.APPLICATION_XML_VALUE , MediaType.APPLICATION_JSON_VALUE
			,"application/hal+json"})
	public Resources<AddressesRest> getUserAddresses(@PathVariable String id) {
		List<AddressesRest> addressRestListModel = new ArrayList<>();
		List<AddressDTO> addressesDTO= addressesService.getAddresses(id);
		ModelMapper modelMapper = new ModelMapper();
		if(addressesDTO != null && ! addressesDTO.isEmpty()) {
		Type listType = new TypeToken<List<AddressesRest>>() {}.getType();
		addressRestListModel = modelMapper.map(addressesDTO, listType);
		
		for(AddressesRest addressesRest: addressRestListModel) {
			Link addressLink = linkTo(methodOn(UserController.class).getUserAddresses(id, addressesRest.getAddressId())).withSelfRel();
			addressesRest.add(addressLink);
			Link userLink = linkTo(methodOn(UserController.class).getUser(id)).withRel("user");
			addressesRest.add(userLink);
		}
		}
		return new Resources<>(addressRestListModel);
	}
	
	
	@GetMapping(path="/{userId}/addresses/{addressId}", produces = {MediaType.APPLICATION_XML_VALUE ,
			MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
	public Resource<AddressesRest> getUserAddresses(@PathVariable String userId, @PathVariable String addressId) {
		AddressesRest returnValue = new AddressesRest();
		AddressDTO addressesDTO= addressesService.getAddress(addressId);
		ModelMapper modelMapper = new ModelMapper();
		//Link addressLink = linkTo(UserController.class).slash(userId).slash("addresses").slash(addressId).withSelfRel();
				
		Link addressLink = linkTo(methodOn(UserController.class).getUserAddresses(userId, addressId)).withSelfRel();
		Link userLink = linkTo(UserController.class).slash(userId).withRel("user");
		Link addressesLink = linkTo(methodOn(UserController.class).getUserAddresses(userId)).withRel("addresses");

		if(addressesDTO != null ) {
		
		returnValue = modelMapper.map(addressesDTO, AddressesRest.class);
		returnValue.add(addressLink);
		returnValue.add(userLink);
		returnValue.add(addressesLink);
		}
		return new Resource<>(returnValue);
	}

}
