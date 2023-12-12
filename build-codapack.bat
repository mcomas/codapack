set PATH=%PATH%;"R-4.3.1\bin\x64";"jdk-17.0.7\bin"
set JAVA_HOME="jdk-17.0.7"
set R_HOME="R-4.3.1"
set R_LIBS_USER=Rlibraries

apache-maven-3.9.2\bin\mvn package & apache-maven-3.9.2\bin\mvn dependency:copy-dependencies & Rscript -e "TYPE = 'binary'; source('install_packages.R')"

