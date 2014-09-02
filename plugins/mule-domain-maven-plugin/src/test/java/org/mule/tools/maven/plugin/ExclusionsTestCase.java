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
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.junit.Ignore;

@Ignore
public class ExclusionsTestCase extends AbstractMuleMavenPluginTestCase
{
    private static final String LOG4J_JAR = "lib/log4j-1.2.14.jar";
    private static final String MULE_CORE_JAR = "lib/mule-core-2.2.1.jar";

    public void testExcludeDirectDependency() throws Exception
    {
        File zipFile = zipFileFromBuildingProject("exclude-direct-dependency");

        String muleCoreLib = "lib/mule-core-2.2.1.jar";
        String beanutilsLib = "lib/commons-beanutils-1.7.0-osgi.jar"; // this is a transitive dependency of mule-core
        assertZipDoesNotContain(zipFile, muleCoreLib, beanutilsLib);
    }

    /**
     * log4j is a transitive dependency of a dependency that mule-core pulls in. Check that only
     * this dependency is excluded.
     */
    public void testExcludeTransitiveLeaf() throws Exception
    {
        File zipFile = zipFileFromBuildingProject("exclude-transitive-leaf");
        assertZipDoesNotContain(zipFile, LOG4J_JAR);
    }

    /**
     * The dependeny tree looks like this:
     * <pre>
     * org.mule.transports:mule-transport-http:jar:2.2.1:compile
     * +- org.mule:mule-core:jar:2.2.1:compile
     * \- log4j:log4j:jar:1.2.14:compile
     * </pre>
     *
     * Make sure that mule-core and log4j are excluded
     */
    public void testExcludeTransitiveDependencyAndChildren() throws Exception
    {
        File zipFile = zipFileFromBuildingProject("exclude-transitive-with-child");
        assertZipDoesNotContain(zipFile, MULE_CORE_JAR, LOG4J_JAR);
    }

    public void testAutoExcludeDirectMuleDependency() throws Exception
    {
        File zipFile = zipFileFromBuildingProject("auto-exclude-direct-mule-dependency");
        assertZipDoesNotContain(zipFile, MULE_CORE_JAR);
    }

    private void assertZipDoesNotContain(File file, String... filenames) throws IOException
    {
        ZipFile zipFile = null;
        try
        {
            zipFile = new ZipFile(file);

            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements())
            {
                ZipEntry entry = entries.nextElement();

                for (String name :filenames)
                {
                    if (entry.getName().equals(name))
                    {
                        fail(file.getAbsolutePath() + " contains invalid entry " + name);
                    }
                }
            }
        }
        finally
        {
            if (zipFile != null)
            {
                zipFile.close();
            }
        }
    }
}


