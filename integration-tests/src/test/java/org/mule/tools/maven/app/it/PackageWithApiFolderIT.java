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

public class PackageWithApiFolderIT extends AbstractMavenIT {

    @Override
    protected String getArtifactVersion() {
        return "1.0-SNAPSHOT";
    }

    @Override
    protected String getArtifactId() {
        return "project-with-api";
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
        assertZipDoesContain(zipFile, "api/api.raml");
        assertZipDoesContain(zipFile, "api/examples/example.json");
        assertZipDoesContain(zipFile, "api/examples/more-examples/second-example.json");
        assertZipDoesContain(zipFile, "api/schemas/schemas.json");
    }
}

