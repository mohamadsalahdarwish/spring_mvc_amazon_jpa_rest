package com.appsdeveloperblog.app.ws.io.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "users")
public class UserEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private long id;

	@Column(nullable = false, length = 50)
	private long userId;
	@Column(nullable = false, length = 50)
	private long userName;
	@Column(nullable = false, length = 50)
	private long lastName;
	@Column(nullable = false, length = 120)
	private String email;
	@Column(nullable = false)
	private String encyptedPassword;
	private String emailVerificationToken;
	@Column(nullable = false, columnDefinition = "boolean default false")
	private Boolean emailVerificationStatus;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getUserName() {
		return userName;
	}

	public void setUserName(long userName) {
		this.userName = userName;
	}

	public long getLastName() {
		return lastName;
	}

	public void setLastName(long lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEncyptedPassword() {
		return encyptedPassword;
	}

	public void setEncyptedPassword(String encyptedPassword) {
		this.encyptedPassword = encyptedPassword;
	}

	public String getEmailVerificationToken() {
		return emailVerificationToken;
	}

	public void setEmailVerificationToken(String emailVerificationToken) {
		this.emailVerificationToken = emailVerificationToken;
	}

	public Boolean getEmailVerificationStatus() {
		return emailVerificationStatus;
	}

	public void setEmailVerificationStatus(Boolean emailVerificationStatus) {
		this.emailVerificationStatus = emailVerificationStatus;
	}
}
