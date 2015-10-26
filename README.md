# Pi4j Client [![Circle CI](https://circleci.com/gh/lachatak/pi4j-client/tree/master.svg?style=svg)](https://circleci.com/gh/lachatak/pi4j-client/tree/master) [![Coverage Status](https://coveralls.io/repos/lachatak/pi4j-client/badge.svg?branch=master)](https://coveralls.io/r/lachatak/pi4j-client?branch=master)
- Have you ever tried to test your Raspberry Pi pi4j based java application without having a Raspberry?
- Have you ever tried to run your Raspberry Pi pi4j based java application on your own machine?
- Have you ever tried to continuously deploy new version of your Raspberry Pi pi4j based java application to the Raspberry via Wifi?
If your answer for any of the questions is yes than you know that none of them is plain sail. The project addresses the above mentioned problems.

## Goal of this project
This project is based on the [pi4j](http://pi4j.com/) java library. 
- It provides a **console** pi4j client to be able to test your application without having a physical Raspberry Pi at hand.
- It provides a **client** and a **server** artifact to be able to run your pi4j application in a remote mode where your application is running on your own machine with the **client** where it communicates with the **server** deployed to your Raspberry Pi.

## How to run
Since it is using [Aspectj](https://eclipse.org/aspectj/) and [Load-Time Weaving](https://eclipse.org/aspectj/doc/released/devguide/ltw.html) to achieve its goal you have to use **javaagent** to run your application.
```
java -javagent:aspectjweaver.java -jar -Dpi4j.client.mode=console YOUR_ARTIFACT.jar
```
More detailed description can be found the in the [examples](examples) sub-module. 
## Supported client modes

### console
- Simulates Raspberry Pi hardware in a local machine
- Captures any local native calls related to GPIO management and handles them internally 
- Maps INPUT pins to a keyboard button to mimic digital input pins.

### web (PLANNED)
- Simulates Raspberry Pi hardware in a local machine
- Captures any local native calls related to GPIO management and handles them internally
- Generates a web UI based on the provisioned pins where you can trace what is happening and also interract with INPUT pins.

### mock (PLANNED)
- Simulates Raspberry Pi hardware in a local machine
- Captures any local native calls related to GPIO management and handles them internally based on your provided pin behavior.

### remote
- Captures any local native calls related to GPIO management and forwards the requests to a remote server which is running on the Raspberry Pi where the request is handled. Response is feeded back to the client.   
First version is ready...

## Design
TBD


