package com.esprit.dto.migration;

public class UserMigration {

	private String userId;

	private String firstName;

	private String lastName;

	private String email;

	private String role;

	private String username;

	private String password;

	private String pole;

	public String getUserId() {
		return userId;
	}

	public UserMigration userId(String userId) {
		this.userId = userId;
		return this;
	}

	public String getFirstName() {
		return firstName;
	}

	public UserMigration firstName(String firstName) {
		this.firstName = firstName;
		return this;
	}

	public String getLastName() {
		return lastName;
	}

	public UserMigration lastName(String lastName) {
		this.lastName = lastName;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public UserMigration email(String email) {
		this.email = email;
		return this;
	}

	public String getRole() {
		return role;
	}

	public UserMigration role(String role) {
		this.role = role;
		return this;
	}

	public String getUsername() {
		return username;
	}

	public UserMigration username(String username) {
		this.username = username;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public UserMigration password(String password) {
		this.password = password;
		return this;
	}

	public String getPole() {
		return pole;
	}

	public UserMigration pole(String pole) {
		this.pole = pole;
		return this;
	}

}