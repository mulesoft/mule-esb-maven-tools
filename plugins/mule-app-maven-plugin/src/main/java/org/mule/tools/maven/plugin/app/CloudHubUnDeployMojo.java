/**
 * Mule ESB Maven Tools
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.tools.maven.plugin.app;

import org.apache.maven.plugin.MojoExecutionException;
import org.mule.tools.maven.plugin.app.cloudhub.CloudHubAdapter;

/**
 * @goal cloudhub-undeploy
 */
public class CloudHubUnDeployMojo extends AbstractCloudHubMojo {

    /**
     * @parameter expression="${ion.maxWaitTime}" default-value="120000"
     */
    protected long maxWaitTime;

    @Override
    public void execute() throws MojoExecutionException {
        getLog().info("Undeploying");

        final CloudHubAdapter domainConnection = createDomainConnection();
        domainConnection.undeploy(this.maxWaitTime);
    }

}
