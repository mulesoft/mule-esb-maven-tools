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
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.test.plugin.BuildTool;
import org.junit.Ignore;

@Ignore
public class AbstractMuleMavenPluginTestCase extends AbstractMojoTestCase
{
    private static final String BUILD_OUTPUT_DIRECTORY = "target/surefire-reports/build-output";

    protected ProjectBuilder builder;

    @Override
    public void setUp() throws Exception
    {
        super.setUp();

        BuildTool buildTool = (BuildTool) lookup(BuildTool.ROLE, "default");
        builder = new ProjectBuilder(buildTool);
    }

    protected InvocationResult installProject(String projectName) throws Exception
    {
        builder.setGoals("compile", "install");
        return buildProject(projectName);
    }

    protected InvocationResult buildProject(String projectName) throws Exception
    {
        File outputFile = createBuildLogFile(projectName);
        builder.setOutputFile(outputFile);

        File pomFile = pomInProject(projectName);
        return builder.build(pomFile);
    }

    private File createBuildLogFile(String basename)
    {
        File outputDir = new File(BUILD_OUTPUT_DIRECTORY);
        outputDir.mkdirs();

        return new File(outputDir, basename + ".log");
    }

    private File pomInProject(String projectName)
    {
        File projectFolder = new File("target/it", projectName);
        File pomFile = new File(projectFolder, "pom.xml");
        assertFileExists(pomFile);
        return pomFile;
    }

    protected File zipFileFromBuildingProject(String projectName) throws Exception
    {
        InvocationResult result = buildProject(projectName);
        assertSuccess(result);

        String appArchivePath = String.format("target/it/%1s/target/%2s-1.0-SNAPSHOT.zip",
            projectName, projectName);
        File appArchiveFile = new File(appArchivePath);
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

    protected void assertSuccess(InvocationResult result)
    {
        assertEquals("Expected exit code 0", 0, result.getExitCode());
    }

    protected void assertFailure(InvocationResult result)
    {
        assertNotSame("Expected exit code != 0", 0, result.getExitCode());
    }

    protected void assertZipContains(File zipFile, String... filenames) throws IOException
    {
        List<String> filesInZip = listZip(zipFile);

        for (String filename : filenames)
        {
            assertTrue("Zip file does not contain " + filename, filesInZip.contains(filename));
        }
    }

    private List<String> listZip(File file) throws IOException
    {
        List<String> filenames = new ArrayList<String>();

        ZipFile zipFile = null;
        try
        {
            zipFile = new ZipFile(file);

            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements())
            {
                ZipEntry entry = entries.nextElement();
                filenames.add(entry.getName());
            }
        }
        finally
        {
            if (zipFile != null)
            {
                zipFile.close();
            }
        }

        return filenames;
    }
}


