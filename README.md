# PaymentsOnline  
A web application that allows users to select unpaid invoices from Xero or Alma and submit payments via the campus Transact system.

#Building/Testing via Maven:  
+Maven 3.9.14 is the latest release (as of 2026-03-26), and can be downloaded [here](https://maven.apache.org/download.cgi).
+Maven installation instructions are [here](https://maven.apache.org/install.html).
+The Maven build uses a custom repo hosted in GitHub, [here](https://github.com/UCLALibrary/MavenForDLSR). To access this repo, the Maven `settings.xml` file 
(usually found in `$USER_HOME/.m2`) must have the following stanza added to the `<servers>` section:
```
<server>
  <id>github</id>
  <username>[github user name]</username>
  <password>[github classic access tokens]</password>
</server>
```
+There is a token created for the `uclalibraryservicesrobot` account which has access to this repo (contact David Rickard for token), or users can
create their own (token must have write/delete packages and read:user permissions).
+LPO runs on Java 1.8.0_452 on the `*-w-webservices01` servers. It can _probably_ be built against later Java versions, but there are no guarantees of
not encountering deprecated classes/methods.
+If you have a preexisting, later than 8 Java install on your testing machine, and install Java 8 for LPO, you will need to temporarily set Java home
at Maven runtime:
```
Unix/Linux BASH: 	JAVA_HOME=/path/to/your/jdk mvn [maven commands]  
Windows Powershell:	$env:JAVA_HOME = "C:\Path\To\Your\JDK"; mvn [maven commands]  
Windows cmd prompt:	set JAVA_HOME=C:\Path\To\Your\JDK && mvn [maven commands]  
```
+To confirm project will build on test machine, execute `mvn clean compile`
+To confirm tests will build on test machine, execute `mvn clean test-compile`
+To execute all tests, execute `mvn test`
+To execute a particular test class, execute `mvn test -Dtest=[test class]`