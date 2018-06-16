# Name Find & Replace Purpose
When you have directory with large number of files and there is a word (or regex) that all names contains, now you want to remove it or replace it with another word, Here this program can help you.

# Features
* JavaFX Aplication runs via JAR file
* Replace file names keyword with replacement keyword.
* Delete file names keyword (in case you leave replacement keyword empty).
* Rollback any changes after each rename operation.

# Requirments
* [Java Runtime Environment Version 10.0.1](http://www.oracle.com/technetwork/java/javase/downloads/jre10-downloads-4417026.html) is needed to run JAR file or run it manually.
Other versions not tested so not recommended.
* I tried to write a cross-platform application so there should be no problem on java supported os's. But i only tested in Windows 10 1803.

# How to's

## How to use program
Here is a simple example: I enter "example" as the keyword, "" (empty) as the replacement keyword, and "PATH/Directory" as the path, then run the program:

![Example](https://github.com/Matin-A/Name-Find-Replace/blob/master/Assests/Example.jpg)


## How to use Regex Keyword
Regex (Regular Expressions) is a word (sequence of characters) which is a pattern to more than one word.

Regex Keyword must follow Java Regex Rules. [Here](https://docs.google.com/document/d/1CDhy9E-SLz_CeW5VSJ-uM63UPCEj2O3hQUsicKNE178/edit?usp=sharing) is a short summary of rules.