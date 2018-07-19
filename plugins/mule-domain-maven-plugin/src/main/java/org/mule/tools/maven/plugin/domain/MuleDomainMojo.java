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
import java.util.List;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProjectHelper;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.zip.ZipArchiver;
import org.codehaus.plexus.logging.Logger;

/**
 * Build a Mule domain archive.
 *
 * @phase package
 * @goal mule-domain
 * @requiresDependencyResolution runtime
 */
public class MuleDomainMojo extends AbstractMuleMojo
{

    /**
     * @component
     */
    private MavenProjectHelper projectHelper;

    /**
     * List of exclusion elements (having groupId and artifactId children) to exclude from the
     * application archive.
     *
     * @parameter
     * @since 1.2
     */
    private List<Exclusion> exclusions;

    /**
     * List of inclusion elements (having groupId and artifactId children) to exclude from the
     * application archive.
     *
     * @parameter
     * @since 1.5
     */
    private List<Inclusion> inclusions;

    /**
     * Exclude all artifacts with Mule groupIds. Default is <code>true</code>.
     *
     * @parameter default-value="true"
     * @since 1.4
     */
    private boolean excludeMuleDependencies;

    /**
     * @parameter default-value="false"
     * @since 1.8
     */
    private boolean prependGroupId;

    public void execute() throws MojoExecutionException, MojoFailureException
    {
        File domain = getMuleZipFile();
        try
        {
            createMuleDomain(domain);
        }
        catch (ArchiverException e)
        {
            throw new MojoExecutionException("Exception creating the Mule App", e);
        }

        this.project.getArtifact().setFile(domain);
    }

    protected Logger createLogger()
    {
        //TODO see how to fix this
        return new Logger()
        {
            public void debug(String s)
            {
                getLog().debug(s);
            }

            public void debug(String s, Throwable throwable)
            {
                getLog().debug(s, throwable);
            }

            public boolean isDebugEnabled()
            {
                return getLog().isDebugEnabled();
            }

            public void info(String s)
            {
                getLog().info(s);
            }

            public void info(String s, Throwable throwable)
            {
                getLog().info(s, throwable);
            }

            public boolean isInfoEnabled()
            {
                return getLog().isInfoEnabled();
            }

            public void warn(String s)
            {
                getLog().warn(s);
            }

            public void warn(String s, Throwable throwable)
            {
                getLog().warn(s, throwable);
            }

            public boolean isWarnEnabled()
            {
                return getLog().isWarnEnabled();
            }

            public void error(String s)
            {
                getLog().error(s);
            }

            public void error(String s, Throwable throwable)
            {
                getLog().error(s, throwable);
            }

            public boolean isErrorEnabled()
            {
                return getLog().isErrorEnabled();
            }

            public void fatalError(String s)
            {
                getLog().error(s);
            }

            public void fatalError(String s, Throwable throwable)
            {
                getLog().error(s, throwable);
            }

            public boolean isFatalErrorEnabled()
            {
                return getLog().isErrorEnabled();
            }

            public Logger getChildLogger(String s)
            {
                return null;
            }

            public int getThreshold()
            {
                return 0;
            }

            public String getName()
            {
                return null;
            }
        };
    }

    protected void createMuleDomain(final File domain) throws MojoExecutionException, ArchiverException
    {
        //PLG - not for now
        //validateProject();

        MuleArchiver archiver = new MuleArchiver(prependGroupId);
        //addAppDirectory(archiver);
        //addCompiledClasses(archiver);
        addDependencies(archiver);
        addResourcesFile(archiver);
        addDomainFile(archiver);
        //addMappingsDirectory(archiver);
        addMetaInfDirectory(archiver);

        archiver.setDestFile(domain);

        try
        {
            domain.delete();
            archiver.createArchive();
        }
        catch (IOException e)
        {
            getLog().error("Cannot create archive", e);
        }
    }

    private void addMetaInfDirectory(MuleArchiver archiver) throws MojoExecutionException {
        if (this.metaInfDirectory.exists()) {
            getLog().info("Copying META-INF directly");
            try {
                archiver.addDirectory(this.metaInfDirectory, "META-INF/", null, null);
            } catch (ArchiverException e) {
                throw new MojoExecutionException("Error adding META-INF " + metaInfDirectory.getName(),e);
            }
        }
        else
        {
            getLog().info(this.metaInfDirectory + " does not exist, skipping");
        }
    }

    protected void addAppsZipFilesToArchive(ZipArchiver archiver, File appsFolder) throws MojoExecutionException
    {
        if (appsFolder.exists())
        {
            File[] directories = appsFolder.listFiles();
            for (File file : directories)
            {
                if (file.isDirectory() && !file.isHidden())
                {
                    File targetFolder = new File(file, "target");
                    if (!targetFolder.exists())
                    {
                        throw new MojoExecutionException("Cannot bound app " + file.getName() + ". Seems it was not build");
                    }
                    File[] zipFiles = targetFolder.listFiles(new FilenameFilter()
                    {
                        public boolean accept(File dir, String name)
                        {
                            return name.endsWith(".zip");
                        }
                    });
                    if (zipFiles.length == 0)
                    {
                        throw new MojoExecutionException("No application zip in project " + file.getName());
                    }
                    File muleApp = zipFiles[0];
                    try
                    {
                        archiver.addFile(muleApp, "apps" + File.separator + muleApp.getName());
                    }
                    catch (ArchiverException e)
                    {
                        throw new MojoExecutionException("Error adding domain application " + muleApp.getName(),e);
                    }
                }
            }
        }
        else
        {
            throw new RuntimeException(appsFolder.getAbsolutePath() + " does not exists");
        }
    }

    private void addDomainFile(MuleArchiver archiver) throws ArchiverException
    {
        archiver.addResources(domainDirectory);
    }

    private void addResourcesFile(MuleArchiver archiver) throws ArchiverException
    {
        archiver.addResources(resourcesDirectory);
    }

    private void validateProject() throws MojoExecutionException
    {
        File muleConfig = new File(appDirectory, "mule-config.xml");
        File deploymentDescriptor = new File(appDirectory, "mule-deploy.properties");

        if ((muleConfig.exists() == false) && (deploymentDescriptor.exists() == false))
        {
            String message = format("No mule-config.xml or mule-deploy.properties in %1s",
                                    this.project.getBasedir());

            getLog().error(message);
            throw new MojoExecutionException(message);
        }
    }

    private void addDependencies(MuleArchiver archiver) throws ArchiverException
    {
        for (Artifact artifact : getArtifactsToArchive())
        {
            String message = format("Adding <%1s> as a lib", artifact.getId());
            getLog().info(message);
            archiver.addLibraryArtifact(artifact);
        }
    }

    private Set<Artifact> getArtifactsToArchive()
    {
        ArtifactFilter filter = new ArtifactFilter(this.project, this.inclusions,
            this.exclusions, this.excludeMuleDependencies);
        return filter.getArtifactsToArchive();
    }


}
