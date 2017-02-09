We use some third party libraries, are not in maven public repositories. You need to execute in this directory:

mvn install:install-file -Dfile=granule-1.0.9.jar
mvn install:install-file -Dfile=maven-merge-properties-plugin-0.2.jar

Maven documentation:
https://maven.apache.org/guides/mini/guide-3rd-party-jars-local.html
