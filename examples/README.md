## How to test
The project is based on [pi4j](http://pi4j.com). Pi4j tests were copied here from the current version to be able to see how the official tests behaves with the pi4j client directory.
There are some **sbt** alieses to be able to run them easily from your console.
```
sbt controlGpioExample
```
It basically uses **javaagent** and **aspectjweaver** to initialise the examples using **console** client.
