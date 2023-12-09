#!/bin/bash

# Define paths to your JDK and other necessary files
JDK_PATH=../../jdk-17.jdk
JAR_PATH=/Users/uri/Documents/CODAPACK-ARM/target/CoDaPack-2.03.06-jar-with-dependencies.jar
JSON_PATH=/Users/uri/Documents/CODAPACK-ARM/codapack_structure.json
HELP_PATH=/Users/uri/Documents/CODAPACK-ARM/Help
RSCRIPTS_PATH=/Users/uri/Documents/CODAPACK-ARM/RScripts
ICON_PATH=/Users/uri/Documents/CODAPACK-ARM/AppIcon.icns
RLIBRARIES_PATH=/Users/uri/Documents/CODAPACK-ARM/Rlibraries

# Create "CoDaPack.app" and its structure
mkdir -p CoDaPack.app/Contents
cd CoDaPack.app/Contents

cat << EOF > info.plist
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
mkdir Java
cp -rf $JDK_PATH/* Java/

# Create MacOS folder and copy necessary files
mkdir MacOS
cp $JAR_PATH MacOS/
cp $JSON_PATH MacOS/
cp -r $RSCRIPTS_PATH MacOS/
cp -r $HELP_PATH MacOS/


# Create run.sh in MacOS
cd MacOS

cat << EOF > run.sh
#!/bin/zsh
# source env_variables
EXEDIR="\$(dirname "\$0")"

cd \$EXEDIR

export JAVA_HOME="../Java/"
export R_LIBS_USER="../Rlibraries"
export PATH=\$JAVA_HOME/bin:\$R_LIBS_USER/rJava/jri:\$PATH
export R_HOME="/Library/Frameworks/R.Framework/Versions/4.3-arm64/Resources"

export CDP_WORKING_DIR=\$EXEDIR
export CDP_RESOURCES="."
export CDP_R_SCRIPTS=\$CDP_RESOURCES/Rscripts/

../Java/bin/java -Djava.library.path="\$R_LIBS_USER/rJava/jri" -Xdock:name="CoDaPack" -Xdock:icon="../Resources/AppIcon.icns" -jar "CoDaPack-2.03.06-jar-with-dependencies.jar" 
EOF
chmod +x run.sh

# Go back to Contents and create Resources folder, then copy the icon
cd ..
mkdir Resources
cp $ICON_PATH Resources/

# Copy Rlibraries
cp -r $RLIBRARIES_PATH .

# Return to the starting directory
cd ../..

# End of script
echo "CoDaPack.app structure created successfully."
