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
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.codehaus.plexus.util.IOUtil;
import org.junit.Ignore;

@Ignore
public class AppsFolderFilteringTestCase extends AbstractMuleMavenPluginTestCase
{
    public void testFilteringDisabledByDefault() throws Exception
    {
        File appZip = zipFileFromBuildingProject("filtering-disabled-by-default");
        String muleConfig = contentsOfMuleConfigFromZipFile(appZip);
        assertTrue(muleConfig.contains("${thePort}"));
    }

    public void testFilteringDisabledInConfig() throws Exception
    {
        File appZip = zipFileFromBuildingProject("filtering-disabled-in-config");
        String muleConfig = contentsOfMuleConfigFromZipFile(appZip);
        assertTrue(muleConfig.contains("${thePort}"));
    }

    public void testFilteringEnabled() throws Exception
    {
        File appZip = zipFileFromBuildingProject("filtering-enabled");
        String muleConfig = contentsOfMuleConfigFromZipFile(appZip);
        assertFalse(muleConfig.contains("${thePort}"));
        assertTrue(muleConfig.contains("http://localhost:8888/"));
    }
    
    public void testFilteringWithExplicitFilterElementInPom() throws Exception
    {
        File appZip = zipFileFromBuildingProject("filtering-explicit-filter-element");
        String muleConfig = contentsOfMuleConfigFromZipFile(appZip);
        assertFalse(muleConfig.contains("${thePort}"));
        assertTrue(muleConfig.contains("http://localhost:8888/"));
    }

    private String contentsOfMuleConfigFromZipFile(File muleAppZipFile) throws Exception
    {
        ZipFile zipFile = null;
        InputStream muleConfigStream = null;
        try
        {
            zipFile = new ZipFile(muleAppZipFile);

            ZipEntry muleConfigEntry = zipFile.getEntry("mule-config.xml");
            assertNotNull(muleConfigEntry);

            muleConfigStream = zipFile.getInputStream(muleConfigEntry);
            return IOUtil.toString(muleConfigStream);
        }
        finally
        {
            if (zipFile != null)
            {
                zipFile.close();
            }
            if (muleConfigStream != null)
            {
                muleConfigStream.close();
            }
        }
    }
}
