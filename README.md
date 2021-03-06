# COP5618-Project

Group Members
-------

Junran Xie, gitid: XieJunran<br>
Jingyu Luo, gitid: ResponsoMundus<br>
Yihao Wu, gitid: zmcwu<br>

Contribution
---------

### Junran Xie<br>
Core logic of game server, including battlefield initialization, adding tank and battlefield updating according to client input.<br>

##### Contributed Source File:
* BattleField.java
* Tank.java
* Missile.java
* SafeTankFactory.java
* SafeMapFactory.java

### Jingyu Luo<br>
Game client initilization, game joining and display of battlefield.<br>

##### Contributed Source File:
* MultiPlayerGame.java
* MultiPlayerClient.java

### Yihao Wu<br>
Communication between game client and server.

##### Contributed Source File:
* Server.java
* Client.java

Project Description
--------------
This project produces a game similar to an very famous video game, Battle City. Most elements, including tank, missile, wall and water, come from the original game. The difference is that Battle City is a local game which allows one or two player to control a tank to move in 4 directions and fire missiles, and fight with enemy tanks controlled by AI to protect the base, chapter by chapter, while this game is multi-player network game. Each play controls a tank, fights with each other and wins the game by standing the last.

Running Instruction
---------------------
##### Note:
* All clients and server must be in the same LAN.
* Temporarily shut down firewall on all involved computers.
* Need at least two computers to play the game. One computer can only run at most one client. One server and client can be run on the same computer.

##### Run with IDE (Eclipse):
* Load: open the project with Eclipse.
* Build: build the project.
* Run Server: run the Server class in package cop5618.utility.
* Run Client: run the MultiPlayerClient class in package cop5618.
* Start the game: now you may see a window with a start button, click on it.
* Fight: wait for other players join in, then enjoy the game.

##### Run with makefile:
* Build: enter project path in Terminal and type "make".
* Enter Classpath: enter the "src" directory by command "cd src"
* Run Server: type "java cop5618.utility.Server" in Terminal.
* Run Client: type "java cop5618.MultiPlayerClient " plus server's IP address in Terminal as the only  Argument. For example, "java cop5618.MultiPlayerClient 127.0.0.1" to join the Server runs on localhost.
* Start the game: now you may see a window with a start button, click on it.
* Fight: wait for other players join in, then enjoy the game.
