package com.capestart.kernel.security.persistence;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.easymock.EasyMock;
import org.eclipse.persistence.jpa.PersistenceProvider;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.capestart.security.api.DefaultOrganization;
import com.capestart.security.api.JaxbRole;
import com.capestart.security.api.JaxbUser;
import com.capestart.security.api.Organization;
import com.capestart.security.api.SecurityConstants;
import com.capestart.security.api.SecurityService;
import com.capestart.security.api.User;
import com.capestart.util.NotFoundException;
import com.capestart.util.PathSupport;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

/**
 * Tests organization persistence: storing, merging, retrieving and removing.
 * @author CS39
 *
 */
public class OrganizationPersistenceTest {

	private ComboPooledDataSource pooledDataSource;
	private OrganizationDatabaseImpl organizationDatabase;
	private String storage;
	private SecurityService securityService;
	
	@Before
	public void setUp() throws Exception {
		long currentTime = System.currentTimeMillis();
	    storage = PathSupport.concat("target", "db" + currentTime + ".h2.db");

	    pooledDataSource = new ComboPooledDataSource();
	    pooledDataSource.setDriverClass("org.h2.Driver");
	    pooledDataSource.setJdbcUrl("jdbc:h2:./target/db" + currentTime);
	    pooledDataSource.setUser("capehub");
	    pooledDataSource.setPassword("capehub");

	    // Collect the persistence properties
	    Map<String, Object> props = new HashMap<String, Object>();
	    props.put("javax.persistence.nonJtaDataSource", pooledDataSource);
	    props.put("eclipselink.ddl-generation", "create-tables");
	    props.put("eclipselink.ddl-generation.output-mode", "database");

	    securityService = EasyMock.createNiceMock(SecurityService.class);
	    User user = new JaxbUser("admin", new DefaultOrganization(), new JaxbRole(SecurityConstants.GLOBAL_ADMIN_ROLE,
	            new DefaultOrganization()));
	    EasyMock.expect(securityService.getOrganization()).andReturn(new DefaultOrganization()).anyTimes();
	    EasyMock.expect(securityService.getUser()).andReturn(user).anyTimes();
	    EasyMock.replay(securityService);

	    organizationDatabase = new OrganizationDatabaseImpl();
	    organizationDatabase.setPersistenceProvider(new PersistenceProvider());
	    organizationDatabase.setPersistenceProperties(props);
	    organizationDatabase.setSecurityService(securityService);
	    organizationDatabase.activate(null);
	}
	
	@Test
	public void testAdding() throws Exception {
		Map<String, String> orgProperties = new HashMap<String, String>();
	    orgProperties.put("test", "one");
	    JpaOrganization org = new JpaOrganization("newOrg", "test organization", "test.org", 8080, "ROLE_TEST_ADMIN", orgProperties);

	    organizationDatabase.storeOrganization(org);

	    Assert.assertTrue(organizationDatabase.containsOrganization("newOrg"));

	    Organization orgById = organizationDatabase.getOrganization("newOrg");
	    Organization orgByHost = organizationDatabase.getOrganizationByHost("test.org", 8080);
	    Assert.assertEquals(orgById, orgByHost);

	    Assert.assertEquals("newOrg", orgById.getId());
	    Assert.assertEquals("test organization", orgById.getName());
	    Assert.assertEquals("ROLE_TEST_ADMIN", orgById.getAdminRole());
	    Map<String, Integer> servers = orgById.getServers();
	    Assert.assertEquals(1, servers.size());
	    Assert.assertTrue(servers.containsKey("test.org"));
	    Assert.assertTrue(servers.containsValue(8080));
	    Map<String, String> properties = orgById.getProperties();
	    Assert.assertEquals(1, properties.size());
	    Assert.assertTrue(properties.containsKey("test"));
	    Assert.assertTrue(properties.containsValue("one"));
	}
	
	@Test
	public void testList() throws Exception {
		Map<String, String> orgProperties = new HashMap<String, String>();
	    orgProperties.put("test", "one");
	    JpaOrganization org1 = new JpaOrganization("newOrg", "test organization", "test.org", 8080, "ROLE_TEST_ADMIN", orgProperties);

	    organizationDatabase.storeOrganization(org1);

	    orgProperties.put("test", "one");
	    orgProperties.put("test2", "two");
	    JpaOrganization org2 = new JpaOrganization("newOrg2", "test organization 2", "test2.org", 8081, "ROLE_TEST2_ADMIN",orgProperties);

	    organizationDatabase.storeOrganization(org2);

	    Assert.assertEquals(2, organizationDatabase.countOrganizations());

	    List<Organization> organizations = organizationDatabase.getOrganizations();
	    Assert.assertEquals(2, organizations.size());

	    Assert.assertEquals(org1, organizations.get(0));
	    Assert.assertEquals(org2, organizations.get(1));
	}
	
	@Test
	public void testDeleting() throws Exception {
		Map<String, String> orgProperties = new HashMap<String, String>();
	    orgProperties.put("test", "one");
	    JpaOrganization org = new JpaOrganization("newOrg", "test organization", "test.org", 8080, "ROLE_TEST_ADMIN", orgProperties);

	    organizationDatabase.storeOrganization(org);

	    Assert.assertTrue(organizationDatabase.containsOrganization("newOrg"));

	    try {
	      organizationDatabase.getOrganization("newOrg");
	    } catch (NotFoundException e) {
	      Assert.fail("Organization not found");
	    }

	    organizationDatabase.deleteOrganization("newOrg");

	    Assert.assertFalse(organizationDatabase.containsOrganization("newOrg"));

	    try {
	      organizationDatabase.getOrganization("newOrg");
	      Assert.fail("Organization found");
	    } catch (NotFoundException e) {
	      Assert.assertNotNull(e);
	    }
	}
	
	@After
	public void tearDown() throws Exception {
		organizationDatabase.deactivate(null);
	    DataSources.destroy(pooledDataSource);
	    FileUtils.deleteQuietly(new File(storage));
	}
}
