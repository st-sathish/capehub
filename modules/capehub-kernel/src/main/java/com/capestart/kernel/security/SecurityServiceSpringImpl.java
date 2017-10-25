package com.capestart.kernel.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.capestart.security.api.JaxbOrganization;
import com.capestart.security.api.JaxbRole;
import com.capestart.security.api.JaxbUser;
import com.capestart.security.api.Organization;
import com.capestart.security.api.SecurityService;
import com.capestart.security.api.User;

public class SecurityServiceSpringImpl implements SecurityService {

	/** Holds delegates users for new threads that have been spawned from authenticated threads */
	private static final ThreadLocal<User> delegatedUserHolder = new ThreadLocal<User>();

	/** Holds organization responsible for the current thread */
	private static final ThreadLocal<Organization> organization = new ThreadLocal<Organization>();
	
	@Override
	public User getUser() throws IllegalStateException {
		Organization org = getOrganization();
	    if (org == null)
	      throw new IllegalStateException("No organization is set in security context");

	    User delegatedUser = delegatedUserHolder.get();
	    if (delegatedUser != null) {
	      return delegatedUser;
	    }
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    JaxbOrganization jaxbOrganization = JaxbOrganization.fromOrganization(org);
	    if (auth == null) {
	    	throw new IllegalStateException("Authentication Exception in security context");
	    } else {
	      Object principal = auth.getPrincipal();
	      if (principal == null) {
	    	  throw new IllegalStateException("No user found in security context");
	      }
	      if (principal instanceof UserDetails) {
	        UserDetails userDetails = (UserDetails) principal;

	        Set<JaxbRole> roles = new HashSet<JaxbRole>();
	        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
	        if (authorities != null && authorities.size() > 0) {
	          for (GrantedAuthority ga : authorities) {
	            roles.add(new JaxbRole(ga.getAuthority(), jaxbOrganization));
	          }
	        }
	        return new JaxbUser(userDetails.getUsername(), jaxbOrganization, roles);
	      } else {
	    	  throw new IllegalStateException("No user found in security context");
	      }
	    }
	}

	@Override
	public Organization getOrganization() {
		return SecurityServiceSpringImpl.organization.get();
	}

	@Override
	public void setOrganization(Organization organization) {
		SecurityServiceSpringImpl.organization.set(organization);
	}

	@Override
	public void setUser(User user) {
		delegatedUserHolder.set(user);
	}

}