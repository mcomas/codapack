# CoDaPack

Historically CoDaPack 3D was intended to be a package of Compositional 
Data with an easy and intuitive way of use. For this reason from the 
beginning it has been associated with Excel, software known and used 
for many people. However, over the years different versions of Excel 
and Windows have been appeared and CoDaPack has had to be adapted to 
these new versions due to some incompatibilities.

For this reason and also because of CoDaPack only worked with Excel under 
windows; the Girona Compositional Data Group decided to implement a new 
software with at least the same capabilities and the same profile of users 
but independent of any other software.

The new CoDaPack has three different areas: the variables area, the data 
area and the results area which has a textual output window and independent 
graphical output. Also it is expected to work at least under Unix, Window 
and MacOS operating systems.

# CoDaPack building 

To run CoDaPack you need at least Java SE Runtime Environment 8. To build the executables Java SE Development Kit 8 and Maven are required.

## Command line steps to build CoDaPack

Clone codapack from Github and move into codapack's folder

```{r}
git clone http://github.com/mcomas/codapack.git
cd codapack
```
`codapack` is packaged as a Maven project and can be imported to any IDE: Netbeans, Eclipse or IntelliJ IDEA. To build CoDaPack from the command line just execute

```
mvn package
```

Maven will create the follwing targets:

* `CoDaPack-2.02.21-jar-with-dependencies.jar` jar file with all dependencies needed.
* `CoDaPack.exe` executable file for Windows.
* `CoDaPack-2.02.21.dmg` application for MacOSX.

