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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @phase process-resources
 * @goal process-resources
 */
public class ProcessResources extends AbstractMuleMojo {

  public static final String META_INF_FOLDER = "META-INF";
  public static final String MULE_EXPORT_PROPERTIES_FILE = "mule_export.properties";
  public static final String MULE_EXPORT_VERSION_KEY = "mule_export_version";
  public static final String MULE_EXPORT_VERSION_VALUE = "2.0";
  public static final String MULE_EXPORTED_PROJECTS_KEY = "mule_exported_projects";
  public static final String[] BLACK_LIST = new String[]{".DS_STORE", "target"};
  private static final Set<String> BLACK_LIST_SET = new HashSet<>(Arrays.asList(BLACK_LIST));

  /**
   * Whether this project should attach mule sources to make it importable in Studio.
   *
   * @parameter alias="attachMuleSources" expression="${attachMuleSources}" default-value="false"
   * @required
   */
  private boolean attachMuleSources;

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    if(attachMuleSources) {
      File projectBaseFolder = project.getBasedir();
      File targetFolder = new File(project.getBuild().getDirectory());
      String projectName = projectBaseFolder.getName();
      File metaInfFolder = new File(targetFolder, META_INF_FOLDER);

      metaInfFolder.mkdir();

      generateMuleExportProperties(metaInfFolder, projectName);
      generateExportedZipFile(metaInfFolder, projectBaseFolder);
    } else {
      getLog().info("attachMuleSources default value is false, skipping process-resources goal...");
    }
  }

  private void generateExportedZipFile(File exportableFolder, File projectBaseFolder) {
    FileOutputStream fos = null;
    try {
      fos = new FileOutputStream(exportableFolder.getAbsolutePath() + File.separator + projectBaseFolder.getName() + ".zip");
      ZipOutputStream zipOut = new ZipOutputStream(fos);

      zipFile(projectBaseFolder, projectBaseFolder.getName(), zipOut);
      zipOut.close();
      fos.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void generateMuleExportProperties(File exportableFolder, String projectName) throws MojoExecutionException {
    try (FileOutputStream output = new FileOutputStream(new File(exportableFolder, MULE_EXPORT_PROPERTIES_FILE))) {
      Properties muleExportProperties = new Properties();
      muleExportProperties.setProperty(MULE_EXPORT_VERSION_KEY, MULE_EXPORT_VERSION_VALUE);
      muleExportProperties.setProperty(MULE_EXPORTED_PROJECTS_KEY, projectName);
      muleExportProperties.store(output, null);
    } catch (IOException e) {
      throw new MojoExecutionException("Could not create " + MULE_EXPORT_PROPERTIES_FILE, e);
    }
  }

  private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
    if (!isInBlackList(fileToZip)) {
      if (fileToZip.isDirectory()) {
        File[] children = fileToZip.listFiles();
        ZipEntry zipEntry = new ZipEntry(fileName + "/");
        zipOut.putNextEntry(zipEntry);
        for (File childFile : children) {
          zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
        }
      } else {
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
          zipOut.write(bytes, 0, length);
        }
        fis.close();
      }
    }
  }

  private static boolean isInBlackList(File fileToZip) {
    return BLACK_LIST_SET.contains(fileToZip.getName());
  }
}
