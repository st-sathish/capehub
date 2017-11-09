/**
 *  Copyright 2017 The Regents is Capestart at Nagercoil
 *
 */
package com.capestart.kernel.rest;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.capestart.rest.RestConstants;

/**
 * Goes through each request and sets its max inactive time to a default value if they are a normal request or
 * invalidates the session if they are a security request. Without this filter you will see HashSession object contain
 * more and more objects and running the garbage collector will not clear them out until the server runs out of memory.
 * This will not be obvious on a test server unless it is under heavy load for a long period of time.
 * @author CS39
 *
 */
public class CleanSessionsFilter implements Filter {

	/** The logger */
	private static final Logger logger = LoggerFactory.getLogger(CleanSessionsFilter.class);

	/** Maximum inactive request time */
	private static final int NO_MAX_INACTIVE_INTERVAL_SET = -1;

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// Cast the request and response to HTTP versions
	    HttpServletRequest httpRequest = (HttpServletRequest) request;
	    if (httpRequest != null && httpRequest.getSession() != null) {
	      if (httpRequest.getSession().getMaxInactiveInterval() == NO_MAX_INACTIVE_INTERVAL_SET) {
	        // There is no maxInactiveInterval set so we need to set one.
	        logger.trace("Setting maxInactiveInterval to " + RestConstants.MAX_INACTIVE_INTERVAL + " on request @" + httpRequest.getRequestURL());
	        httpRequest.getSession().setMaxInactiveInterval(RestConstants.MAX_INACTIVE_INTERVAL);
	      }
	    }
	    chain.doFilter(request, response);
	    
	    // This has to be run after the chain.doFilter to invalidate the sessions after Spring Security has run as it creates new sessions.
	    if (request != null && HttpServletRequest.DIGEST_AUTH.equals(httpRequest.getAuthType())) {
	      logger.trace("Invalidating digest request.");
	      httpRequest.getSession().invalidate();
	    }
	    else if (httpRequest.getHeader("Authorization") != null) {
	      logger.trace("Invalidating digest request.");
	      httpRequest.getSession().invalidate();
	    }
	}

	@Override
	public void destroy() {
		
	}
}
