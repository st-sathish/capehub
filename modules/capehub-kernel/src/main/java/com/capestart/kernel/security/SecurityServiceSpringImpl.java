package com.capestart.kernel.security;

import com.capestart.security.api.Organization;
import com.capestart.security.api.SecurityService;
import com.capestart.security.api.User;

public class SecurityServiceSpringImpl implements SecurityService {

	@Override
	public User getUser() throws IllegalStateException {
		return null;
	}

	@Override
	public Organization getOrganization() {
		return null;
	}

	@Override
	public void setOrganization(Organization organization) {
		
	}

	@Override
	public void setUser(User user) {
		
	}

}