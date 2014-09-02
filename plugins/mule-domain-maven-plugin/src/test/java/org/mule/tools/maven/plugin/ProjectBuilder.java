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
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.test.plugin.BuildTool;
import org.apache.maven.shared.test.plugin.TestToolsException;
import org.junit.Ignore;

@Ignore
public class ProjectBuilder
{
    private BuildTool buildTool;
    private List<String> goals;
    private Properties properties;
    private File outputFile;

    public ProjectBuilder(BuildTool buildTool)
    {
        super();
        this.buildTool = buildTool;
        this.properties = new Properties();
        setGoals("compile", "package");
    }

    public InvocationResult build(File pom) throws TestToolsException
    {
        InvocationRequest request = buildTool.createBasicInvocationRequest(pom, properties, goals,
            outputFile);
        request.setUpdateSnapshots(false);
        request.setShowErrors(true);
        request.setDebug(false);

        return buildTool.executeMaven(request);
    }

    public void setGoals(String... goals)
    {
        this.goals = Arrays.asList(goals);
    }

    public void setOutputFile(File outputFile)
    {
        this.outputFile = outputFile;
    }

    public void setProperties(Properties properties)
    {
        this.properties = properties;
    }
}
