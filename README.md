xcode-teamcity-plugin
=============

This is a plugin for the Continuous Integration server [TeamCity](http://www.jetbrains.com/teamcity/).

Installation
------------
### Prepare

Run the commands listed in `importPackages.sh` in your `TeamCity/devPackage` folder to import the needed jars.

### Server

Run in directory `xcode-teamcity-plugin-server` the maven command

    mvn assembly:assembly

and move the generated zip file into `$HOME/.Buildserver/plugins`.

Restart your TeamCity server.

### Build Agent

Run in directory `xcode-teamcity-plugin-agent` the maven command

    mvn assembly:assembly
    
and move the generated zip file into `<TeamCityBuildAgent>/plugins`.
Unzip the file and restart your TeamCity Build Agent.
