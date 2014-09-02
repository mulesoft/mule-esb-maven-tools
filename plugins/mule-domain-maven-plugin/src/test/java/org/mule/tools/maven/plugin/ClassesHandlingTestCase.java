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

import org.apache.maven.shared.invoker.InvocationResult;
import org.junit.Ignore;

@Ignore
public class ClassesHandlingTestCase extends AbstractMuleMavenPluginTestCase
{
    public void testPackageWithClassesFolder() throws Exception
    {
        String projectName = "project-with-plain-classes";

        InvocationResult result = buildProject(projectName);
        assertSuccess(result);

        String pathToClasses = String.format("target/it/%1s/target/classes", projectName);
        File classesFolder = new File(pathToClasses);
        assertFileExists(classesFolder);
        assertTrue(classesFolder.isDirectory());
    }

    public void testPackageWithArchivedClasses() throws Exception
    {
        String projectName = "project-with-archived-classes";

        InvocationResult result = buildProject(projectName);
        assertSuccess(result);

        String classesArchive = String.format("target/it/%1s/target/%2s-1.0-SNAPSHOT.jar",
            projectName, projectName);
        assertFileExists(new File(classesArchive));
    }

    public void testPackageWithoutClassesFolder() throws Exception
    {
        String projectName = "project-without-classes";

        InvocationResult result = buildProject(projectName);
        assertSuccess(result);

        String classesFolder = String.format("target/it/%1s/target/classes", projectName);
        assertFalse(new File(classesFolder).exists());
    }

    public void testPackageWithEmptyArchive() throws Exception
    {
        String projectName = "project-without-archived-classes";

        // the archive will contain the mule-config.xml at least ...
        InvocationResult result = buildProject(projectName);
        assertSuccess(result);
    }
}
