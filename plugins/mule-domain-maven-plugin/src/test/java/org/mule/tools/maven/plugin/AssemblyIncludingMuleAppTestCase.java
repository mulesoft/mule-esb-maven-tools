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

import org.apache.maven.shared.invoker.InvocationResult;
import org.junit.Ignore;

@Ignore
public class AssemblyIncludingMuleAppTestCase extends AbstractMuleMavenPluginTestCase
{
    public void testAssemblyIncludingMuleApp() throws Exception
    {
        String projectName = "assembly-with-mule-app";
        InvocationResult result = installProject(projectName);
        assertSuccess(result);
    }
}
