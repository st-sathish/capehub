
package com.capestart.security.api;

import java.util.Map;

public interface Organization {

  /**
   * @return the id
   */
  String getId();

  /**
   * @return the name
   */
  String getName();

  /**
   * Returns the name for the local admin role.
   *
   * @return the admin role name
   */
  String getAdminRole();
  
  /**
   * Returns the organizational properties
   *
   * @return the properties
   */
  Map<String, String> getProperties();

  /**
   * Returns the server names and the corresponding ports that have been registered with this organization.
   *
   * @return the servers
   */
  Map<String, Integer> getServers();

}
