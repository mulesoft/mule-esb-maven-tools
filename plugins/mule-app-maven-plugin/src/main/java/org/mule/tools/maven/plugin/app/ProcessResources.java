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
import org.apache.maven.plugin.MojoFailureException;
import org.mule.tools.maven.plugin.MuleExportProperties;
import org.mule.tools.maven.plugin.ZipArchiver;

import java.io.File;
import java.io.IOException;

import static org.mule.tools.maven.plugin.MuleExportProperties.MULE_EXPORT_PROPERTIES_FILE;

/**
 * This mojo generates the META-INF folder with the necessary files to make the zip
 * importable in Studio.
 *
 * @phase process-resources
 * @goal process-resources
 */
public class ProcessResources extends AbstractMuleMojo {

  public static final String META_INF_FOLDER = "META-INF";
  public static final String[] BLACK_LIST = new String[]{".DS_STORE", "target"};

  /**
   * Whether this project should attach mule sources to make it importable in Studio.
   *
   * @parameter alias="attachMuleSources" expression="${attachMuleSources}" default-value="false"
   * @required
   */
  private boolean attachMuleSources;

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    if (attachMuleSources) {
      File metaInfFolder = createMetaInfFolderInTarget();

      File projectBaseFolder = project.getBasedir();
      generateExportedZipFile(metaInfFolder, projectBaseFolder);

      String projectName = projectBaseFolder.getName();
      generateMuleExportProperties(metaInfFolder, projectName);
    } else {
      getLog().info("attachMuleSources default value is false, skipping process-resources goal...");
    }
  }

  /**
   * Creates the META-INF folder in the target directory.
   *
   * @return the file representing META-INF
   */
  private File createMetaInfFolderInTarget() {
    File targetFolder = new File(project.getBuild().getDirectory());
    File metaInfFolder = new File(targetFolder, META_INF_FOLDER);
    metaInfFolder.mkdir();
    return metaInfFolder;
  }

  /**
   * Generates a zip file containing all the project content. The file is named as the projectBaseFolderName + .zip
   *
   * @param metaInfFolder
   * @param projectBaseFolder
   * @throws MojoExecutionException
   */
  protected void generateExportedZipFile(File metaInfFolder, File projectBaseFolder) throws MojoExecutionException {
    ZipArchiver archiver = getZipArchiver();
    String output = metaInfFolder.getAbsolutePath() + File.separator + projectBaseFolder.getName() + ".zip";
    try {
      archiver.toZip(projectBaseFolder, output);
    } catch (IOException e) {
      throw new MojoExecutionException("Could not create exportable zip file", e);
    }
  }

  /**
   * Generates the mule_export.properties file in META-INF
   *
   * @param metaInfFolder
   * @param projectName
   * @throws MojoExecutionException
   */
  protected void generateMuleExportProperties(File metaInfFolder, String projectName) throws MojoExecutionException {
    MuleExportProperties exportProperties = new MuleExportProperties(projectName);
    try {
      exportProperties.store(metaInfFolder);
    } catch (IOException e) {
      throw new MojoExecutionException("Could not create " + MULE_EXPORT_PROPERTIES_FILE, e);
    }
  }


  public ZipArchiver getZipArchiver() {
    return new ZipArchiver(BLACK_LIST);
  }
}
