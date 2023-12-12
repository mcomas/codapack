#!/bin/bash

# Define paths to your JDK and other necessary files
JAVA_HOME=jdk-17.0.7.jdk/Contents/Home/
JAR_PATH=target/CoDaPack-2.03.06-jar-with-dependencies.jar
JSON_PATH=codapack_structure.json
HELP_PATH=Help
RSCRIPTS_PATH=RScripts
ICON_PATH=src/main/resources/icons.icns
RLIBRARIES_PATH=Rlibraries

# Create "CoDaPack.app" and its structure
mkdir -p CoDaPack.app/Contents
CONTENTS=CoDaPack.app/Contents

cat << EOF > $CONTENTS/info.plist
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" 
"http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
<dict>
    <key>CFBundleExecutable</key>
    <string>run.sh</string>
    <key>CFBundleIconFile</key>
    <string>AppIcon.icns</string>
</dict>
</plist>
EOF

# Copy JDK contents
mkdir $CONTENTS/Java
cp -rf $JAVA_HOME/* $CONTENTS/Java/

# Create MacOS folder and copy necessary files
mkdir $CONTENTS/MacOS
cp $JAR_PATH $CONTENTS/MacOS/
cp $JSON_PATH $CONTENTS/MacOS/
cp -r $RSCRIPTS_PATH $CONTENTS/MacOS/
cp -r $HELP_PATH $CONTENTS/MacOS/


# Create run.sh in MacOS
cat << EOF > $CONTENTS/MacOS/run.sh
#!/bin/zsh
# source env_variables
EXEDIR="\$(dirname "\$0")"

cd \$EXEDIR

export JAVA_HOME="../Java/"
export R_LIBS_USER="../Rlibraries"
export PATH=\$JAVA_HOME/bin:\$R_LIBS_USER/rJava/jri:\$PATH
export R_HOME="$(R RHOME)"

export CDP_WORKING_DIR=\$EXEDIR
export CDP_RESOURCES="."
export CDP_R_SCRIPTS=\$CDP_RESOURCES/Rscripts/

exec java -Djava.library.path="\$R_LIBS_USER/rJava/jri" -Xdock:name="CoDaPack" -Xdock:icon="../Resources/AppIcon.icns" -jar "CoDaPack-2.03.06-jar-with-dependencies.jar" 
EOF
chmod +x $CONTENTS/MacOS/run.sh

# Go back to Contents and create Resources folder, then copy the icon
mkdir $CONTENTS/Resources
cp $ICON_PATH $CONTENTS/Resources/AppIcon.icns

# Copy Rlibraries
cp -r $RLIBRARIES_PATH $CONTENTS/

# End of script
echo "CoDaPack.app structure created successfully."
