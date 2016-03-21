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

import org.apache.commons.io.IOUtils;
import org.apache.maven.it.VerificationException;
import org.apache.maven.it.Verifier;
import org.apache.maven.it.util.IOUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

public abstract class AbstractMavenIT {

    private static final boolean DEBUG = false;

    protected abstract String getArtifactVersion();

    protected abstract String getArtifactId();

    protected abstract String getGroupId();

    protected abstract File getRoot();

    protected abstract void verify() throws Exception;

    @Before
    public void setUp() throws VerificationException, IOException {
        Verifier verifier = new Verifier(getRoot().getAbsolutePath());

        // Deleting a former created artefact from the archetype to be tested
        verifier.deleteArtifact(getGroupId(), getArtifactId(), getArtifactVersion(), null);

        // Delete the created maven project
        verifier.deleteDirectory(getArtifactId());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void buildExecutable() throws Exception {
        try {
            Verifier verifier = new Verifier(getRoot().getAbsolutePath(), null, DEBUG, true);
            verifier.setAutoclean(true);

            setSystemProperties(verifier);

            Map<String, String> envVars = new HashMap<String, String>();
            envVars.put("MAVEN_OPTS", "-Xmx512m -XX:MaxPermSize=256m");

            verifier.executeGoal("package", envVars);

            verifier.verifyErrorFreeLog();

            verify();
        } catch (IOException ioe) {
            throw new VerificationException(ioe);
        }
    }

    protected void setSystemProperties(Verifier verifier) throws IOException {
        InputStream systemPropertiesStream = null;
        try {
            systemPropertiesStream = getClass().getClassLoader().getResourceAsStream("maven.properties");
            Properties systemProperties = new Properties();
            systemProperties.load(systemPropertiesStream);
            verifier.setSystemProperties(systemProperties);
        } finally {
            IOUtils.closeQuietly(systemPropertiesStream);
        }
    }

    protected String contentsOfMuleConfigFromZipFile(File muleAppZipFile) throws Exception
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

    protected File zipFileFromBuildingProject() throws Exception
    {
        String appArchivePath = String.format("target/integration-tests/%1s/target/%2s-1.0-SNAPSHOT.zip",
                getArtifactId(), getArtifactId());
        File appArchiveFile = (new File(appArchivePath)).getAbsoluteFile();
        assertFileExists(appArchiveFile);

        return appArchiveFile;
    }

    protected void assertFileExists(File file)
    {
        assertTrue(file.getAbsolutePath() + " must exist", file.exists());
    }

    protected void assertFileDoesNotExist(File file)
    {
        assertFalse(file.getAbsolutePath() + " must not exist", file.exists());
    }

    protected void assertZipDoesNotContain(File file, String... filenames) throws IOException
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

    protected void assertZipDoesContain(File file, String... filenames) throws IOException
    {
        ZipFile zipFile = null;
        try
        {
            zipFile = new ZipFile(file);
            for (String name :filenames)
            {
                if (zipFile.getEntry(name) == null)
                {
                    fail(file.getAbsolutePath() + " does not contain valid entry " + name);
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