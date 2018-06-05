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

import java.io.File;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;

public class MuleInstallMojoTest {

  @Rule
  public TemporaryFolder temp = new TemporaryFolder();

  private MuleInstallMojo mojo;

  @Before
  public void setUp() {
    mojo = new MuleInstallMojo();
  }

  @Test
  public void renameFile() throws IOException, MojoExecutionException {
    String origFileName = "file.temp";
    String destFileName = "file.zip";

    File orig = temp.newFile(origFileName);
    File dest = new File(temp.getRoot(), destFileName);

    assertThat("Origin file should exist", orig.exists());
    assertThat("Destination file should not exist", !dest.exists());

    mojo.renameFile(orig, dest);

    assertThat("Origin file should not exist", !orig.exists());
    assertThat("Destination file should exist", dest.exists());
  }
}