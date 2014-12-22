How to verify the fix for https://bugzilla.redhat.com/show_bug.cgi?id=1164199
-----------------------------------------------------------------------------

1. Run maven to build the project:
   
   $ mvn clean package
   
2. The 2nd test - testWithExpire - will fail:

Failed tests:   testWithExpire(org.drools.bug.leak.LeakTest): Leak ... SimpleFactWithExpire should have been evacuated from heap


3. Change the pom.xml to use the patch libraries:

   $ mv pom.xml pom.xml.brms603-unpatched
   $ mv pom.xml.patch pom.xml
   
4. Run maven to build the project again:
   
   $ mvn clean package
   
5. Both tests should finish successfully
