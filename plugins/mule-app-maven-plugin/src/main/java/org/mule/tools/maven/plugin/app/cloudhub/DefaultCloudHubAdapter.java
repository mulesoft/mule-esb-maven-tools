/**
 * Mule ESB Maven Tools
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.tools.maven.plugin.app.cloudhub;

import java.io.File;
import java.util.Map;

import com.mulesoft.ch.rest.model.Application;
import com.mulesoft.ch.rest.model.ApplicationStatusChange;
import com.mulesoft.ch.rest.model.ApplicationStatusChange.DesiredApplicationStatus;
import com.mulesoft.ch.rest.model.ApplicationUpdateInfo;
import com.mulesoft.cloudhub.client.CloudHubConnectionImpl;
import com.mulesoft.cloudhub.client.CloudHubDomainConnectionI;

public class DefaultCloudHubAdapter implements CloudHubAdapter {

    private CloudHubDomainConnectionI connectionDomain;

    public DefaultCloudHubAdapter() {
    }

    @Override
    public void create(String cloudHubUrl, String username, String password, String domain) {
        connectionDomain = new CloudHubConnectionImpl(cloudHubUrl,username,password,null, false).connectWithDomain(domain);
    }

    @Override
    public void deploy(File file, String muleVersion, int workers, long maxWaitTime, Map<String, String> properties) {
    	
    	Application app = getApplication(muleVersion, workers, properties);
    	
    	if (connectionDomain.isDomainAvailable(connectionDomain.getDomain())){
    		/* Domain is available, create the application */
    		connectionDomain.createApplication(app);
    	} else {
    		/* Domain is not available, the application already exists. Update the application. */
    		connectionDomain.updateApplication(new ApplicationUpdateInfo(app));
    	}
    	connectionDomain.deployApplication(file, maxWaitTime);
    	
    }

	@Override
    public void undeploy(long maxWaitTime) {
        connectionDomain.updateApplicationStatus(new ApplicationStatusChange(DesiredApplicationStatus.STOP), maxWaitTime);
    }
	
    private Application getApplication(String muleVersion, int workers, Map<String, String> properties) {
		Application app = new Application();
		
		app.setMuleVersion(muleVersion);
		app.setWorkers(workers);
		app.setProperties(properties);
		
		return app;
	}

}
