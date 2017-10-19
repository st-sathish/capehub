package com.capestart.kernel.security;

import java.io.File;

import org.apache.felix.fileinstall.ArtifactInstaller;

public class SpringSecurityConfigurationArtifactInstaller implements ArtifactInstaller {

	@Override
	public boolean canHandle(File arg0) {
		return false;
	}

	@Override
	public void install(File arg0) throws Exception {
		
	}

	@Override
	public void uninstall(File arg0) throws Exception {
		
	}

	@Override
	public void update(File arg0) throws Exception {
		
	}

}
