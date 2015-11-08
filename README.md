# Pi4j Client [![Circle CI](https://circleci.com/gh/lachatak/pi4j-client/tree/master.svg?style=svg)](https://circleci.com/gh/lachatak/pi4j-client/tree/master) [![Coverage Status](https://coveralls.io/repos/lachatak/pi4j-client/badge.svg?service=github)](https://coveralls.io/github/lachatak/pi4j-client?branch=master)
- Have you ever tried to **run** your Raspberry Pi pi4j based java application **on your own machine**? You cannot...
- Have you ever tried to **test** your Raspberry Pi pi4j based java application **without having a Raspberry Pi**? You cannot...
- Have you ever tried to **continuously deploy** new version of your Raspberry Pi pi4j based java application to your Raspberry **via wifi** or a **slow network**? It is a pain...

If your answer is *yes* for any of the questions than you know that none of them is plain sail and this project if for you to ease your pain!

## Goal of this project
This project is based on the [pi4j](http://pi4j.com/) java library. It is not a wrapper but an extension to over bridge some weak ares of pi4j since is not really gives answers for the above mentioned questions. 
It uses [Aspectj](https://eclipse.org/aspectj/) and [Load-Time Weaving](https://eclipse.org/aspectj/doc/released/devguide/ltw.html) to capture low level native *Gpio, GpioUtil, GpioInterrupt, GpioController, NativeLibraryLoader* calls. Hijacked calls then can transparently be decorated, modified, forwarded to dedicated **clients** and fed back to pi4j callers as it were served by the native library beneath. 

## Used technology stack
- [Aspectj](https://eclipse.org/aspectj/) to capture native calls and delegate them to clients
- [Akka](http://akka.io) to provide a reactive platform for clients 
- [JNativeHook](https://github.com/kwhat/jnativehook) for the *console* client to handle keyboard events for GPIO inputs
- [Spray](http://spray.io) for the *web* client to represent GPIO changes using websockets 

## Dependencies
There are couple of artifacts you could add to your application as a dependency. 

### core 
It contains the meat of the framework. It loads the selected client mode based on the **pi4j.client.mode** environment variable. If it is not provided than the **console** mode is used by default.
Core should be always part of your dependency list if you would like to use the framework.
```
libraryDependencies += "org.kaloz.pi4j.client" %% "core" % "0.1.0-SNAPSHOT"
```

### console 
It contains the **console** client implementation.
```
libraryDependencies += "org.kaloz.pi4j.client" %% "console-client" % "0.1.0-SNAPSHOT"
```

### web 
It contains the **web** client implementation.
```
libraryDependencies += "org.kaloz.pi4j.client" %% "web-client" % "0.1.0-SNAPSHOT"
```

### remote 
It has 2 major elements. A **remote-client** which should be added to your project and the **remote-server** which should be deployed to your Raspberry Pi. 
```
libraryDependencies += "org.kaloz.pi4j.client" %% "remote-client" % "0.1.0-SNAPSHOT"
```
The server could be started with the following command:
```
sudo java -javagent:aspectjweaver.java -jar pi4j-remote-server-0.1.0-SNAPSHOT-assembly.jar  
```

## How to run
*After you have added all the dependency you need* to your pi4j project you are ready to run your application using any of the client modes.
Since it is using Aspectj to be able to capture native calls you have to use **javaagent** to run your application.
```
java -javagent:aspectjweaver.java -jar -Dpi4j.client.mode=console YOUR_ARTIFACT-assembly.jar
```
More detailed description can be found the in the [examples](examples) sub-module. 

## Supported client modes in detail
Client mode can be easily changed at start time by providing **pi4j.client.mode** environment variable for the application.

### console
It provides a **console** pi4j client mode to be able to *test/run* your application *without having a physical Raspberry Pi at hand* using the console to interact with the application 
Main features:

- Simulates Raspberry Pi GPIO management in an **in-memory model** 
- Captures any local native calls related to GPIO management and handles them internally 
- OUTPUT pin state changes simply **printed out to console** meanwhile INPUT pins are mapped to a **dedicated keyboard button to mimic digital input pins** and be able trigger callbacks if you press the assigned button
- **jnativehook** is used to handle keyboard events and pipes them back the framework
- This mode is the **default** implementation

### web
It provides a **web** pi4j client mode to be able to *test/run* your application *without having a physical Raspberry Pi at hand* using a web UI to interact with the application.  
Main features:

- Simulates Raspberry Pi GPIO management in an **in-memory model**
- Captures any local native calls related to GPIO management and handles them internally
- OUTPUT pin state changes simply **pushed to the index page** meanwhile INPUT pins are require **clicking on the pins image or push a dedicated button**. Both pt them will trigger any callback you assigned the the INPUT pin
- Generates a **web UI** based on the provisioned pins where you can trace what is happening and also interact with INPUT pins

### remote
It provides a **remote** pi4j client mode to be able to *run* your application on a remote mode where local native calls are captured and delegated to the remote server which will eventually interact with your Raspberry and post back any relevant pin state changes to you work station where the pi4j app is running. 
Main features:

- Captures any local native calls related to GPIO management and forwards the requests to the **remote server** which should be running on your Raspberry Pi in a stand alone fashion 
- Every request will be handled on the *remote Raspberry* and response *will be fed back to the originating application*
- When a new client connection starts a new session will be created at the server. After the caller application stopped it *unexports all the pins* and *waits for the next client to connect* 
- It uses *Akka Clustering* to deliver the functionality

### autopilot/mock (PLANNED)
- *Will* simulate Raspberry Pi GPIO management in an **in-memory model**
- *Will* captures any local native calls related to GPIO management and handles them internally based on your predefined pin behavior 

## Extending the current framework
The framework was designed the way it is easy to extend and add new clients. All you have to do is just implement some interfaces and follow a naming convention in your new module.

- Use the **api** dependency on your new module to be able to write your new plugin. It contains all the interfaces and Akka message types you need for your extension 
- You have to **write a new client factory** for your new client type which delivers the functionality 
- The name pattern is the following: **TYPE**ClientFactory where the **TYPE** will be registered at the start time of the application
- The factory should **wire up all the necessary components** which will handle the captured native calls
- If you have just only one client type on the classpath at runtime it will be picked up and used immediately when you start your app
- If there are more client types on the classpath then you have to provide **pi4j.client.mode** environment variable to select what you need
  
## Design
TBD


