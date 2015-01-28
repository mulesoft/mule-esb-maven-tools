
WELCOME
=======


Congratulations, you have just created a new Mule project!

This wizard created a number of new classes and resources useful for new Mule
projects.  Each of the created files contains documentation and _todo_ items
where necessary.  Here is an overview of what was created.

./pom.xml:

A maven project descriptor that describes how to build this project.  If you
enabled this project for the MuleForge, this file will contain additional
information about the project on MuleForge.


.src/main/app/mule-config.xml

A basic configuration to get you started.

.src/main/app/mule-deploy.properties

A basic deployment descriptor that controls how the application should be deployed.
Here you can specify things like the encoding to use, or which domain the app belongs to.
For more information see: http://www.mulesoft.org/documentation/display/current/Deployment+Descriptor

TESTING
=======

This project also contains test classes that can be run as part of a test suite.

-----------------------------------------------------------------
./src/test/java/${packageInPathFormat}/ExampleFunctionalTestCase.java

This is an example functional test case.  The test will work as is, but you
need to configure it to actually test your code.
#if(${domainGroupId} != 'empty' && $muleVersion.matches("(3.5.[^0|1|2]*|3.6.[^0]*|3.7.*)"))
Also, the test assumes the domain configuration file is name 'mule-domain-config.xml'. So you should modify it accordingly if it's not.
This file is added to the test resources by maven, so you should run 'mvn compile' before attempting to run it through an IDE.
#end
For more information about
testing see: http://www.mulesoft.org/documentation/display/current/Functional+Testing.

ADDITIONAL RESOURCES
====================

Everything you need to know about getting started with Mule can be found here:
http://www.mulesoft.org/documentation/display/current/Home

For information about working with Mule Studio which is the Mule IDE can be found here:
http://www.mulesoft.org/documentation/display/current/Anypoint+Studio+Essentials

Remember if you get stuck you can try getting help on the Mule forum:
http://forum.mulesoft.org/mulesoft

Also, MuleSoft, the company behind Mule, offers 24x7 support options:
http://www.mulesoft.com/support-and-services/enterprise-subscriptions-support

Enjoy your Mule ride!

The Mule Team
