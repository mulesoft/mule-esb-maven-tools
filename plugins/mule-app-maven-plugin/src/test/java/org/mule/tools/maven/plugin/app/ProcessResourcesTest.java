/**
 * Mule ESB Maven Tools
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.tools.maven.plugin.app;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mule.tools.maven.plugin.ZipArchiver;

import java.io.File;
import java.io.IOException;

import static org.apache.maven.it.util.FileUtils.fileRead;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class ProcessResourcesTest {

  public static final String PROJECT_NAME = "project-name";
  @Rule
  public TemporaryFolder temporaryFolder = new TemporaryFolder();
  private ProcessResources processResources;

  @Before
  public void setUp() {
    processResources = new ProcessResources();
  }

  @Test
  public void generateExportedZipFile() throws MojoExecutionException, IOException {
    ProcessResources processResourcesSpy = spy(processResources);
    ZipArchiver zipArchiverMock = mock(ZipArchiver.class);
    doReturn(zipArchiverMock).when(processResourcesSpy).getZipArchiver();

    File metaInfFolderMock = mock(File.class);
    File projectBaseFolderMock = mock(File.class);

    processResourcesSpy.generateExportedZipFile(metaInfFolderMock, projectBaseFolderMock);

    verify(zipArchiverMock).toZip(eq(projectBaseFolderMock), anyString());
  }

  @Test
  public void generateMuleExportProperties() throws IOException, MojoExecutionException {
    File muleExportProperties = new File(temporaryFolder.getRoot(), "mule_export.properties");

    assertThat("File should not exist", muleExportProperties.exists(), is(false));

    processResources.generateMuleExportProperties(temporaryFolder.getRoot(), PROJECT_NAME);

    assertThat("File should exist", muleExportProperties.exists(), is(true));

    String contents = fileRead(muleExportProperties);

    assertThat("Property file should contain mule export version", contents.contains("mule_export_version=2.0"), is(true));
    assertThat("Property file should contain mule export project name", contents.contains("mule_exported_projects=" + PROJECT_NAME), is(true));
  }
}