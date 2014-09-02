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

import org.apache.maven.artifact.Artifact;

import java.io.File;

/**
 * @phase deploy
 * @goal cloudhub-deploy
 */
public class ArtifactCloudHubDeployMojo extends AbstractCloudHubDeployMojo {

    static final String MULE_TYPE = "mule";

    @Override
	protected File getArtifactFile() {
		final String type = this.project.getArtifact().getType();
        if (!MULE_TYPE.equals(type)) {
            throw new IllegalArgumentException("Only supports mule packaging type, not <"+type+">.");
        }

        if (this.project.getAttachedArtifacts().isEmpty()) {
            throw new IllegalArgumentException("No Mule application attached. This probably means `package` phase has not been executed.");
        }

        final Artifact artifact = (Artifact) this.project.getAttachedArtifacts().get(0);
        
		return artifact.getFile();
	}
}