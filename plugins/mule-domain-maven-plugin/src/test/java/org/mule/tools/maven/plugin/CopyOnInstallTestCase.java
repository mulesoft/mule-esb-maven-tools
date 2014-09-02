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
import java.util.Properties;

import org.apache.maven.shared.invoker.InvocationResult;
import org.junit.Ignore;
import org.junit.rules.TemporaryFolder;

@Ignore
public class CopyOnInstallTestCase extends AbstractMuleMavenPluginTestCase
{
    private TemporaryFolder tempFolder = new TemporaryFolder();
    private File muleHome;

    @Override
    public void setUp() throws Exception
    {
        super.setUp();
        
        assertNull("the MULE_HOME environment variable may not be set when running the tests",
            System.getenv("MULE_HOME"));
        
        tempFolder.create();
        muleHome = tempFolder.newFolder("MULE_HOME");
    }

    @Override
    protected void tearDown() throws Exception
    {
        tempFolder.delete();
        super.tearDown();
    }

    public void testMuleHomeSet() throws Exception
    {
        Properties props = new Properties();
        props.put("mule.home", muleHome.getAbsolutePath());
        builder.setProperties(props);

        File appsDirectory = createAppsDirectory();

        InvocationResult result = installProject("copyToAppsDirectory");
        assertSuccess(result);

        File deployedZip = new File(appsDirectory, "muleApp.zip");
        assertFileExists(deployedZip);
    }

    public void testMuleHomeNotSet() throws Exception
    {
        File appsDirectory = createAppsDirectory();

        InvocationResult result = installProject("copyToAppsDirectory");
        assertSuccess(result);

        File deployedZip = new File(appsDirectory, "muleApp.zip");
        assertFileDoesNotExist(deployedZip);
    }

    private File createAppsDirectory()
    {
        File appsDirectory = new File(muleHome, "apps");
        if (appsDirectory.mkdirs() == false)
        {
            fail("Could not make directory " + appsDirectory.getAbsolutePath());
        }
        return appsDirectory;
    }
}
