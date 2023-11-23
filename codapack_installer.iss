; Command line call: 
; C:\Program Files (x86)\Inno Setup 5\ISCC.exe build_setup_file.iss
; Script generated by the Inno Setup Script Wizard.
; SEE THE DOCUMENTATION FOR DETAILS ON CREATING INNO SETUP SCRIPT FILES!

#define MyAppName "CoDaPack"
#define MyAppVersion "2.03.06"
#define MyAppPublisher "Universitat de Girona"
#define MyAppURL "http://ima.udg.edu/codapack/"
#define MyAppExeName "CoDaPack.exe"

[Setup]
; NOTE: The value of AppId uniquely identifies this application.
; Do not use the same AppId value in installers for other applications.
; (To generate a new GUID, click Tools | Generate GUID inside the IDE.)
AppId={{C1C9D163-40EA-40F2-81C2-877DA22B1570}
AppName={#MyAppName}
AppVersion={#MyAppVersion}
;AppVerName={#MyAppName} {#MyAppVersion}
AppPublisher={#MyAppPublisher}
AppPublisherURL={#MyAppURL}
AppSupportURL={#MyAppURL}
AppUpdatesURL={#MyAppURL}
DefaultDirName={commonpf64}\{#MyAppName}
DefaultGroupName={#MyAppName}
LicenseFile=license.txt
OutputDir=target\
OutputBaseFilename=CoDaPack-setup-{#MyAppVersion}
SetupIconFile=src\main\resources\icon.ico
Compression=lzma
SolidCompression=yes
ChangesAssociations=yes

[Languages]
Name: "english"; MessagesFile: "compiler:Default.isl"

[Tasks]
Name: "desktopicon"; Description: "{cm:CreateDesktopIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked

[Files]
Source: "jdk-17.0.7\*"; DestDir: "{app}\jdk-17.0.7"; Flags: ignoreversion recursesubdirs
Source: "R-4.3.1\*"; DestDir: "{app}\R-4.3.1"; Flags: ignoreversion recursesubdirs
Source: "Rlibraries\*"; DestDir: "{app}\Rlibraries"; Flags: ignoreversion recursesubdirs
Source: "Rscripts\*"; DestDir: "{app}\Rscripts"; Flags: ignoreversion recursesubdirs
Source: "src\*"; DestDir: "{app}\src"; Flags: ignoreversion recursesubdirs
Source: "target\CoDaPack-2.03.06-jar-with-dependencies.jar"; DestDir: "{app}\target"; Flags: ignoreversion recursesubdirs
Source: "CoDaPack.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "codapack_structure.json"; DestDir: "{app}"; Flags: ignoreversion
;Source: "target\codapack-{#MyAppVersion}.jar"; DestDir: "{app}"; Flags: ignoreversion
Source: "license.txt"; DestDir: "{app}"; Flags: ignoreversion
; NOTE: Don't use "Flags: ignoreversion" on any shared system files

[Icons]
Name: "{group}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"
Name: "{commondesktop}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"; Tasks: desktopicon

[Run]
Filename: "{app}\{#MyAppExeName}"; Description: "{cm:LaunchProgram,{#StringChange(MyAppName, '&', '&&')}}"; Flags: nowait postinstall skipifsilent

[Registry]
Root: HKCR; Subkey: ".cdp"; ValueType: string; ValueName: ""; ValueData: "cdpfile"; Flags: uninsdeletevalue
Root: HKCR; Subkey: "cdpfile"; ValueType: string; ValueName: ""; ValueData: "CoDaPack File"; Flags: uninsdeletekey
Root: HKCR; Subkey: "cdpfile\DefaultIcon"; ValueType: string; ValueName: ""; ValueData: "{app}\CoDaPack.exe,0"
Root: HKCR; Subkey: "cdpfile\shell\open\command"; ValueType: string; ValueName: ""; ValueData: """{app}\CoDaPack.exe"" ""%1"""
