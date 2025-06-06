package com.genc.project.services;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.genc.project.entities.User;

public class UserDetailsImpl implements UserDetails {

private User user;
	
	public UserDetailsImpl(User user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singleton(new SimpleGrantedAuthority(user.getRole().toString()));
	}
//once user loggedin the authenticate object is created  it will possess user details like name pass email which will used in differenet endpoints which require those details
	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}
	
	public String getEmail() {
		return user.getEmail();
	}
	
	public int getId() {
		return user.getId();
	}
}
