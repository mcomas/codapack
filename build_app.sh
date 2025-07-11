#!/bin/bash

set -e

# Define version and paths
VERSION=2.03.09
JAVA_HOME=jdk-17
JAR_PATH=target/CoDaPack-${VERSION}-jar-with-dependencies.jar
YAML_PATH=codapack_structure.yaml
HELP_PATH=Help
RSCRIPTS_PATH=RScripts
ICON_PATH=src/main/resources/icons.icns
RLIBRARIES_PATH=Rlibraries
RFRAMEWORK_PATH=/Applications/CoDaPack.app/Contents/R.framework

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
cp $YAML_PATH $CONTENTS/MacOS/

rsync -av $RSCRIPTS_PATH/ $CONTENTS/MacOS/RScripts/
rsync -av $HELP_PATH/ $CONTENTS/MacOS/Help/

# Copy R.framework and R libraries
rsync -av $RFRAMEWORK_PATH/ $CONTENTS/R.framework/
rsync -av $RLIBRARIES_PATH/ $CONTENTS/Rlibraries/

# Create run.sh
cat << EOF > $CONTENTS/MacOS/CoDaPack
#!/bin/zsh
EXEDIR="\$(cd "\$(dirname "\$0")" && pwd)"
cd \$EXEDIR

export JAVA_HOME=../Java/
export R_HOME=/Applications/CoDaPack.app/Contents/R.framework/Resources/lib/R
export R_LIBS_USER="../Rlibraries"
export PATH=\$JAVA_HOME/bin:\$R_LIBS_USER/rJava/jri:/usr/local/bin:\$PATH
export DYLD_FALLBACK_LIBRARY_PATH=\$R_HOME/lib:\$R_LIBS_USER/rJava/jri

export CDP_WORKING_DIR=\$EXEDIR
export CDP_RESOURCES=.
export CDP_R_SCRIPTS=\$CDP_RESOURCES/RScripts/

# Check Rscript
if [ ! -x "\$R_HOME/bin/Rscript" ]; then
  echo "Rscript not found in \$R_HOME/bin/"
  exit 1
fi

exec java -Djava.library.path="\$R_LIBS_USER/rJava/jri" \\
     -Xdock:name="CoDaPack" \\
     -Xdock:icon="../Resources/AppIcon.icns" \\
     -jar "CoDaPack-${VERSION}-jar-with-dependencies.jar"
EOF

chmod +x $CONTENTS/MacOS/CoDaPack

# Copy resources
mkdir -p $CONTENTS/Resources
cp $ICON_PATH $CONTENTS/Resources/AppIcon.icns

# Remove quarantine attributes
sudo xattr -cr CoDaPack.app

echo "CoDaPack.app structure created successfully."
