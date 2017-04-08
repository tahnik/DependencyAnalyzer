# DependencyAnalyzer
Finds dependency of packages

##To run in Windows using JDK 8

Clone the repo using Git bash

Navigate to src folder inside DependencyAnalyzer

Shift + Right-click to open command line window

then enter:

javac -cp ".;lib\junit-4.12.jar;lib\hamcrest-core-1.3.jar" *.java

This compiles all the files including JUnitTests. To compile without the JUnitTest:

javac -cp Main.java Package.java Utilities.java

Modify the test.txt or place your own text file under src folder

Then run the program with command like this:

java Main test.txt gui runner

You can change the test.txt with your own filename

![devRantFX mockup 1](http://i.imgur.com/WgyjMJ7.gif)
