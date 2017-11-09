package com.capestart.kernel.security;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.osgi.service.cm.ConfigurationException;

import com.capestart.kernel.security.persistence.OrganizationDatabase;
import com.capestart.kernel.security.persistence.OrganizationDatabaseException;
import com.capestart.security.api.Organization;
import com.capestart.util.NotFoundException;

/**
 * Test class for {@link OrganizationDirectoryServiceImpl}
 */
public class OrganizationDirectoryServiceTest {
	
	private OrganizationDirectoryServiceImpl orgDirectoryService;
	
	@Before
	public void setUp() {
		OrganizationDatabase organizationDatabase = new OrganizationDatabase() {

			private int i = 0;
		    private Organization organization;

			@Override
			public List<Organization> getOrganizations() throws OrganizationDatabaseException {
				return null;
			}

			@Override
			public int countOrganizations() throws OrganizationDatabaseException {
				return 0;
			}

			@Override
			public Organization getOrganization(String orgId) throws OrganizationDatabaseException, NotFoundException {
				if (organization == null)
			         throw new NotFoundException();
			    return organization;
			}

			@Override
			public Organization getOrganizationByHost(String host, int port)
					throws OrganizationDatabaseException, NotFoundException {
				return null;
			}

			@Override
			public void deleteOrganization(String orgId) throws OrganizationDatabaseException, NotFoundException {
				
			}

			@Override
			public void storeOrganization(Organization organization) throws OrganizationDatabaseException {
				this.organization = organization;
		        if (i == 0) {
		          Assert.assertNotNull(organization);
		          Assert.assertEquals("ch_default", organization.getId());
		          Assert.assertEquals("Capehub Test", organization.getName());
		          Assert.assertEquals("ROLE_TEST_ADMIN", organization.getAdminRole());
		          Map<String, Integer> servers = organization.getServers();
		          Assert.assertEquals(1, servers.size());
		          Assert.assertTrue(servers.containsKey("localhost"));
		          Assert.assertTrue(servers.containsValue(8080));
		          Assert.assertEquals("true", organization.getProperties().get("org.test"));
		        } else if (i == 1) {
		          Assert.assertNotNull(organization);
		          Assert.assertEquals("ch_default", organization.getId());
		          Assert.assertEquals("Capehub Test 2", organization.getName());
		          Assert.assertEquals("ROLE_TEST2_ADMIN", organization.getAdminRole());
		          Map<String, Integer> servers = organization.getServers();
		          Assert.assertEquals(2, servers.size());
		          Assert.assertTrue(servers.containsKey("localhost"));
		          Assert.assertTrue(servers.containsValue(8080));
		          Assert.assertTrue(servers.containsKey("localhost2"));
		          Assert.assertTrue(servers.containsValue(8081));
		          Assert.assertEquals("false", organization.getProperties().get("org.test"));
		        } else {
		          Assert.fail("Too much storeOrganization calls: " + i);
		        }
		        i++;
				
			}

			@Override
			public boolean containsOrganization(String orgId) throws OrganizationDatabaseException {
				return false;
			}
			
		};
		orgDirectoryService = new OrganizationDirectoryServiceImpl();
	    orgDirectoryService.setOrgPersistence(organizationDatabase);
	}
	
	@Test
	public void testUpdateMethod() {
		Hashtable<String, String> properties = new Hashtable<String, String>();
		
		// Test wrong configuration
		try {
			orgDirectoryService.updated("ch_default", properties);
		    Assert.fail("No configuration exception occured");
		} catch(ConfigurationException e) {
			//should be an Exception
		}
		
		// Add properties
	    properties.put(OrganizationDirectoryServiceImpl.ORG_ID_KEY, "ch_default");
	    properties.put(OrganizationDirectoryServiceImpl.ORG_NAME_KEY, "Capehub Test");
	    properties.put(OrganizationDirectoryServiceImpl.ORG_SERVER_KEY, "localhost");
	    properties.put(OrganizationDirectoryServiceImpl.ORG_PORT_KEY, "8080");
	    properties.put(OrganizationDirectoryServiceImpl.ORG_ADMIN_ROLE_KEY, "ROLE_TEST_ADMIN");
	    properties.put("prop.org.test", "true");
	    
	    // Test storing
	    try {
	    	orgDirectoryService.updated("ch_default", properties);
	    }catch(ConfigurationException e) {
	    	Assert.fail("Configuration exception occured");
	    }
	    
	    // Update properties
	    properties.put(OrganizationDirectoryServiceImpl.ORG_ID_KEY, "ch_default");
	    properties.put(OrganizationDirectoryServiceImpl.ORG_NAME_KEY, "Capehub Test 2");
	    properties.put(OrganizationDirectoryServiceImpl.ORG_SERVER_KEY, "localhost2");
	    properties.put(OrganizationDirectoryServiceImpl.ORG_PORT_KEY, "8081");
	    properties.put(OrganizationDirectoryServiceImpl.ORG_ADMIN_ROLE_KEY, "ROLE_TEST2_ADMIN");
	    properties.put("prop.org.test", "false");
	    
	    // Test adding second server
	    try {
	    	orgDirectoryService.updated("ch_default", properties);
	    }catch(ConfigurationException e) {
	    	Assert.fail("Configuration exception occured");
	    }
	}
}
