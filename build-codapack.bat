set PATH=%PATH%;"R-4.3.1\bin\x64";"jdk\bin"
set JAVA_HOME="jdk"
set R_HOME="R-4.3.1"
set R_LIBS_USER=Rlibraries

apache-maven-3.9.2\bin\mvn package & apache-maven-3.9.2\bin\mvn dependency:copy-dependencies & Rscript -e "TYPE = 'binary'; source('install_packages.R')"
