/**
 * Mule ESB Maven Tools
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.tools.maven.plugin.domain;

import static java.lang.String.format;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.zip.ZipArchiver;
import org.codehaus.plexus.archiver.zip.ZipUnArchiver;
import org.codehaus.plexus.util.FileUtils;

/**
 * Build a Mule domain bundle archive.
 *
 * @phase package
 * @goal mule-domain-bundle
 * @requiresDependencyResolution runtime
 */
public class MuleDomainBundleMojo extends MuleDomainMojo
{


    public void execute() throws MojoExecutionException, MojoFailureException
    {
        File targetDirectory = new File(project.getBuild().getDirectory());
        if (!targetDirectory.exists())
        {
            if (!targetDirectory.mkdir())
            {
                throw new MojoExecutionException("Could not create target directory");
            }
        }
        if (project.getParent() == null)
        {
            throw new MojoExecutionException("Mule maven plugin with bundleApps=true can't be used without a parent project aggregating domains and apps");
        }
        if (project.getParent().getModules() == null || project.getParent().getModules().size() < 3)
        {
            throw new MojoExecutionException("Verify your project structure. Seems that domain project, or apps project or domain-bundle project are missing");
        }
        String domainProjectFolder = (String) project.getParent().getModules().get(0);

        File domainProjectFolderFile = new File(project.getBasedir().getParentFile(), domainProjectFolder);

        if (!domainProjectFolderFile.exists())
        {
            throw new MojoExecutionException(format("Folder for project %s does not exists", domainProjectFolder));
        }
        File domainProjectTargetFolderFile = new File(domainProjectFolderFile, "target");
        if (!domainProjectTargetFolderFile.exists())
        {
            throw new MojoExecutionException(format("Folder target for project %s does not exists. Did you build %s project successfully?", domainProjectFolder, domainProjectTargetFolderFile.getAbsolutePath()));
        }
        File[] filesInTargetFolder = domainProjectTargetFolderFile.listFiles(new FilenameFilter()
        {
            public boolean accept(File dir, String name)
            {
                return name.endsWith(".zip");
            }
        });
        if (filesInTargetFolder.length == 0)
        {
            throw new MojoExecutionException(format("Target folder %s does not contain a domain zip file", domainProjectFolder));
        }
        File domainZipFile = filesInTargetFolder[0];
        ZipUnArchiver domainUnzipArchiver = new ZipUnArchiver();
        File domainBundleTempDir = new File(project.getBasedir(), "target" + File.separator + "domain-bundle-temp");
        if (domainBundleTempDir.exists())
        {
            try
            {
                FileUtils.deleteDirectory(domainBundleTempDir);
            }
            catch (IOException e)
            {
                throw new MojoExecutionException("Could not delete directory " + domainBundleTempDir.getAbsolutePath());
            }
        }
        if (!domainBundleTempDir.mkdir())
        {
            throw new MojoExecutionException(format("Could not create folder %s", domainBundleTempDir));
        }
        try
        {
            domainUnzipArchiver.enableLogging(createLogger());
            domainUnzipArchiver.setSourceFile(domainZipFile);
            domainUnzipArchiver.setDestDirectory(domainBundleTempDir);
            domainUnzipArchiver.extract();
        }
        catch (ArchiverException e)
        {
            throw new MojoExecutionException("Failure unzipping domain zip file",e);
        }
        ZipArchiver muleArchiver = new ZipArchiver();
        try
        {
            muleArchiver.addDirectory(domainBundleTempDir);
        }
        catch (ArchiverException e)
        {
            throw new MojoExecutionException("Could not add domain temp directory to zip files", e);
        }
        File appsModuleFolder = new File(project.getBasedir().getParent(), "apps");
        if (!appsModuleFolder.exists())
        {
            throw new MojoExecutionException(format("Could not find apps module in %s", appsModuleFolder.getAbsolutePath()));
        }
        addAppsZipFilesToArchive(muleArchiver, appsModuleFolder);
        String extension = ".zip";
        File artifactFile = new File(targetDirectory, project.getBuild().getFinalName() + extension);
        //this is required by the install phase.
        project.getArtifact().setFile(artifactFile);
        muleArchiver.setDestFile(artifactFile);
        try
        {
            muleArchiver.createArchive();
            domainBundleTempDir.delete();
        }
        catch (Exception e)
        {
            throw new MojoExecutionException("Could not create domain bundle zip file", e);
        }
    }


}
