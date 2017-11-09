/**
 *  Copyright 2017 The Regents is CapeStart at Nagercoil
 *
 */
package com.capestart.kernel.security;

/**
 * An MxBean that exposes the number of open http connections to a JXM agent
 */
public interface HttpConnectionMXBean {
  /** Gets the number of open http connections */
  int getOpenConnections();
}
