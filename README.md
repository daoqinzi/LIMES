# LIMES - Link Discovery Framework for Metric Spaces.

[![Build Status](https://travis-ci.org/dice-group/LIMES.svg?branch=master)](https://travis-ci.org/dice-group/LIMES) [![Join the chat at https://gitter.im/dice_limes/Lobby](https://badges.gitter.im/dice_limes/Lobby.svg)](https://gitter.im/dice_limes/Lobby?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

## Generating Jar File (Headless)
installing use:
```
mvn clean install
```

Creating the runnable jar file including the dependencies use:
```
mvn clean package shade:shade -Dcheckstyle.skip=true -Dmaven.test.skip=true
```

The .jar will be placed in `limes-core/target/limes-core-VERSION-SNAPSHOT.jar`

## Generating Jar File (GUI)
switch to `limes-gui` and use:
```
mvn jfx:jar -Dcheckstyle.skip=true -Dmaven.test.skip=true
```

The .jar will be placed in `limes-gui/target/jfx/app/limes-GUI-VERSION-SNAPSHOT-jfx.jar`

The `limes-gui/target/jfx/app/lib` folder needs to be in the same folder as the .jar for the .jar to work!

### Importing into Eclipse
In case Eclipse does not recognize the project as Java. Please run the following from the `limes-core/` directory:
```
mvn eclipse:eclipse
```
Then, update the project on Eclipse.


## More details

* [Project web site](http://cs.uni-paderborn.de/ds/research/research-projects/active-projects/limes/)
* [User manual](http://dice-group.github.io/LIMES/user_manual/)
* [Developer manual](http://dice-group.github.io/LIMES/developer_manual/)

## Working with semantic similarities
In order to use the semantic similarities incorporated into LIMES, you must follow the next steps:
* Visit https://wordnet.princeton.edu/download/current-version
* Go to WordNet 3.1 DATABASE FILES ONLY and follow the instructions on how to download version 3.1 files
* Create a folder named wordent/ inside /src/main/resources/
* Unzip the downloaded package from the wordnet website 
* Place the dict folder inside /src/main/resources/wordnet/
* Now you are ready to use the semantic similarities
