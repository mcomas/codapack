#!/bin/bash

set -e

# Define version and paths
VERSION=2.03.07
JAVA_HOME=jdk-17.0.7.jdk/Contents/Home/
JAR_PATH=target/CoDaPack-${VERSION}-jar-with-dependencies.jar
JSON_PATH=codapack_structure.json
HELP_PATH=Help
RSCRIPTS_PATH=RScripts
ICON_PATH=src/main/resources/icons.icns
RLIBRARIES_PATH=Rlibraries

# Clean previous build
rm -rf CoDaPack.app

# Create app structure
mkdir -p CoDaPack.app/Contents
CONTENTS=CoDaPack.app/Contents

# Create info.plist
cat << EOF > $CONTENTS/info.plist
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" 
"http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
<dict>
    <key>CFBundleExecutable</key>
    <string>CoDaPack</string>
    <key>CFBundleIconFile</key>
    <string>AppIcon.icns</string>
    <key>CFBundleName</key>
    <string>CoDaPack</string>
    <key>CFBundleVersion</key>
    <string>${VERSION}</string>
    <key>CFBundleIdentifier</key>
    <string>com.coda.codaPack</string>
    <key>CFBundlePackageType</key>
    <string>APPL</string>
</dict>
</plist>
EOF

# Copy JDK
mkdir -p $CONTENTS/Java
cp -a $JAVA_HOME/. $CONTENTS/Java/

# Copy application files
mkdir -p $CONTENTS/MacOS
cp $JAR_PATH $CONTENTS/MacOS/
cp $JSON_PATH $CONTENTS/MacOS/

rsync -av $RSCRIPTS_PATH/ $CONTENTS/MacOS/RScripts/
rsync -av $HELP_PATH/ $CONTENTS/MacOS/Help/

# Create run.sh
cat << EOF > $CONTENTS/MacOS/CoDaPack
#!/bin/zsh
EXEDIR="\$(dirname "\$0")"
cd \$EXEDIR

export JAVA_HOME="../Java/"
export R_LIBS_USER="../Rlibraries"
export PATH=\$JAVA_HOME/bin:\$R_LIBS_USER/rJava/jri:/usr/local/bin:\$PATH
export R_HOME="\$(R RHOME)"

export CDP_WORKING_DIR=\$EXEDIR
export CDP_RESOURCES="."
export CDP_R_SCRIPTS=\$CDP_RESOURCES/RScripts/

exec java -Djava.library.path="\$R_LIBS_USER/rJava/jri" -Xdock:name="CoDaPack" -Xdock:icon="../Resources/AppIcon.icns" -jar "CoDaPack-${VERSION}-jar-with-dependencies.jar"
EOF

chmod +x $CONTENTS/MacOS/CoDaPack

# Copy resources
mkdir -p $CONTENTS/Resources
cp $ICON_PATH $CONTENTS/Resources/AppIcon.icns

# Copy R libraries
rsync -av $RLIBRARIES_PATH/ $CONTENTS/Rlibraries/

echo "CoDaPack.app structure created successfully."
