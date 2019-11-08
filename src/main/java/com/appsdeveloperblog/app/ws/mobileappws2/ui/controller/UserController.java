package com.appsdeveloperblog.app.ws.mobileappws2.ui.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.appsdeveloperblog.app.ws.ui.model.request.UserDetailsRequestModel;
import com.appsdeveloperblog.app.ws.ui.model.response.UserRest;

@RestController
@RequestMapping("/users")
public class UserController {
	
	
	@GetMapping
	public String getUser() {
		return "return get user";
	}

	@PostMapping
	public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails)  {
		return null;
	}

	@PutMapping
	public String updateUser() {

		return "return update user";
	}

	@DeleteMapping
	public String deleteUser() {
	
		return "return delete user";
	}

}
