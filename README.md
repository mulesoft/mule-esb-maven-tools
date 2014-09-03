Mule ESB Maven Tools
====================

The Mule Application Kit allows the development of Mule application based on Maven tooling. This kit includes archetypes for building regular Mule applications.

Creating a Mule Application
--------------------------

Creating a mule application using the mule archetype project is extremely easy. Just invoke it as follows:

     mvn archetype:generate -DarchetypeGroupId=org.mule.tools.maven -DarchetypeArtifactId=maven-achetype-mule-app \
	-DarchetypeVersion=1.0-SNAPSHOT -DgroupId=org.mule -DartifactId=mule-test-archetype -Dversion=1.0-SNAPSHOT \
	-DmuleVersion=3.5.0 -Dpackage=org.mule -Dtransports=file,http,jdbc,jms,vm -Dmodules=client,cxf,management,scripting,sxc,xml
	
In case you want your application to belong to a mule domain then you can add the domain specification parameters:

     mvn archetype:generate -DarchetypeGroupId=org.mule.tools.maven -DarchetypeArtifactId=maven-achetype-mule-app \
	-DarchetypeVersion=1.0-SNAPSHOT -DgroupId=org.mule -DartifactId=mule-test-archetype -Dversion=1.0-SNAPSHOT \
	-DmuleVersion=3.5.0 -Dpackage=org.mule -Dtransports=file,http,jdbc,jms,vm -Dmodules=client,cxf,management,scripting,sxc,xml \
	-DdomainGroupId=org.mule.domain -DdomainArtifactId=my-domain -DdomainVersion=1.0-SNAPSHOT

Archetype Parameters:

|parameter|description|default|
|:--------|:----------|:----------|
|archetypeGroupId|The group Id of the archetype This value must ALWAYS by org.mule.tools.maven||
|archetypeArtifactId|The artifact Id of the archetype| This value must ALWAYS mule-archetype-project||
|archetypeVersion|The version of the archetype. This value can change as we release new versions of the archetype. Always use the latest non-SNAPSHOT version available.||
|groupId|The group Id of the application you are creating. A good value would be the reserve name of your company domain name, like: com.mulesoft.app or org.mule.app||
|artifactId|The artifact Id of the application you are creating. ||
|version|The version of your application. Usually 1.0-SNAPSHOT.|1.0-SNAPSHOT|
|muleVersion|The version of the mule runtime you are going to use. Mule 2.2.x is no longer supported|3.2.1|
|transports|A comma separated list of the transport you are going to use within your application.|file,http,jdbc,jms,vm |
|modules|A comma separated list of the modules you are going to use within your application. |client,cxf,management,scripting,sxc,xml |
|EE|A flag to import the EE counterpart of the transports/modules you are using. |false |
|domainGroupId|The group Id of the domain that applications belongs to. |empty |
|domainArtifactId|The artifact Id of the domain that applications belongs to. |empty |
|domainVersion|The version of the domain that applications belongs to. |empty |

Creating a Mule Domain
--------------------------

A mule application can belong to a domain group. The domain allows sharing of resources such as connectors or libraries between applications.

To create a domain execute:

     mvn archetype:generate -DarchetypeGroupId=org.mule.tools.maven -DarchetypeArtifactId=maven-achetype-mule-domain \
	-DarchetypeVersion=1.0-SNAPSHOT -DgroupId=org.mule.test -DartifactId=mule-test-domain -Dversion=1.0-SNAPSHOT \
	-Dpackage=org.mule.test -DEE=true

Archetype Parameters:

|parameter|description|default|
|:--------|:----------|:----------|
|archetypeGroupId|The group Id of the archetype This value must ALWAYS by org.mule.tools.maven||
|archetypeArtifactId|The artifact Id of the archetype| This value must ALWAYS by maven-achetype-mule-domain||
|archetypeVersion|The version of the archetype. This value can change as we release new versions of the archetype. Always use the latest non-SNAPSHOT version available.||
|groupId|The group Id of the domain you are creating. A good value would be the reserve name of your company domain name, like: com.mulesoft.app or org.mule.app||
|artifactId|The artifact Id of the domain you are creating. ||
|version|The version of your application. Usually 1.0-SNAPSHOT. Your domain name, when depoyed to mule, will be artifactId-version|1.0-SNAPSHOT|
|EE|A flag to import the EE counterpart of the domain namespace. |false |


Create a Complete Mule Domain Project
----------------------------------------

Several mule applications will make use of a particular mule domain for sharing resources. So it make perfect sense to aggregate all the applications and
the domain in a single project.

To create a maven project with the domain and all the applications that will be deployed using that domain you can use the mule domain bundle archetype:

     mvn archetype:generate -DarchetypeGroupId=org.mule.tools.maven -DarchetypeArtifactId=maven-achetype-mule-domain-bundle \
	-DarchetypeVersion=1.0-SNAPSHOT -DgroupId=org.mule.test -DartifactId=mule-api -Dversion=1.0-SNAPSHOT \
	-Dpackage=org.mule.test -DEE=false

This command will create a maven multi-module project with the following modules:
 - domain: This project is exactly as any project created with mule domain archetype. The artifact id for this project is ${artifactId}-domain. In this case would be mule-api-domain.
 - apps: This project is a bundle project for the mule applications that belong to this domain. Create here the mule applications using the mule applications archetype.
 - domain-bundle: This project is creates a bundle artifact with the domain plus the applications. This bundle project can be deployed as any domain and will also deploy the domain applications.

 |parameter|description|default|
 |:--------|:----------|:----------|
 |archetypeGroupId|The group Id of the archetype This value must ALWAYS by org.mule.tools.maven||
 |archetypeArtifactId|The artifact Id of the archetype| This value must ALWAYS by maven-achetype-mule-domain-bundle||
 |archetypeVersion|The version of the archetype. This value can change as we release new versions of the archetype. Always use the latest non-SNAPSHOT version available.||
 |groupId|The group Id of the domain bundle project you are creating. A good value would be the reserve name of your company domain name, like: com.mulesoft.app or org.mule.app||
 |artifactId|The artifact Id of the domain bundle you are creating. Try to not include the domain word in it. ||
 |version|The version of your domain bundle. Usually 1.0-SNAPSHOT. Your domain name, when deployed to mule, will be artifactId-version|1.0-SNAPSHOT|
 |package|Required by maven archetype but not used. ||
 |EE|A flag to import the EE counterpart of the domain namespace. |false |

