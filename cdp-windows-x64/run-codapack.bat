set PATH=R-4.3.1\bin\x64;Rlibraries\rJava\jri;%PATH%
set JAVA_HOME=jdk-17.0.7
set R_HOME=R-4.3.1
set R_LIBS_USER=Rlibraries
set CDP_R_SCRIPTS=Rscripts

jdk-17.0.7\bin\java -Djava.library.path=Rlibraries\rJava\jri -classpath target\classes;target\dependency\* coda.gui.CoDaPackMain
