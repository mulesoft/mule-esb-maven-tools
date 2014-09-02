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

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.settings.Server;
import org.apache.maven.settings.Settings;
import org.mule.tools.maven.plugin.app.cloudhub.CloudHubAdapter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Base class for CloudHub Mojos
 */
public abstract class AbstractCloudHubMojo extends AbstractMojo {

    private static final String URL_LAYOUT = ".*://(.*)(/+)";

    /**
     * @parameter expression="${cloudhub.url}" default-value="https://cloudhub.io/"
     * @required
     */
    protected String cloudHubUrl;

    /**
     * @parameter expression="${cloudhub.domain}"
     * @required
     */
    protected String domain;

    /**
     * @parameter expression="${cloudhub.username}"
     */
    protected String username;

    /**
     * @parameter expression="${cloudhub.password}"
     */
    protected String password;

    /**
     * @parameter default-value="${settings}"
     * @readonly
     */
    private Settings settings;

    /**
     * @component
     */
    private CloudHubAdapter cloudHubAdapter;

    protected Server getServer() throws MojoExecutionException {
        return this.settings.getServer(normalize(this.cloudHubUrl));
    }

    protected String normalize( String url) throws MojoExecutionException {
        final Pattern pattern = Pattern.compile(AbstractCloudHubMojo.URL_LAYOUT);
        final Matcher matcher = pattern.matcher(url);
        if (!matcher.matches()) {
            throw new MojoExecutionException("Invalid URL <"+url+">");
        }
        return matcher.group(1);
    }

    protected String getUsername() throws MojoExecutionException {
        if (this.username != null) {
            return this.username;
        }

        final Server server = getServer();
        if (server == null) {
            throw new MojoExecutionException("Failed to extract username from server settings");
        }
        return server.getUsername();
    }

    protected String getPassword() throws MojoExecutionException {
        if (this.password != null) {
            return this.password;
        }

        final Server server = getServer();
        if (server == null) {
            throw new MojoExecutionException("Failed to extract password from server settings");
        }
        return server.getPassword();
    }

    protected CloudHubAdapter createDomainConnection() throws MojoExecutionException {
        cloudHubAdapter.create(this.cloudHubUrl, getUsername(), getPassword(), this.domain);
        return cloudHubAdapter;
    }

}
