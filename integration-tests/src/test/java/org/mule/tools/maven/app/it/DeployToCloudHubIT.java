/**
 * Mule ESB Maven Tools
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.tools.maven.app.it;

import org.junit.Ignore;

import java.io.File;

import static junit.framework.Assert.assertFalse;

@Ignore("Use only to check that CloudHub deploy works. Not for CI.")
public class DeployToCloudHubIT extends AbstractMavenIT {

    @Override
    protected String getArtifactVersion() {
        return "1.0";
    }

    @Override
    protected String getArtifactId() {
        return "deploy-to-cloudhub";
    }

    @Override
    protected String getGroupId() {
        return "org.mule.maven.tools.it";
    }

    @Override
    protected File getRoot() {
        return new File("target/integration-tests/" + getArtifactId());
    }

    @Override
    protected void verify() throws Exception {
        String classesFolder = String.format("target/integration-tests/%1s/target/classes", getArtifactId());
        assertFalse((new File(classesFolder)).getAbsoluteFile().exists());
    }
}

