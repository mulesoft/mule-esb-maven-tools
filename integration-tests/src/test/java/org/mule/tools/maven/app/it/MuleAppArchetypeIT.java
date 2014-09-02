/**
 * Mule ESB Maven Tools
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.tools.maven.app.it;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.maven.it.VerificationException;
import org.apache.maven.it.Verifier;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class MuleAppArchetypeIT {

    private static final File ROOT = new File("target/integration-tests/");
    private static final String ARCHETYPE_PROPERTIES = "/mule-app-archetype.properties";

    private Properties archetypeProperties;
    private Properties verifierProperties;

    @Before
    public void setUp() throws VerificationException, IOException {
        InputStream stream = getClass().getResourceAsStream(ARCHETYPE_PROPERTIES);
        archetypeProperties = new Properties();
        archetypeProperties.load(stream);

        verifierProperties = new Properties();
        verifierProperties.setProperty("use.mavenRepoLocal", "true");

        Verifier verifier = new Verifier(ROOT.getAbsolutePath());

        // deleting a former created artifact from the archetype to be tested
        verifier.deleteArtifact(getGroupId(), getArtifactId(), getVersion(), null);

        // delete the created maven project
        verifier.deleteDirectory(getArtifactId());
    }

    private String getVersion() {
        return archetypeProperties.getProperty("version");
    }

    private String getArtifactId() {
        return archetypeProperties.getProperty("artifactId");
    }

    private String getGroupId() {
        return archetypeProperties.getProperty("groupId");
    }

    @Test
    @Ignore
    public void testGenerateArchetype() throws VerificationException {
        Verifier verifier = new Verifier(ROOT.getAbsolutePath());
        verifier.setSystemProperties(archetypeProperties);
        verifier.setVerifierProperties(verifierProperties);
        verifier.setAutoclean(false);
        verifier.setMavenDebug(false);
        verifier.setDebug(false);

        verifier.executeGoal("archetype:generate");

        verifier.verifyErrorFreeLog();

        verifier = new Verifier(ROOT.getAbsolutePath() + "/" + getArtifactId());
        verifier.setAutoclean(true);
        verifier.executeGoal("package");

        verifier.verifyErrorFreeLog();
    }
}