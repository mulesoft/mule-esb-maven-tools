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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.IOUtil;

/**
 * @goal install
 * @requiresDependencyResolution runtime
 */
public class MuleDomainInstallMojo extends AbstractMuleMojo
{
    /**
     * If set to <code>true</code> attempt to copy the Mule application zip to $MULE_HOME/apps
     *
     * @parameter alias="copyToDomainsDirectory" expression="${copyToDomainsDirectory}" default-value="false"
     * @required
     */
    protected boolean copyToDomainsDirectory;
    
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        if (copyToDomainsDirectory || Boolean.getBoolean("mule.domain.copyToDomainsDirectory"))
        {
            File muleHome = determineMuleHome();
            if (muleHome != null)
            {
                copyMuleZipToMuleHome(muleHome);
            }
            else
            {
                getLog().warn("MULE_HOME is not set, not copying " + finalName + ".zip");
            }
        }
    }

    private File determineMuleHome() throws MojoExecutionException
    {
        File muleHomeFile = null;

        String muleHome = getMuleHomeEnvVarOrSystemProperty();
        if (muleHome != null)
        {
            muleHomeFile = new File(muleHome);
            if (muleHomeFile.exists() == false)
            {
                String message =
                    String.format("MULE_HOME is set to %1s but this directory does not exist.",
                    muleHome);
                throw new MojoExecutionException(message);
            }
            if (muleHomeFile.canWrite() == false)
            {
                String message =
                    String.format("MULE_HOME is set to %1s but the directory is not writeable.",
                    muleHome);
                throw new MojoExecutionException(message);
            }
        }

        return muleHomeFile;
    }

    private String getMuleHomeEnvVarOrSystemProperty()
    {
        String muleHome = System.getenv("MULE_HOME");
        if (muleHome == null)
        {
            // fall back to a system property which is set from the plugin testing framework
            // when invoking the integration tests
            muleHome = System.getProperty("mule.home");
        }
        return muleHome;
    }

    private void copyMuleZipToMuleHome(File muleHome) throws MojoExecutionException
    {
        try
        {
            copyMuleZipFileToTempFileInDomainsDirectory(muleHome);
            renameMuleZipFileToFinalName(muleHome);
        }
        catch (IOException iox)
        {
            throw new MojoExecutionException("Exception while copying to apps directory", iox);
        }
    }

    private void copyMuleZipFileToTempFileInDomainsDirectory(File muleHome) throws IOException
    {
        InputStream muleZipInput = null;
        OutputStream tempOutput = null;
        try
        {
            File zipFile = getMuleZipFile();
            muleZipInput = new FileInputStream(zipFile);

            File tempFile = tempFileInDomainsDirectory(muleHome);
            tempOutput = new FileOutputStream(tempFile);

            IOUtil.copy(muleZipInput, tempOutput);

            String message = String.format("Copying %1s to %2s", zipFile.getAbsolutePath(),
                tempFile.getAbsolutePath());
            getLog().info(message);
        }
        finally
        {
            IOUtil.close(muleZipInput);
            IOUtil.close(tempOutput);
        }
    }

    private void renameMuleZipFileToFinalName(File muleHome) throws MojoExecutionException
    {
        File sourceFile = tempFileInDomainsDirectory(muleHome);

        File domainsDirectory = muleDomainsDirectory(muleHome);
        File targetFile = new File(domainsDirectory, finalName + ".zip");

        if (sourceFile.renameTo(targetFile) == false)
        {
            String message = String.format("Could not rename %1s to %2s",
                sourceFile.getAbsolutePath(), targetFile.getAbsolutePath());
            throw new MojoExecutionException(message);
        }

        String message = String.format("Renaming %1s to %2s", sourceFile.getAbsolutePath(),
            targetFile.getAbsolutePath());
        getLog().info(message);
    }

    private File tempFileInDomainsDirectory(File muleHome)
    {
        File appsDirectory = muleDomainsDirectory(muleHome);
        return new File(appsDirectory, finalName + ".temp");
    }

    private File muleDomainsDirectory(File muleHome)
    {
        return new File(muleHome, "domains");
    }
}
