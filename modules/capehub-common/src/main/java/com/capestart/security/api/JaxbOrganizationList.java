package com.capestart.security.api;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * A JAXB-annotated list of organizations.
 * @author CS39
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "organizations", namespace = "http://com.capestart.security")
@XmlRootElement(name = "organizations", namespace = "http://com.capestart.security")
public class JaxbOrganizationList {

	/** The list of organizations */
	@XmlElement(name = "organization")
	protected List<JaxbOrganization> organizations = new ArrayList<JaxbOrganization>();
	
	/**
	 * No arg constructor needed by JAXB
	 */
	public JaxbOrganizationList() {
		
	}
	
	/**
	 * Constructs a new OrganizationList wrapper from a list of organizations.
	 * @param organizations
	 * 		the list or organization
	 */
	public JaxbOrganizationList(List<JaxbOrganization> organizations) {
		this.organizations = organizations;
	}
	
	/**
	 * 
	 * @return the organizations
	 */
	public List<JaxbOrganization> getOrganizations() {
		return organizations;
	}
	
	/**
	 * the organizations to set
	 * @param organizations
	 */
	public void setOrganizations(List<JaxbOrganization> organizations) {
		this.organizations = organizations;
	}
	
	public void add(Organization org) {
		if (org instanceof JaxbOrganization) {
			organizations.add((JaxbOrganization) org);
		} else {
			organizations.add(JaxbOrganization.fromOrganization(org));
		}
	}
}
