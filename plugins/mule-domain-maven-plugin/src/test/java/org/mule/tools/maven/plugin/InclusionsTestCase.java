/**
 * Mule ESB Maven Tools
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.tools.maven.plugin;

import java.io.File;

import org.junit.Ignore;

@Ignore
public class InclusionsTestCase extends AbstractMuleMavenPluginTestCase
{
    private static final String HTTP_TRANSPORT_JAR = "lib/mule-transport-http-2.2.1.jar";
    private static final String HTTPCLIENT_JAR = "lib/commons-httpclient-3.1-osgi.jar";
    private static final String SPRING_CONFIG_JAR = "lib/mule-module-spring-config-2.2.1.jar";
    private static final String SSL_TRANSPORT_JAR = "lib/mule-transport-ssl-2.2.1.jar";
    private static final String TCP_TRANSPORT_JAR = "lib/mule-transport-tcp-2.2.1.jar";

    public void testIncludeDirectDependency() throws Exception
    {
        File zipFile = zipFileFromBuildingProject("include-direct-dependency");
        assertZipContains(zipFile, HTTP_TRANSPORT_JAR);
        assertZipContains(zipFile, SSL_TRANSPORT_JAR);
        assertZipContains(zipFile, TCP_TRANSPORT_JAR);
        assertZipContains(zipFile, HTTPCLIENT_JAR);
    }

    public void _testIncludeLeafDependency() throws Exception
    {
        File zipFile = zipFileFromBuildingProject("include-leaf-dependency");
        assertZipContains(zipFile, HTTPCLIENT_JAR);
    }

    public void _testIncludeTransitiveDependency() throws Exception
    {
        File zipFile = zipFileFromBuildingProject("include-transitive-dependency");
        assertZipContains(zipFile, SPRING_CONFIG_JAR);
    }
}
