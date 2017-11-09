/**
 *  Copyright 2017 The Regents is CapeStart at Nagercoil
 *
 */
package com.capestart.kernel.security.persistence;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.capestart.security.api.Organization;
import com.capestart.util.EqualsUtil;

/**
 * JPA-annotated organization object.
 */
@Entity
@Access(AccessType.FIELD)
@Table(name = "ch_organization")
@NamedQueries({
        @NamedQuery(name = "Organization.findAll", query = "Select o FROM JpaOrganization o"),
        @NamedQuery(name = "Organization.findById", query = "Select o FROM JpaOrganization o where o.id = :id"),
        @NamedQuery(name = "Organization.findByHost", query = "Select o FROM JpaOrganization o JOIN o.servers s where key(s) = :serverName AND value(s) = :port"),
        @NamedQuery(name = "Organization.getCount", query = "Select COUNT(o) FROM JpaOrganization o") })
public class JpaOrganization implements Organization {

  /** The organizational identifier */
  @Id
  @Column(name = "id", length = 128)
  protected String id;

  /** The friendly name of the organization */
  @Column(name = "name")
  protected String name;

  /** The local admin role name */
  @Column(name = "admin_role")
  protected String adminRole;
  
  @ElementCollection
  @MapKeyColumn(name = "name")
  @Column(name = "port")
  @CollectionTable(name = "ch_organization_node", joinColumns = @JoinColumn(name = "organization"))
  protected Map<String, Integer> servers;

  @ElementCollection
  @MapKeyColumn(name = "name")
  @Column(name = "value")
  @CollectionTable(name = "ch_organization_property", joinColumns = @JoinColumn(name = "organization"))
  protected Map<String, String> properties;

  /**
   * No-arg constructor needed by JPA
   */
  public JpaOrganization() {
  }

  /**
   * Constructs an organization with its attributes.
   *
   * @param orgId
   *          the unique identifier
   * @param name
   *          the friendly name
   * @param serverName
   *          the host name
   * @param serverPort
   *          the host port
   * @param adminRole
   *          name of the local admin role
   * @param properties
   *          arbitrary properties defined for this organization, which might include branding, etc.
   */
  public JpaOrganization(String orgId, String name, String serverName, Integer serverPort, String adminRole, Map<String, String> properties) {
    super();
    this.id = orgId;
    this.name = name;
    this.adminRole = adminRole;
    this.servers = new HashMap<String, Integer>();
    this.servers.put(serverName, serverPort);
    this.properties = properties;
  }

  /**
   * Constructs an organization with its attributes.
   *
   * @param orgId
   *          the unique identifier
   * @param name
   *          the friendly name
   * @param servers
   *          the servers
   * @param adminRole
   *          name of the local admin role
   * @param properties
   *          arbitrary properties defined for this organization, which might include branding, etc.
   */
  public JpaOrganization(String orgId, String name, Map<String, Integer> servers,String adminRole,  Map<String, String> properties) {
    super();
    this.id = orgId;
    this.name = name;
    this.servers = servers;
    this.adminRole = adminRole;
    this.properties = properties;
  }

  	/**
	 * @see com.capestartproject.security.api.Organization#getId()
	 */
  @Override
  public String getId() {
    return id;
  }

  /**
   * @see com.capestart.security.api.Organization#getName()
   */
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  	/**
	 * @see com.capestart.security.api.Organization#getProperties()
	 */
  public Map<String, String> getProperties() {
    return properties;
  }

  public void setProperties(Map<String, String> properties) {
    this.properties = properties;
  }

  	/**
	 * @see com.capestart.security.api.Organization#getServers()
	 */
  @Override
  public Map<String, Integer> getServers() {
    return servers;
  }

  /**
   * Replaces the existing servers.
   *
   * @param servers
   *          the servers
   */
  public void setServers(Map<String, Integer> servers) {
    this.servers = servers;
  }

  /**
   * Adds the server - port mapping.
   *
   * @param serverName
   *          the server name
   * @param port
   *          the port
   */
  public void addServer(String serverName, Integer port) {
    if (servers == null)
      servers = new HashMap<String, Integer>();
    servers.put(serverName, port);
  }

  /**
   * Removes the given server - port mapping.
   *
   * @param serverName
   *          the server name
   * @param port
   *          the port
   */
  public void remove(String serverName, Integer port) {
    if (port.equals(servers.get(serverName))) {
      servers.remove(serverName);
    }
  }

  /**
   * {@inheritDoc}
   *
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Organization))
      return false;
    return ((Organization) obj).getId().equals(id);
  }

  /**
   * {@inheritDoc}
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    return EqualsUtil.hash(id);
  }

  /**
   * {@inheritDoc}
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return id;
  }

  public void setAdminRole(String adminRole) {
	 this.adminRole = adminRole;
  }
  
	@Override
	public String getAdminRole() {
		return this.adminRole;
	}

}
