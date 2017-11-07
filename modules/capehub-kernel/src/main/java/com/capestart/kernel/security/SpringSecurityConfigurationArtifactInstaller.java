package com.capestart.kernel.security;

import java.io.File;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.commons.io.FilenameUtils;
import org.apache.felix.fileinstall.ArtifactInstaller;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.osgi.context.support.OsgiBundleXmlApplicationContext;

import com.capestart.rest.RestConstants;
import com.capestart.security.api.SecurityService;

public class SpringSecurityConfigurationArtifactInstaller implements ArtifactInstaller {

	  protected static final Logger logger = LoggerFactory.getLogger(SpringSecurityConfigurationArtifactInstaller.class);

	  /** This component's bundle context */
	  protected BundleContext bundleContext = null;

	  /** The security filter */
	  protected SecurityFilter securityFilter = null;

	  /** The security filter's service registration */
	  protected ServiceRegistration filterRegistration = null;

	  /** The security service */
	  protected SecurityService securityService = null;

	  /** Spring application contexts */
	  protected Map<String, OsgiBundleXmlApplicationContext> appContexts = null;

	/**
	 * OSGI activate Callback
	 * @param cc
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void activate(ComponentContext cc) {
		logger.info("activate() is called");
		this.bundleContext = cc.getBundleContext();
	    this.appContexts = new HashMap<String, OsgiBundleXmlApplicationContext>();

	    // Register the security filter
	    securityFilter = new SecurityFilter(securityService);
	    Dictionary props = new Hashtable<String, Boolean>();
	    props.put("contextId", RestConstants.HTTP_CONTEXT_ID);
	    props.put("pattern", ".*");
	    props.put("service.ranking", "2");
	    filterRegistration = bundleContext.registerService(Filter.class.getName(), securityFilter, props);
	}
	
	/**
	 * OSGI deactivate callback
	 */
	protected void deactivate() {
		
	}

	@Override
	public boolean canHandle(File artifact) {
		return "security".equals(artifact.getParentFile().getName()) && artifact.getName().endsWith(".xml");
	}

	@Override
	public void install(File artifact) throws Exception {
		// If we already have a registration for this ID, take it out of the security filter and close it
	    String orgId = FilenameUtils.getBaseName(artifact.getName());
	    OsgiBundleXmlApplicationContext orgAppContext = appContexts.get(orgId);
	    if (orgAppContext != null) {
	      securityFilter.removeFilter(orgId);
	      orgAppContext.close();
	    }

	    OsgiBundleXmlApplicationContext springContext = new OsgiBundleXmlApplicationContext(new String[] { "file:"
	            + artifact.getAbsolutePath() });
	    springContext.setBundleContext(bundleContext);
	    logger.info("registered {} for {}", springContext, orgId);

	    // Refresh the spring application context
	    try {
	      springContext.refresh();
	    } catch (Exception e) {
	      logger.error("Unable to refresh spring security configuration file {}: {}", artifact, e);
	      return;
	    }

	    // Keep track of the app context so we can close it later
	    appContexts.put(orgId, springContext);

	    // Add the filter chain for this org to the security filter
	    securityFilter.addFilter(orgId, (Filter) springContext.getBean("springSecurityFilterChain"));
	}

	@Override
	public void uninstall(File artifact) throws Exception {
		String orgId = FilenameUtils.getBaseName(artifact.getName());
	    OsgiBundleXmlApplicationContext appContext = appContexts.get(orgId);
	    if (appContext != null) {
	      securityFilter.removeFilter(orgId);
	      appContexts.remove(orgId);
	      appContext.close();
	    }
	}

	@Override
	public void update(File artifact) throws Exception {
		install(artifact);
	}

	/**
	 * Sets's the security service
	 * @param securityService
	 * 		the security service to set
	 */
	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
	}
}
