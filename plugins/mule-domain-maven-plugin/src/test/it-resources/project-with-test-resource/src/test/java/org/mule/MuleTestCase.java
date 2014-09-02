/**
 * Mule ESB Maven Tools
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule;

import java.net.URL;
import junit.framework.TestCase;

public class MuleTestCase extends TestCase
{
    public void testMuleConfigIsAttached()
    {
        URL muleConfigUrl = getClass().getClassLoader().getSystemResource("mule-config.xml");
        assertNotNull(muleConfigUrl);
    }
}
