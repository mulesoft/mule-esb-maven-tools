/**
* Mule ESB Maven Tools
*
* Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
*
* The software in this package is published under the terms of the CPAL v1.0
* license, a copy of which has been included with this distribution in the
* LICENSE.txt file.
*/
//
//package org.mule.tools.maven.plugin.app;
//
//import static org.apache.maven.plugin.testing.ArtifactStubFactory.setVariableValueToObject;
//import static org.mockito.Matchers.any;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//import java.io.File;
//import java.util.Arrays;
//import java.util.Properties;
//
//import org.apache.maven.artifact.Artifact;
//import org.apache.maven.project.MavenProject;
//import org.apache.maven.settings.Server;
//import org.apache.maven.settings.Settings;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.junit.runners.JUnit4;
//
//import org.mule.tools.maven.plugin.app.ArtifactCloudHubDeployMojo;
//import org.mule.tools.maven.plugin.app.cloudhub.CloudHubAdapter;
//
//import com.mulesoft.cloudhub.client.CloudHubConnectionI;
//import com.mulesoft.cloudhub.client.CloudHubDomainConnectionI;
//
//@RunWith(JUnit4.class)
//public class CloudHubDeployMojoTest {
//
//    private AbstractCloudHubDeployMojo mojo;
//
//    @Before
//    public void setUp() throws Exception {
//
//        mojo = new ArtifactCloudHubDeployMojo();
//
//        CloudHubConnectionI connection = mock(CloudHubConnectionI.class);
//
//        CloudHubDomainConnectionI domainConnection = mock(CloudHubDomainConnectionI.class);
//
//        when(connection.connectWithDomain(any(String.class))).thenReturn(domainConnection);
//
//        MavenProject project = mock(MavenProject.class);
//
//        Artifact artifact = mock(Artifact.class);
//
//        when(artifact.getType()).thenReturn(ArtifactCloudHubDeployMojo.MULE_TYPE);
//
//        File file = mock(File.class);
//        when(artifact.getFile()).thenReturn(file);
//
//        when(project.getArtifact()).thenReturn(artifact);
//        when(project.getAttachedArtifacts()).thenReturn(Arrays.asList(artifact));
//        when(project.getProperties()).thenReturn(new Properties());
//
//        String cloudHubUrl = "https://cloudhub.io/";
//
//        Settings settings = mock(Settings.class);
//
//        Server server = mock(Server.class);
//
//        when(settings.getServer(any(String.class))).thenReturn(server);
//
//        CloudHubAdapter cloudHubAdapter = mock(CloudHubAdapter.class);
//
//        setVariableValueToObject(mojo, "project", project);
//        setVariableValueToObject(mojo, "settings", settings);
//        setVariableValueToObject(mojo, "cloudHubUrl", cloudHubUrl);
//        setVariableValueToObject(mojo, "cloudHubAdapter", cloudHubAdapter);
//    }
//
//    @Test
//    public void testExecute() throws Exception {
//        mojo.execute();
//    }
//}
