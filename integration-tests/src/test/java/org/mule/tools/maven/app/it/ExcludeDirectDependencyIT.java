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

import java.io.File;

import static junit.framework.Assert.assertTrue;

public class ExcludeDirectDependencyIT extends AbstractMavenIT {

    @Override
    protected String getArtifactVersion() {
        return "1.0";
    }

    @Override
    protected String getArtifactId() {
        return "exclude-direct-dependency";
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
        File zipFile = zipFileFromBuildingProject();

        String muleCoreLib = "lib/mule-core-2.2.1.jar";
        String beanutilsLib = "lib/commons-beanutils-1.7.0-osgi.jar"; // this is a transitive dependency of mule-core
        assertZipDoesNotContain(zipFile, muleCoreLib, beanutilsLib);
    }
}
